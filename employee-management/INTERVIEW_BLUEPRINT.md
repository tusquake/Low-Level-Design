# The 45-Minute LLD / Machine Coding Round Blueprint

During a 45-minute coding interview, it is physically impossible to write 25 files from scratch. The interviewer is testing your **prioritization, architectural design, and implementation efficiency**. 

This blueprint outlines how to code this Employee CRUD application step-by-step under intense time pressure, with optimized "interview-friendly" shortcuts.

---

## 1. The Priority Matrix (What to Code First)

| Priority | Component | Code Cost | Why? |
| :--- | :--- | :--- | :--- |
| **1 (Critical)** | `pom.xml` & Configuration | 2 mins | Use Spring Initializr (`start.spring.io`) to generate dependencies in 30 seconds. |
| **2 (Critical)** | JPA Entity & Repository | 5 mins | Foundation of the database schema. Standard JPA & Lombok annotations. |
| **3 (Critical)** | DTO Records | 2 mins | Java records (`public record DTO(...) {}`) eliminate 100% of class boilerplate in one line. |
| **4 (Critical)** | Service Layer & Controller | 15 mins | Core business logic and REST endpoints. Use manual mappings directly in Service. |
| **5 (Important)** | Global Exception Handler | 5 mins | One central class with `@RestControllerAdvice` to handle standard errors. |
| **6 (Optional/Extra)** | Spring Security & JWT | 10 mins | Do this last. Combine all security/JWT rules into a **single consolidated file** (see section 4). |
| **7 (Optional/Extra)** | Docker & OpenAPI | 2 mins | Keep Dockerfiles and OpenAPI config minimal. |

---

## 2. Java Records: The ultimate time-saver
Never write standard classes with getters, setters, and constructors for DTOs in an LLD interview. Use **Java Records** to define requests, responses, and wrappers in single-line statements:

```java
// Request DTO
public record EmployeeRequestDTO(
    @NotBlank String name,
    @Email String email,
    @NotBlank String department,
    @Positive BigDecimal salary
) {}

// Response DTO
public record EmployeeResponseDTO(
    Long id, String name, String email, String department, BigDecimal salary
) {}

// Generic API Wrapper
public record ApiResponse<T>(boolean success, String message, T data) {
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }
}
```

---

## 3. Inline Mapping inside the Service (No MapStruct)
Configuring MapStruct or ModelMapper requires updates to `pom.xml` and annotation configurations, which invite syntax errors. Instead, use a private helper method at the bottom of your `ServiceImpl` class for fast manual mapping:

```java
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository repository;

    // Manual mapper helper
    private EmployeeResponseDTO mapToDTO(Employee emp) {
        return new EmployeeResponseDTO(emp.getId(), emp.getName(), emp.getEmail(), emp.getDepartment(), emp.getSalary());
    }
    
    // Usage: repository.findById(id).map(this::mapToDTO).orElseThrow(...);
}
```

---

## 4. Single-File Security Setup (JWT & Auth Consolidated)
If the interviewer requests JWT authentication, **do not** write separate `JwtService`, `JwtFilter`, and `SecurityConfig` files. Create a single `SecurityConfig.java` file and embed the filter, service, and configuration inside it. This takes 5 minutes to write and requires no extra imports!

Here is the entire consolidated security code:

```java
package com.management.employee.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String SECRET = "superSecretKey123456789012345678901234567890"; // Min 256 bits

    // 1. Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Pre-configured Users
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User.builder().username("admin").password(encoder.encode("admin123")).roles("ADMIN").build();
        UserDetails user = User.builder().username("user").password(encoder.encode("user123")).roles("USER").build();
        return new InMemoryUserDetailsManager(admin, user);
    }

    // 3. Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/employees/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/v1/employees/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // 4. Compact JWT Token Generator helper
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    // 5. Embedded JWT Filter
    private static class JwtFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
                throws ServletException, IOException {
            String header = req.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                try {
                    String token = header.substring(7);
                    var payload = Jwts.parser()
                            .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8)))
                            .build()
                            .parseSignedClaims(token)
                            .getPayload();
                    
                    String username = payload.getSubject();
                    String role = payload.get("role", String.class);
                    
                    if (username != null) {
                        var auth = new UsernamePasswordAuthenticationToken(
                                username, null, List.of(new SimpleGrantedAuthority(role)));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } catch (Exception ignored) {}
            }
            chain.doFilter(req, res);
        }
    }
}
```

Using this single file config eliminates:
- Creating custom UserDetails services.
- Writing redundant JwtService helper classes.
- Configuring security filter beans across multiple files.

---

## 5. Mocking Database in Tests
Under time constraints, do not waste time configuring H2 databases in testing configs. Write simple Unit Tests with JUnit 5 and Mockito to assert controller routing and service exceptions, as shown in `EmployeeServiceTest.java`.

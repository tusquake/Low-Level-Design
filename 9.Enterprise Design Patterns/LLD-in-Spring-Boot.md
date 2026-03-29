# LLD Design Patterns in Spring Boot

Understanding design patterns in isolation is good. Seeing how a production-grade framework like Spring Boot uses them every day is where real mastery begins. This document maps each major Low Level Design pattern to its concrete usage in Spring, with a real-world analogy for each.

---

## Table of Contents

1. [Singleton Pattern](#1-singleton-pattern)
2. [Prototype Pattern](#2-prototype-pattern)
3. [Factory Pattern](#3-factory-pattern)
4. [Abstract Factory Pattern](#4-abstract-factory-pattern)
5. [Builder Pattern](#5-builder-pattern)
6. [Adapter Pattern](#6-adapter-pattern)
7. [Proxy Pattern](#7-proxy-pattern)
8. [Decorator Pattern](#8-decorator-pattern)
9. [Facade Pattern](#9-facade-pattern)
10. [Composite Pattern](#10-composite-pattern)
11. [Strategy Pattern](#11-strategy-pattern)
12. [Observer Pattern](#12-observer-pattern)
13. [Chain of Responsibility Pattern](#13-chain-of-responsibility-pattern)
14. [Template Method Pattern](#14-template-method-pattern)
15. [Front Controller Pattern](#15-front-controller-pattern)
16. [Command Pattern](#16-command-pattern)
17. [State Pattern](#17-state-pattern)
18. [Interpreter Pattern](#18-interpreter-pattern)
19. [Iterator Pattern](#19-iterator-pattern)
20. [Flyweight Pattern](#20-flyweight-pattern)
21. [Mediator Pattern](#21-mediator-pattern)
22. [Thread Safety in Spring](#thread-safety-in-spring)
23. [Quick Reference Table](#quick-reference-table)

---

## 1. Singleton Pattern

**Spring Component:** `@Service`, `@Repository`, `@Component`, `@Controller`

**What it means:** Only one instance of the class exists for the entire lifetime of the application. Every part of the code that asks for this bean gets the exact same object.

**Real-world analogy:** A company has one CEO. No matter which department contacts the CEO's office, they deal with the same person — not a new CEO created for every call.

**How Spring does it differently from classic Singleton:** In the classic pattern, the class manages its own single instance using a `private static` field. In Spring, the framework (IoC container) manages it externally via Dependency Injection. You never call `new MyService()` — Spring hands you the one existing instance.

```java
@Service
public class PaymentService {
    // Spring creates exactly one instance of this.
    // Every controller, every thread, every request — same object.
}
```

> **Key insight:** Singletons are safe in Spring because these classes are designed to be **stateless** — they hold no user-specific data, only behaviour.

---

## 2. Prototype Pattern

**Spring Component:** `@Scope("prototype")`

**What it means:** Every time the bean is requested from the Spring container, a brand new instance is created and returned.

**Real-world analogy:** A photocopier. You have one original document (the bean definition), and every time someone presses "copy", they get a fresh new copy — not the original.

**When to use it:** When your bean holds state — for example, a shopping cart object, a file upload handler, or a per-user task runner. You do not want users sharing the same instance.

```java
@Component
@Scope("prototype")
public class ReportGenerator {
    private String reportData; // Stateful — each caller needs their own copy
}
```

> **Contrast with Singleton:** Singleton = one shared instance (stateless). Prototype = new instance per request (stateful).

---

## 3. Factory Pattern

**Spring Component:** `BeanFactory`, `FactoryBean<T>`

**What it means:** Instead of the caller creating objects directly with `new`, a "factory" is responsible for deciding what to create and how to create it.

**Real-world analogy:** A car rental agency. You do not build the car yourself — you walk up to the counter (the factory), say what you need, and they hand you the right vehicle. You never worry about the assembly.

**Two flavours in Spring:**

- **`BeanFactory`** — the core Spring container itself. It reads your configuration and creates, manages, and wires all your beans.
- **`FactoryBean<T>`** — an interface you implement when you need to create a complex object that cannot simply be instantiated with `new`.

```java
public class MyConnectionFactoryBean implements FactoryBean<Connection> {

    @Override
    public Connection getObject() throws Exception {
        // Complex setup logic: load config, set timeouts, create pool, etc.
        return DriverManager.getConnection(url, user, password);
    }

    @Override
    public Class<?> getObjectType() {
        return Connection.class;
    }
}
```

> **Real example:** `LocalContainerEntityManagerFactoryBean` in Spring JPA — it builds a fully configured `EntityManagerFactory` with all your Hibernate settings applied.

---

## 4. Abstract Factory Pattern

**Spring Component:** `JpaVendorAdapter`, `PlatformTransactionManagerFactoryBean`

**What it means:** An abstract factory creates families of related objects. You do not pick individual objects — you pick a "family" and get all the related parts pre-configured to work together.

**Real-world analogy:** Choosing a furniture brand. If you choose IKEA, you get IKEA-style tables, chairs, and shelves that all match. If you switch to another brand, every piece changes together — you do not mix and match individual pieces.

**How Spring uses it:** `JpaVendorAdapter` is an abstract factory for your JPA setup. You choose a vendor (Hibernate or EclipseLink), and Spring gets a matching `Dialect`, `PlatformTransactionManager`, and exception translator — all from the same family.

```java
@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter()); // Choose the "family"
    return em;
}
```

---

## 5. Builder Pattern

**Spring Component:** `ResponseEntity`, `UriComponentsBuilder`, `MockMvcRequestBuilders`, `WebClient`

**What it means:** Construct a complex object step by step using a fluent chain of method calls, instead of one giant constructor with 10 parameters.

**Real-world analogy:** Ordering a custom burger. You do not hand the chef one giant instruction sheet. You say: "Start with a sesame bun. Add a beef patty. Add cheese. Add lettuce. No onions. Build." Each step adds one thing.

```java
// Building an HTTP response step by step
ResponseEntity.status(HttpStatus.CREATED)
    .header("X-Request-Id", requestId)
    .contentType(MediaType.APPLICATION_JSON)
    .body(createdUser);

// Building a URI step by step
URI uri = UriComponentsBuilder
    .fromHttpUrl("https://api.example.com")
    .path("/users/{id}")
    .queryParam("format", "json")
    .buildAndExpand(userId)
    .toUri();
```

> **Why it matters:** Avoids "telescoping constructors" — constructors with so many parameters you cannot remember what each one means.

---

## 6. Adapter Pattern

**Spring Component:** `HandlerAdapter`, `JpaVendorAdapter`, `MessageConverter`

**What it means:** Converts the interface of an existing class into a different interface that a client expects. The adapter sits in the middle and translates.

**Real-world analogy:** A travel power adapter. Your Indian plug (3 pins) cannot fit into a European socket (2 round pins). The adapter converts one format to the other — neither the plug nor the socket changes.

**How Spring MVC uses it:** `DispatcherServlet` knows one interface — `HandlerAdapter`. Your controllers can be written in wildly different ways: a classic `@Controller`, a `HttpRequestHandler`, a `@RestController`. Each style has its own `HandlerAdapter` that "translates" it into the standard form `DispatcherServlet` expects.

```
DispatcherServlet
    --> finds HandlerAdapter for your @Controller
    --> HandlerAdapter calls your method and adapts the return value
    --> DispatcherServlet gets a standard ModelAndView back
```

> **Real example:** `JpaVendorAdapter` adapts Hibernate's proprietary API to Spring Data JPA's generic interface, so you can swap Hibernate for EclipseLink without changing your repository code.

---

## 7. Proxy Pattern

**Spring Component:** Spring AOP, `@Transactional`, `@Cacheable`, `@Secured`, `@Async`

**What it means:** A proxy object wraps your real object. The caller talks to the proxy, which adds extra behaviour (logging, transactions, security checks) before and after forwarding the call to the real object.

**Real-world analogy:** A personal assistant standing in front of an executive. Before you get to meet the executive, the assistant checks if you have an appointment (security), logs your visit (auditing), and after your meeting, sends you a follow-up email (post-processing). The executive does none of that themselves.

**How it works in Spring:**

When you annotate a method with `@Transactional`, Spring does not modify your class. Instead, at runtime, it creates a **proxy** that wraps your bean. When the method is called, the proxy runs first.

```
Your code calls: orderService.placeOrder()

What actually runs:
  [CGLIB Proxy]
      --> BEGIN TRANSACTION
      --> calls real orderService.placeOrder()
      --> COMMIT or ROLLBACK
      --> returns result to your code
```

**Two types of proxies Spring uses:**

- **JDK Dynamic Proxy** — used when your class implements an interface
- **CGLIB Proxy** — used when your class does not implement an interface (subclasses your class at runtime)

> **Important gotcha:** If you call a `@Transactional` method from within the same class, the proxy is bypassed and the transaction does not start. The call must come from outside the class to go through the proxy.

---

## 8. Decorator Pattern

**Spring Component:** `HttpServletRequestWrapper`, `OncePerRequestFilter`, `BeanDefinitionDecorator`

**What it means:** Adds new behaviour to an existing object by wrapping it, without changing the original class. You can stack multiple decorators.

**Real-world analogy:** A coffee shop. You start with a plain coffee. You "decorate" it with milk. Then with sugar. Then with vanilla syrup. Each addition wraps the previous cup — the original coffee is still in there, just with more added around it.

**How Spring uses it:** In Spring Security, the incoming `HttpServletRequest` is often wrapped in a `SecurityContextHolderAwareRequestWrapper`. This adds methods like `isUserInRole()` to the request without touching the original servlet code.

```java
// Spring Security wraps the raw request and adds auth-aware methods
HttpServletRequest original = ...;
HttpServletRequest decorated = new SecurityContextHolderAwareRequestWrapper(original, rolePrefix);

decorated.isUserInRole("ADMIN"); // Added by the decorator — not in the original
```

> **Difference from Proxy:** Proxy controls access to the object. Decorator adds new features to the object. Subtle but important.

---

## 9. Facade Pattern

**Spring Component:** `SLF4J`, `JdbcTemplate`, `RedisTemplate`

**What it means:** Provides a single, simple interface to a complex subsystem. The caller does not need to know which components are underneath.

**Real-world analogy:** A hotel reception desk. You walk up and say "I need a taxi, room service, and a wake-up call." One person handles it all. Behind the scenes, they coordinate with the transport team, the kitchen, and housekeeping — you do not call each department yourself.

**How Spring uses it with SLF4J:** There are many Java logging libraries: Logback, Log4j2, java.util.logging. SLF4J is a facade that gives you one clean API. You write `log.info(...)` everywhere. Whether Logback or Log4j2 is running underneath does not matter — SLF4J routes the call correctly.

```java
// You write this everywhere — the same code regardless of the logging library below
private static final Logger log = LoggerFactory.getLogger(OrderService.class);

log.info("Order placed: {}", orderId);
```

> **`JdbcTemplate` as a Facade:** It hides the complexity of getting a `Connection`, creating a `PreparedStatement`, handling `SQLExceptions`, and closing everything. You just write the SQL and handle the result.

---

## 10. Composite Pattern

**Spring Component:** `CompositeMessageConverter`, `HandlerMethodArgumentResolverComposite`, `AuthenticationManager`

**What it means:** A group of objects is treated as if it were a single object. The composite delegates the task to its children and returns the combined result.

**Real-world analogy:** A company org chart. You send one email to "the engineering team" (the composite). That email reaches every individual engineer (the leaves). You do not send individual emails to each person.

**How Spring uses it:** When Spring needs to convert your object to a response (JSON, XML, plain text), it asks `CompositeMessageConverter`. This composite holds a list of individual converters — `MappingJackson2HttpMessageConverter`, `StringHttpMessageConverter`, etc. It tries each one until it finds the right fit.

```
Client Request (Accept: application/json)
    --> CompositeMessageConverter.write(myObject)
        --> tries StringHttpMessageConverter      [no match]
        --> tries MappingJackson2HttpMessageConverter [MATCH]
        --> writes JSON response
```

---

## 11. Strategy Pattern

**Spring Component:** `AuthenticationProvider`, `ResourceLoader`, `SortHandlerMethodArgumentResolver`

**What it means:** Define a family of algorithms, put each one in its own class, and make them interchangeable at runtime.

**Real-world analogy:** A GPS navigation app. It has three route strategies: Fastest, Shortest, and Avoid Tolls. You pick one at runtime. The app uses whichever strategy you chose — the rest of the navigation code stays the same.

**How Spring Security uses it:** `AuthenticationManager` holds a list of `AuthenticationProvider` implementations. When a user tries to log in, it tries each provider in order: LDAP, database, JWT, OAuth. You can add or remove providers without changing the login logic.

```java
@Bean
public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    return http.getSharedObject(AuthenticationManagerBuilder.class)
        .authenticationProvider(new JwtAuthenticationProvider())    // Strategy 1
        .authenticationProvider(new LdapAuthenticationProvider())   // Strategy 2
        .build();
}
```

**Resource loading strategy:**

| Prefix       | Strategy Used            |
|--------------|--------------------------|
| `classpath:` | `ClassPathResourceLoader`|
| `file:`      | `FileSystemResourceLoader`|
| `https:`     | `UrlResourceLoader`       |

---

## 12. Observer Pattern

**Spring Component:** `ApplicationEventPublisher`, `ApplicationListener`, `@EventListener`

**What it means:** When an object (the subject) changes state, all its dependents (observers) are automatically notified. The subject does not know who is listening.

**Real-world analogy:** A YouTube channel. When you post a video, every subscriber gets a notification. You do not know who subscribed — you just publish. Subscribers independently decide what to do when notified.

**How Spring uses it:**

```java
// 1. Define the event
public class UserRegisteredEvent extends ApplicationEvent {
    private final String email;
    public UserRegisteredEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}

// 2. Publish the event (the subject — knows nothing about listeners)
@Service
public class UserService {
    @Autowired private ApplicationEventPublisher publisher;

    public void register(User user) {
        userRepository.save(user);
        publisher.publishEvent(new UserRegisteredEvent(this, user.getEmail()));
        // Done. No knowledge of what happens next.
    }
}

// 3. Multiple independent listeners (observers)
@Component
public class WelcomeEmailListener {
    @EventListener
    public void handle(UserRegisteredEvent event) { /* send email */ }
}

@Component
public class AnalyticsListener {
    @EventListener
    public void handle(UserRegisteredEvent event) { /* log to analytics */ }
}
```

> **Key benefit:** You can add or remove listeners without touching `UserService`. The components are fully decoupled.

---

## 13. Chain of Responsibility Pattern

**Spring Component:** `FilterChain` (Spring Security), `HandlerInterceptor`, `ExceptionHandlerExceptionResolver`

**What it means:** A request passes through a chain of handlers. Each handler either processes it and passes it on, or stops the chain entirely.

**Real-world analogy:** Airport security. You pass through check-in, then baggage screening, then passport control, then boarding gate check. Each step either lets you through or stops you. The airline at the gate does not know about the baggage screening step — each checkpoint is independent.

**How Spring Security FilterChain works:**

```
Incoming HTTP Request
    --> UsernamePasswordAuthenticationFilter  (checks credentials)
    --> BasicAuthenticationFilter            (checks Basic Auth header)
    --> CorsFilter                           (checks CORS policy)
    --> ExceptionTranslationFilter           (catches auth errors)
    --> FilterSecurityInterceptor            (checks authorization)
    --> Your Controller
```

Each filter calls `chain.doFilter(request, response)` to pass the request forward. If authentication fails, the filter returns a 401 and never calls `chain.doFilter()` — the chain breaks.

```java
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        log.info("Incoming: {}", req.getRequestURI());
        chain.doFilter(req, res); // pass to next filter
        log.info("Outgoing: {} {}", req.getRequestURI(), res.getStatus());
    }
}
```

---

## 14. Template Method Pattern

**Spring Component:** `JdbcTemplate`, `RestTemplate`, `KafkaTemplate`, `AbstractController`

**What it means:** Define the skeleton of an algorithm in a base class. Leave the specific steps to be filled in by the caller or subclass. The overall flow is fixed; only the details change.

**Real-world analogy:** A standard loan application process at a bank. The bank always follows the same steps: verify identity, check credit score, assess income, decide. What changes is the specific documents you provide. The algorithm skeleton is fixed; the specifics vary per customer.

**How `JdbcTemplate` does it:**

The fixed skeleton (handled by Spring):
1. Get a connection from the pool
2. Create a `PreparedStatement`
3. Execute your query
4. Map results
5. Close everything
6. Handle all `SQLExceptions`

What you provide (the variable part):
- The SQL query
- The result mapping logic

```java
List<User> users = jdbcTemplate.query(
    "SELECT * FROM users WHERE active = ?",          // Your SQL
    ps -> ps.setBoolean(1, true),                    // Your parameters
    (rs, row) -> new User(rs.getLong("id"), rs.getString("name")) // Your mapping
);
```

> You write 3 lines. `JdbcTemplate` handles 50 lines of boilerplate underneath.

---

## 15. Front Controller Pattern

**Spring Component:** `DispatcherServlet`

**What it means:** A single entry point receives all requests, then routes each one to the appropriate handler. Common cross-cutting concerns (security, logging, encoding) are handled centrally.

**Real-world analogy:** A hospital reception. Every patient who enters goes to the reception first, regardless of whether they need cardiology, orthopaedics, or emergency care. Reception checks them in, verifies insurance, and routes them to the right department. No department is directly accessible from the front door.

**How `DispatcherServlet` works:**

```
All HTTP Requests --> DispatcherServlet
    --> HandlerMapping  (which controller handles this URL?)
    --> HandlerAdapter  (how do we call this controller type?)
    --> Your @Controller method
    --> ViewResolver    (which template/view do we render?)
    --> Response
```

All requests go through one place. This is why Spring can apply security, internationalization, and logging globally without you adding it to every controller.

---

## 16. Command Pattern

**Spring Component:** `@Async`, `TaskExecutor`, `CommandLineRunner`, `ApplicationRunner`

**What it means:** Encapsulate a request or operation as an object. This lets you queue it, execute it later, log it, undo it, or retry it — all without the caller knowing any details.

**Real-world analogy:** A restaurant order ticket. The waiter writes your order on a ticket (the command object) and passes it to the kitchen. The kitchen executes it when ready. The waiter does not stand and watch — they are decoupled from the cooking. The ticket can be queued, prioritised, or re-issued.

**How Spring uses it with `@Async`:**

```java
@Service
public class EmailService {

    @Async
    public CompletableFuture<Void> sendWelcomeEmail(String to) {
        // This is wrapped as a command and queued in a thread pool
        // The caller returns immediately — does not wait for this to finish
        emailClient.send(to, "Welcome!");
        return CompletableFuture.completedFuture(null);
    }
}
```

**`CommandLineRunner` as Command Pattern:**

```java
@Component
public class DataMigrationRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        // This command runs once on startup
    }
}
```

---

## 17. State Pattern

**Spring Component:** Spring State Machine (`spring-statemachine`)

**What it means:** An object changes its behaviour based on its internal state. Instead of writing massive `if/else` or `switch` blocks to handle every state, each state is its own class that knows what to do.

**Real-world analogy:** A traffic light. When it is Red, it ignores the "go" signal. When it is Green, it ignores the "stop now" signal. The light's behaviour is entirely determined by which state it is currently in. There is no central controller checking "if red, do this; if green, do that."

**Real-world Spring example — Order lifecycle:**

```
PLACED --> PAID --> PROCESSING --> SHIPPED --> DELIVERED
                                           --> RETURNED
```

Each state defines: what actions are allowed, what events can trigger a transition, and what happens on entry/exit.

```java
@Configuration
@EnableStateMachine
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderEvent> {

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
        transitions
            .withExternal().source(PLACED).target(PAID).event(PAYMENT_RECEIVED)
            .and()
            .withExternal().source(PAID).target(SHIPPED).event(ITEM_SHIPPED)
            .and()
            .withExternal().source(SHIPPED).target(DELIVERED).event(DELIVERY_CONFIRMED);
    }
}
```

> **Without State Machine:** Imagine 6 states and 10 transitions as `if/else` chains in one class. It becomes unmaintainable within weeks.

---

## 18. Interpreter Pattern

**Spring Component:** SpEL (Spring Expression Language)

**What it means:** Define a language or expression format, then build an interpreter that parses and evaluates expressions written in that language at runtime.

**Real-world analogy:** A calculator. You type `(3 + 5) * 2`. The calculator parses this string into a tree of operations, then evaluates the tree to get `16`. The string is the "expression"; the calculator is the "interpreter."

**How SpEL works:**

Spring parses the string expression, builds an Abstract Syntax Tree (AST) of expression nodes, then evaluates it at runtime.

```java
// Inject a value from a property, transformed at runtime
@Value("#{T(Math).random() * 100}")
private double randomValue;

// Use SpEL in @PreAuthorize (evaluated at runtime for each request)
@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
public User getUser(Long userId) { ... }

// Use SpEL in @Cacheable to build a dynamic cache key
@Cacheable(value = "products", key = "#category + '_' + #page")
public List<Product> getProducts(String category, int page) { ... }
```

---

## 19. Iterator Pattern

**Spring Component:** `ResultSetExtractor`, `JdbcCursorItemReader` (Spring Batch), `Page<T>` in Spring Data

**What it means:** Provides a standard way to traverse a collection without exposing its internal structure. The caller just calls `hasNext()` and `next()` — it does not care if the data comes from a list, a database cursor, or a file.

**Real-world analogy:** A TV remote control. You press "Next" to go to the next channel. You do not need to know how channels are stored internally (frequency table, cable mapping, etc.). The remote gives you a uniform way to iterate.

**How Spring Batch uses it:** `JdbcCursorItemReader` reads millions of database rows one at a time using an iterator, without loading the entire result set into memory.

```java
@Bean
public JdbcCursorItemReader<Customer> reader(DataSource dataSource) {
    return new JdbcCursorItemReaderBuilder<Customer>()
        .dataSource(dataSource)
        .sql("SELECT * FROM customers WHERE active = true")
        .rowMapper(new CustomerRowMapper())
        .build();
    // Spring Batch calls reader.read() in a loop — iterator pattern
}
```

**Spring Data `Page<T>`** also follows this: instead of returning all 10,000 records, it lets you iterate through pages.

---

## 20. Flyweight Pattern

**Spring Component:** Connection Pooling (HikariCP), Bean caching in `ApplicationContext`

**What it means:** Share a pool of reusable objects instead of creating a new object every time. This drastically reduces memory usage when many identical or similar objects are needed.

**Real-world analogy:** A library. Instead of every reader buying their own copy of a book, the library keeps a few shared copies. Readers borrow one, use it, and return it. The same physical book serves hundreds of readers over time.

**How connection pooling works (HikariCP — Spring Boot default):**

Creating a new database connection is expensive (network handshake, authentication, memory allocation). HikariCP maintains a pool of pre-created connections. Your code borrows one, uses it, and returns it to the pool.

```yaml
# application.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10      # 10 shared connections serve hundreds of requests
      minimum-idle: 5
      connection-timeout: 30000
```

> Without connection pooling, a 100-requests/second application would create and destroy 100 database connections per second — most databases would collapse.

---

## 21. Mediator Pattern

**Spring Component:** `DispatcherServlet`, `ApplicationEventPublisher`, Spring Cloud Gateway

**What it means:** Instead of objects talking directly to each other (tight coupling), they all talk to a central mediator. The mediator handles all coordination.

**Real-world analogy:** Air traffic control (ATC). Planes do not communicate with each other directly — that would be chaotic. Every plane talks only to the ATC tower. The tower coordinates who lands when, who waits, and who goes around again.

**How `DispatcherServlet` acts as Mediator:**

Controllers, `HandlerMappings`, `ViewResolvers`, and `MessageConverters` never call each other. They all register with `DispatcherServlet`, which coordinates the entire request lifecycle.

```
WITHOUT Mediator:  Controller --> ViewResolver --> MessageConverter --> FilterChain (spaghetti)

WITH Mediator:
    Controller       --> DispatcherServlet
    ViewResolver     --> DispatcherServlet
    MessageConverter --> DispatcherServlet
    FilterChain      --> DispatcherServlet
```

**`ApplicationEventPublisher` as Mediator:**

Services never call each other directly. They publish events to the publisher (mediator), which routes them to the correct listeners.

---

## Thread Safety in Spring

Spring Boot runs on an embedded Tomcat server, which is multi-threaded by default. Understanding how Spring handles concurrency is critical.

**Why Singleton beans are safe by default:**

Most Spring beans are stateless — they contain no instance variables that change per request. A stateless object can be shared across hundreds of threads with no issues because each thread only reads behaviour, never writes shared state.

```java
@Service
public class TaxCalculator {
    // No instance variables that hold user data
    // This is safe to share across all threads
    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(new BigDecimal("0.18")); // GST
    }
}
```

**Dangerous pattern — do not do this:**

```java
@Service
public class OrderService {
    private Order currentOrder; // DANGER: Shared state in a singleton bean
    // Thread A sets currentOrder, Thread B immediately overwrites it
}
```

**Spring's tools for thread safety:**

| Mechanism | Purpose |
|---|---|
| `ThreadLocal` (via `SecurityContextHolder`) | Stores per-thread data (current user) without sharing |
| `@Async` + `TaskExecutor` | Runs methods in a managed thread pool |
| `@Transactional` | Ensures each thread gets its own transaction scope |
| Prototype scope | Gives each requester their own bean instance |
| Immutable objects | Objects that never change are always thread-safe |

---

## Quick Reference Table

| Pattern | Spring Component | Simple Description | Real-World Analogy |
|---|---|---|---|
| Singleton | `@Service`, `@Repository` | One shared instance | One CEO for the whole company |
| Prototype | `@Scope("prototype")` | New instance every time | Photocopier makes fresh copies |
| Factory | `BeanFactory`, `FactoryBean` | Centralised object creation | Car rental agency |
| Abstract Factory | `JpaVendorAdapter` | Creates families of related objects | Choosing a furniture brand |
| Builder | `ResponseEntity`, `UriComponentsBuilder` | Step-by-step object construction | Custom burger order |
| Adapter | `HandlerAdapter`, `JpaVendorAdapter` | Converts one interface to another | Travel power adapter |
| Proxy | `@Transactional`, `@Cacheable` | Wraps object to add behaviour | Personal assistant screening calls |
| Decorator | `HttpServletRequestWrapper` | Adds features without changing original | Adding milk and sugar to coffee |
| Facade | SLF4J, `JdbcTemplate` | Simplifies a complex subsystem | Hotel reception desk |
| Composite | `CompositeMessageConverter` | Group treated as a single unit | Emailing "the team" |
| Strategy | `AuthenticationProvider` | Swappable algorithms at runtime | GPS route strategies |
| Observer | `ApplicationEventPublisher` | Notify all interested parties | YouTube subscription notifications |
| Chain of Responsibility | `FilterChain` | Sequential, decoupled processing | Airport security checkpoints |
| Template Method | `JdbcTemplate`, `RestTemplate` | Fixed skeleton, variable steps | Bank loan process |
| Front Controller | `DispatcherServlet` | Single entry point for all requests | Hospital reception routing patients |
| Command | `@Async`, `TaskExecutor` | Encapsulate request as an object | Restaurant order ticket |
| State | Spring State Machine | Behaviour changes with internal state | Traffic light |
| Interpreter | SpEL | Parse and evaluate string expressions | A calculator |
| Iterator | `JdbcCursorItemReader`, `Page<T>` | Traverse collection without knowing internals | TV remote "next channel" |
| Flyweight | HikariCP connection pool | Share reusable objects from a pool | Library book borrowing |
| Mediator | `DispatcherServlet`, Event publisher | Centralised coordination between objects | Air traffic control |

---

## Further Reading

- [Spring Framework Documentation](https://docs.spring.io/spring-framework/reference/)
- [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture/)
- [Spring State Machine Reference](https://docs.spring.io/spring-statemachine/docs/current/reference/)
- [Spring Batch Reference](https://docs.spring.io/spring-batch/docs/current/reference/html/)
- *Design Patterns: Elements of Reusable Object-Oriented Software* — Gang of Four
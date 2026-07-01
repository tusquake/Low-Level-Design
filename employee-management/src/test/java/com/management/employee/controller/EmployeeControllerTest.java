package com.management.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.employee.config.JwtService;
import com.management.employee.dto.EmployeeRequestDTO;
import com.management.employee.dto.EmployeeResponseDTO;
import com.management.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.management.employee.config.SecurityConfig;
import com.management.employee.config.JwtAuthenticationFilter;
import org.springframework.context.annotation.Import;

@WebMvcTest(EmployeeController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeRequestDTO requestDTO;
    private EmployeeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new EmployeeRequestDTO(
                "Jane Doe",
                "jane.doe@example.com",
                "Product Management",
                new BigDecimal("110000.00")
        );

        responseDTO = new EmployeeResponseDTO(
                2L,
                "Jane Doe",
                "jane.doe@example.com",
                "Product Management",
                new BigDecimal("110000.00"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createEmployee_AsAdmin_Returns201() throws Exception {
        when(employeeService.createEmployee(any(EmployeeRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/v1/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Employee created successfully"))
                .andExpect(jsonPath("$.data.email").value("jane.doe@example.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createEmployee_AsUser_Returns403() throws Exception {
        mockMvc.perform(post("/api/v1/employees")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getEmployeeById_AsUser_Returns200() throws Exception {
        when(employeeService.getEmployeeById(2L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/v1/employees/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(2));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateEmployee_AsAdmin_Returns200() throws Exception {
        when(employeeService.updateEmployee(any(Long.class), any(EmployeeRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/employees/2")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Jane Doe"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteEmployee_AsUser_Returns403() throws Exception {
        mockMvc.perform(delete("/api/v1/employees/2")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}

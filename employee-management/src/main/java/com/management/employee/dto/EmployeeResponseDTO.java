package com.management.employee.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EmployeeResponseDTO(
        Long id,
        String name,
        String email,
        String department,
        BigDecimal salary,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {}

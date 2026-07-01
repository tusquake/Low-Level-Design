package com.management.employee.dto;

import java.util.List;

public record AuthResponse(
        String token,
        String username,
        List<String> roles
) {}

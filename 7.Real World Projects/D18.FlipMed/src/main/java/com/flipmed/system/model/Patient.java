package com.flipmed.system.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Patient {
    private String id;
    private String name;
}

package com.bff.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatusLogResponse {
    private String status;
    private LocalDateTime updatedAt;
}

package com.bff.demo.response.applicationActivityLog;

import com.bff.demo.modal.TaskDefinition;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StatusLogResponse {

    private TaskDefinition.TaskStatus status;
    private LocalDateTime updatedAt;

    public StatusLogResponse(TaskDefinition.TaskStatus status, LocalDateTime updatedAt) {
        this.status = status;
        this.updatedAt = updatedAt;
    }



}


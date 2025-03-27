package com.bff.demo.response;



import com.bff.demo.model.TaskDefinition;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
    @AllArgsConstructor
    public class StatusLogResponse {
        private TaskDefinition.TaskStatus status;
        private LocalDateTime updatedAt;


}






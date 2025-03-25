package com.bff.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
public class TaskDetailsResponse {
    private String funnel;
    private String actorId;
    private String status;
    private LocalDateTime updatedAt;
    private String taskId;
    private String targetTaskId;
    private int sendbacks;
    private int visited;
    private Map<String, Object> metadata;
}

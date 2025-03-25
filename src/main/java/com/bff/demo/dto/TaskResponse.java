package com.bff.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TaskResponse {
    private String taskId;
    private Integer order;
    private String handledBy;
    private LocalDateTime createdAt;
    private List<StatusLogResponse> statusLogs;
    private String targetTaskId;
    private long duration;
    private int sendbacks;
    private int visited;
    private String sourceLoanStage;
    private String sourceSubModule;
}

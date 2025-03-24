package com.bff.demo.response.applicationActivityLog;



import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TaskResponse {
    private String taskId;
    private int order;
    private String handledBy;
    private LocalDateTime createdAt;
    private List<StatusLogResponse> statusHistory;
    private String targetTaskId;
    private long duration;
    private int sendbacks;
    private int visited;
    private String sourceLoanStage;
    private String sourceSubModule;



}

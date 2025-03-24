package com.bff.demo.response.applicationActivityLog;


import com.bff.demo.model.TaskDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@AllArgsConstructor
public class TaskDetailsResponse {
    private String funnel;
    private String actorId;
    private TaskDefinition.TaskStatus status;
    private LocalDateTime updatedAt;
    private String taskId;
    private String targetTaskId;
    private int sendbacks;
    private int duration;
    private Map<String, Object> metadata;

}
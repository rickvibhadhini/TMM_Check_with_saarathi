package com.bff.demo.model.actorMetricsModel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskEntity {

    private String taskId;
    private double duration;
    private int visited;
    private String status;
}
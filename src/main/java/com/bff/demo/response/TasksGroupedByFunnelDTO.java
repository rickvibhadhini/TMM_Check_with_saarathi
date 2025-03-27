package com.bff.demo.response;

import lombok.Builder;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Builder
public class TasksGroupedByFunnelDTO {
    private LinkedHashMap<String, FunnelDataDTO> tasksGroupedByFunnel;
    private Map<String, Map<String, Map<String, TaskResponse>>> sendbackTasks;
    private LatestTaskStateDTO latestTaskState;
}
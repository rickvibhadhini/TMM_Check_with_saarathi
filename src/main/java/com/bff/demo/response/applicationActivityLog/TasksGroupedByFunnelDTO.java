package com.bff.demo.response.applicationActivityLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasksGroupedByFunnelDTO {
    private FunnelTasksResponseDTO tasksGroupedByFunnel;
    private SendbackTasksResponseDTO sendbackTasks;
    private LatestTaskStateDTO latestTaskState;
}

package com.bff.demo.response.applicationActivityLog;


import lombok.Data;

import java.util.List;

@Data
public class FunnelGroupResponse {
    private String funnelName;
    private List<TaskDetailsResponse> tasks;
    private long funnelDuration;

    public FunnelGroupResponse(String funnelName, List<TaskDetailsResponse> tasks) {
        this.funnelName = funnelName;
        this.tasks = tasks;
        this.funnelDuration = 0; // Default value
    }

    //  constructor with duration
    public FunnelGroupResponse(String funnelName, List<TaskDetailsResponse> tasks, long funnelDuration) {
        this.funnelName = funnelName;
        this.tasks = tasks;
        this.funnelDuration = funnelDuration;
    }
}
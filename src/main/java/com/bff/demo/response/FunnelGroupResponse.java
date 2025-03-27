package com.bff.demo.response;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor

public class FunnelGroupResponse {
    private String funnelName;
    private List<TaskDetailsResponse> tasks;
    private long funnelDuration;
}
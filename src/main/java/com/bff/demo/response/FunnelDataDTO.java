package com.bff.demo.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FunnelDataDTO {
    private String funnel;
    private long funnelDuration;
    private List<TaskResponse> tasks;
}
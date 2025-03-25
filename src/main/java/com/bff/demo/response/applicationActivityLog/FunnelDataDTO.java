package com.bff.demo.response.applicationActivityLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunnelDataDTO {
    private String funnel;
    private long funnelDuration;
    private List<TaskResponse> tasks;
}

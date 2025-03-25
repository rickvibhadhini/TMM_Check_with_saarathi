package com.bff.demo.response.applicationActivityLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunnelTasksResponseDTO {
    private Map<String, FunnelDataDTO> tasksGroupedByFunnel;
}

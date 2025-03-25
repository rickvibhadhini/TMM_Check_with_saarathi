package com.bff.demo.response.applicationActivityLog;
import com.bff.demo.model.TaskDefinition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LatestTaskStateDTO {
    private String taskId;
    private Integer order;
    private String handledBy;
    private LocalDateTime createdAt;
    private TaskDefinition.TaskStatus status;
    private LocalDateTime updatedAt;
    private long duration;
    private Integer sendbacks;
    private Integer visited;
}


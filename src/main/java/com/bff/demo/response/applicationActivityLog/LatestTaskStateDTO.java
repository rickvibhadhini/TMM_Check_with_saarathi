package com.bff.demo.response.applicationActivityLog;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
public class LatestTaskStateDTO {
    private String taskId;
    private Integer order;
    private String handledBy;
    private LocalDateTime createdAt;
    private String status;
    private LocalDateTime updatedAt;
    private long duration;
    private int sendbacks;
    private int visited;
}
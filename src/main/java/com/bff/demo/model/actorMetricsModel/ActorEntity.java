package com.bff.demo.model.actorMetricsModel;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "actor_metrics")
@Builder
public class ActorEntity {

    @Id
    private String id;
    private String actorId;
    private String actorType;
    private String funnel;
    private String applicationId;
    private List<TaskEntity> tasks;
    private Long totalDuration;
    private Instant lastUpdatedAt;
    private String handledBy;
}

package com.bff.demo.modal;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Document(collection = "task_execution")
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskExecution {

    @Id
    private String _id;

    private String entityId;

    private String applicationId;

    private String taskId;

    private String version;

    private Integer order;

    private String templateId;

    private String templateVersion;

    private String funnel;

    private String channel;

    private String productType;

    private String journeyType;

    private WorkflowConfig workflow;

    private String entityIdentifier;

    private TaskDefinition.EntityType entityType;

    private com.bff.demo.modal.ApplicantType applicantType;

    private String applicantId;

    private String actorId;

    private String actorType;

    private String agentId;

    private Boolean required;

    private TaskDefinition.TaskStatus status;

    private String statusReason;

    private TaskDefinition.TaskExecutionType executionType;

    private Boolean automationSupported;

    private Boolean optional;

    private Map<String, Object> metadata;

    private SendbackMetadata sendbackMetadata;

    private LocalDateTime skippedAt;

    private com.bff.demo.modal.SkippedReason skippedReason;

    private Map<String, String> inputResourceConfig;

    @Builder.Default
    private Map<String, String> inputResourceValueMap = new HashMap<>();

    private Map<String, Object> taskRuleContext;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    private String taskInput;

    private String requestId;

    @LastModifiedBy
    private String handledBy;

    private com.bff.demo.modal.ActorDetail actorDetail;

    public static TaskExecution from(TaskDefinition taskDefinition) {
        return TaskExecution.builder()
                .taskId(taskDefinition.getName())
                .entityType(taskDefinition.getEntityType())
                .version(taskDefinition.getVersion())
                .workflow(WorkflowConfig.from(taskDefinition.getWorkflow()))
                .actorType(taskDefinition.getActorType())
                .applicantType(taskDefinition.getApplicantType())
                .inputResourceConfig(taskDefinition.getInputResourceConfig())
                .inputResourceValueMap(new HashMap<>())
                .executionType(taskDefinition.getExecutionType())
                .automationSupported(taskDefinition.isAutomationSupported())
                .optional(taskDefinition.isOptional())
                .status(TaskDefinition.TaskStatus.NEW)
                .metadata(taskDefinition.getMetadata())
                .order(Optional.ofNullable(taskDefinition.getOrder()).orElse(0))
                .build();
    }

    public static TaskExecution createNewTask(TaskExecution taskExecution) {
        return TaskExecution.builder()
                .taskId(taskExecution.getTaskId())
                .templateId(taskExecution.getTemplateId())
                .entityType(taskExecution.getEntityType())
                .version(taskExecution.getVersion())
                .workflow(WorkflowConfig.from(taskExecution.getWorkflow()))
                .actorType(taskExecution.getActorType())
                .applicantType(taskExecution.getApplicantType())
                .applicantId(taskExecution.getApplicantId())
                .applicationId(taskExecution.getApplicationId())
                .inputResourceConfig(taskExecution.getInputResourceConfig())
                .inputResourceValueMap(new HashMap<>())
                .executionType(taskExecution.getExecutionType())
                .automationSupported(taskExecution.getAutomationSupported())
                .status(TaskDefinition.TaskStatus.NEW)
                .metadata(taskExecution.getMetadata())
                .order(Optional.ofNullable(taskExecution.getOrder()).orElse(0))
                .productType(taskExecution.getProductType())
                .channel(taskExecution.getChannel())
                .funnel(taskExecution.getFunnel())
                .optional(taskExecution.getOptional())
                .templateVersion(taskExecution.getTemplateVersion())
                .build();

    }

    public static TaskExecution createNewTask(TaskExecution taskExecution, String entityIdentifier) {
        return TaskExecution.builder()
                .entityIdentifier(entityIdentifier)
                .taskId(taskExecution.getTaskId())
                .templateId(taskExecution.getTemplateId())
                .entityType(taskExecution.getEntityType())
                .version(taskExecution.getVersion())
                .workflow(WorkflowConfig.from(taskExecution.getWorkflow()))
                .actorType(taskExecution.getActorType())
                .applicantType(taskExecution.getApplicantType())
                .applicantId(taskExecution.getApplicantId())
                .applicationId(taskExecution.getApplicationId())
                .inputResourceConfig(taskExecution.getInputResourceConfig())
                .inputResourceValueMap(new HashMap<>())
                .executionType(taskExecution.getExecutionType())
                .automationSupported(taskExecution.getAutomationSupported())
                .status(TaskDefinition.TaskStatus.NEW)
                .metadata(taskExecution.getMetadata())
                .order(Optional.ofNullable(taskExecution.getOrder()).orElse(0))
                .optional(taskExecution.getOptional())
                .productType(taskExecution.getProductType())
                .channel(taskExecution.getChannel())
                .funnel(taskExecution.getFunnel())
                .templateVersion(taskExecution.getTemplateVersion())
                .build();
    }
}

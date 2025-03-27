package com.bff.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Document(collection = "task_execution_log")
@Builder(toBuilder = true)
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskExecutionLog {

    @Id
    private String _id;

    private String parentId;

    private String taskId;

    private String version;

    private Integer order;

    private String templateId;

    private String templateVersion;

    private String funnel;

    private String channel;

    private String productType;

    private WorkflowConfig workflow;

    @Indexed
    private String applicationId;

    private String entityIdentifier;

    private TaskDefinition.EntityType entityType;

    private Set<String> roles;

    private Set<String> actorType;

    private String actorId;

    private com.bff.demo.model.ApplicantType applicantType;

    private String applicantId;

    private Boolean required;

    private TaskDefinition.TaskStatus status;

    private String statusReason;

    private TaskDefinition.TaskExecutionType executionType;

    private Map<String, Object> metadata;

    private SendbackMetadata sendbackMetadata;

    private LocalDateTime skippedAt;

    private com.bff.demo.model.SkippedReason skippedReason;

    private Map<String, String> inputResourceConfig;

    private Map<String, String> inputResourceValueMap;

    private Map<String, Object> taskRuleContext;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    private String taskInput;

    private String requestId;

    @LastModifiedBy
    private String handledBy;

    public static TaskExecutionLog from(TaskExecution taskExecution) {
        return TaskExecutionLog.builder()
                .parentId(taskExecution.get_id())
                .templateId(taskExecution.getTemplateId())
                .templateVersion(taskExecution.getTemplateVersion())
                .required(taskExecution.getRequired())
                .taskId(taskExecution.getTaskId())
                .order(taskExecution.getOrder())
                .channel(taskExecution.getChannel())
                .funnel(taskExecution.getFunnel())
                .productType(taskExecution.getProductType())
                .applicationId(taskExecution.getApplicationId())
                .entityType(taskExecution.getEntityType())
                .entityIdentifier(taskExecution.getEntityIdentifier())
                .version(taskExecution.getVersion())
                .workflow(WorkflowConfig.from(taskExecution.getWorkflow()))
                .actorId(taskExecution.getActorId())
                .applicantType(taskExecution.getApplicantType())
                .applicantId(taskExecution.getApplicantId())
                .executionType(taskExecution.getExecutionType())
                .status(taskExecution.getStatus())
                .statusReason(taskExecution.getStatusReason())
                .metadata(taskExecution.getMetadata())
                .sendbackMetadata(taskExecution.getSendbackMetadata())
                .inputResourceConfig(taskExecution.getInputResourceConfig())
                .inputResourceValueMap(taskExecution.getInputResourceValueMap())
                .taskRuleContext(taskExecution.getTaskRuleContext())
                .skippedAt(taskExecution.getSkippedAt())
                .skippedReason(taskExecution.getSkippedReason())
                .createdAt(taskExecution.getCreatedAt())
                .updatedAt(taskExecution.getUpdatedAt())
                .completedAt(taskExecution.getCompletedAt())
                .taskInput(taskExecution.getTaskInput())
                .requestId(taskExecution.getRequestId())
                .handledBy(taskExecution.getHandledBy())
                .build();

    }
}

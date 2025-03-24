package com.bff.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDefinition {

    private String name;

    private String version;

    @Builder.Default
    private Integer order = 0;

    private String actorType;

    private EntityType entityType;

    private com.bff.demo.model.ApplicantType applicantType;

    @Builder.Default
    private TaskStatus status = TaskStatus.NEW;

    private WorkflowConfig workflow;

    private TaskExecutionType executionType;

    private boolean automationSupported;

    private String applicationState;

    private Map<String, Object> metadata;

    private Map<String, String> inputResourceConfig;

    private Map<String, Object> adaptorConfig;

    private boolean optional;

    public enum TaskStatus {
        NEW,
        TODO,
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        SKIPPED,
        SEND_BACK,
        CANCELLED,
        SENDBACK,
        SENDBACK_INVALIDATED;

        public static TaskStatus resolve(String val) {
            try {
                return TaskStatus.valueOf(val);
            } catch (Exception e) {
                return null;
            }
        }
    }

    public enum TaskExecutionType {
        MANUAL,
        AUTOMATED
    }

    public enum EntityType {
        LOAN,
        ASSET,
        BANKING,
        CUSTOMER,
        TERMS,
        OFFER,
        APPROVAL_REQUESTS,
        PARTNER
    }

}
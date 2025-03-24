package com.bff.demo.modal;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
public class SendbackMetadata {

	private String key;

	private SendbackStatus status;

	private String remark;

	private String sourceAgentId;

	private LocalDateTime initiatedAt;

	private String initiatedBy;

	private LocalDateTime processedAt;

	private String processedBy;

	private String rejectionReason;

	private Boolean canValidateSourceSendbackTasks;

	private String sourceLoanStage;

	private String sourceSubModule;

	private String targetSubModule;

	private Boolean rejectionAllowed = true;

}

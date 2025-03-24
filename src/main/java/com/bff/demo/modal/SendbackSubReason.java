package com.bff.demo.modal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.Map;

@Data
@ToString
public class SendbackSubReason {
	@NotBlank(message = "Please provide subReason")
	private String subReason;

	@NotBlank(message = "Please provide sendbackKey")
	@Indexed
	private String sendbackKey;

	@NotBlank(message = "Please provide targetFunnel")
	private String targetFunnel;

	@NotBlank(message = "Please provide targetLoanStage")
	private String targetLoanStage;

	@NotEmpty(message = "Please provide taskId")
	@Indexed
	private String targetTaskId;

	private Map<String, Object> metadata;

	private Boolean rejectionAllowed;

}

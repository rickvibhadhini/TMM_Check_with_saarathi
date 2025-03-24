package com.bff.demo.modal;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Document(collection = "sendback_config")
@Data
@ToString
public class SendbackConfig {
	@Id
	private String _id;

	@Indexed
	@NotBlank(message = "Please provide sourceFunnel")
	private String sourceFunnel;

	@Indexed
	@NotBlank(message = "Please provide sourceSubModule")
	private String sourceSubModule;

	@Indexed
	@NotBlank(message = "Please provide reason")
	private String reason;

	private String category;

	@Valid
	@NotEmpty(message = "The list cannot be empty")
	private List<SendbackSubReason> subReasonList;

	@CreatedDate
	private LocalDateTime createdAt = LocalDateTime.now();

	@LastModifiedDate
	private LocalDateTime updatedAt = LocalDateTime.now();

	private String version = "1";
}
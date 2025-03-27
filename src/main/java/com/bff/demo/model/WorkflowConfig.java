package com.bff.demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowConfig {

    private String identifier;

    private String version;

    public static WorkflowConfig from(WorkflowConfig config) {
        if(Objects.isNull(config))
            return null;
        return WorkflowConfig.builder()
                .identifier(config.getIdentifier())
                .version(config.getVersion())
                .build();
    }
}

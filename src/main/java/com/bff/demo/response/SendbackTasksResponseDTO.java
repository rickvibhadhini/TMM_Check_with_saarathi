package com.bff.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendbackTasksResponseDTO {
    private Map<String, Map<String, Map<String, TaskResponse>>> sendbackTasks;
}


package com.bff.demo.applicationActivityLog.applicationActivityLog.impl;


import com.bff.demo.applicationActivityLog.ApplicationActivityLogService;
import com.bff.demo.applicationActivityLog.applicationActivityLog.ApplicationActivityLogApi;
import com.bff.demo.response.applicationActivityLog.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApplicationActivityLogApiImpl implements ApplicationActivityLogApi {

    private final ApplicationActivityLogService applicationService;

    @Override
    public APIResponse<Map<String, Object>> getTasksByApplicationId(String applicationId) {
        log.info("Fetching tasks for applicationId: {}", applicationId);
        Map<String, Object> tasksByApplicationId = applicationService.getTasksGroupedByFunnel(applicationId);
        return APIResponse.ok(tasksByApplicationId);
    }
}

package com.bff.demo.applicationActivityLogApi.impl;


import com.bff.demo.applicationActivityLogApi.ApplicationActivityLogApi;
import com.bff.demo.applicationActivityLogService.ApplicationActivityLogService;
import com.bff.demo.response.APIResponse;
import com.bff.demo.response.TasksGroupedByFunnelDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApplicationActivityLogApiImpl implements ApplicationActivityLogApi {

    private final ApplicationActivityLogService applicationService;

    @Override
    public APIResponse<TasksGroupedByFunnelDTO> getTasksByApplicationId(String applicationId) {
        log.info("Fetching tasks for applicationId: {}", applicationId);
        TasksGroupedByFunnelDTO
                tasksByApplicationId = applicationService.getTasksGroupedByFunnel(applicationId);
        return APIResponse.ok(tasksByApplicationId);
    }
}

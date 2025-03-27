package com.bff.demo.applicationActivityLogApi;

import com.bff.demo.response.APIResponse;
import com.bff.demo.response.TasksGroupedByFunnelDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/api/v1/applicationLog")
public interface ApplicationActivityLogApi {

    @GetMapping("/{applicationId}")
    APIResponse<TasksGroupedByFunnelDTO> getTasksByApplicationId(@PathVariable String applicationId);

}

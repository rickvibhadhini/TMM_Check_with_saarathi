package com.bff.demo.applicationActivityLog.applicationActivityLog;


import com.bff.demo.response.applicationActivityLog.APIResponse;
import com.bff.demo.response.applicationActivityLog.TasksGroupedByFunnelDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@RequestMapping("/api/v1/applicationLog")
//@CrossOrigin("http://localhost:5173")
public interface ApplicationActivityLogApi {

    @GetMapping("/{applicationId}")
    APIResponse<TasksGroupedByFunnelDTO> getTasksByApplicationId(@PathVariable String applicationId);

}

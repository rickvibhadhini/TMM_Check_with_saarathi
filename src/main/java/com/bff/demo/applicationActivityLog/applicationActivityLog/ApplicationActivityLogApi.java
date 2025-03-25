package com.bff.demo.applicationActivityLog.applicationActivityLog;


import com.bff.demo.response.applicationActivityLog.APIResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@RequestMapping("/api/v1/applicationLog")
public interface ApplicationActivityLogApi {

    @GetMapping("/{applicationId}")
    APIResponse<Map<String, Object>> getTasksByApplicationId(@PathVariable String applicationId);

}

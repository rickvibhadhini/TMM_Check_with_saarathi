package com.bff.demo.applicationActivityLog;


import com.bff.demo.response.applicationActivityLog.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/applicationLog")
@CrossOrigin(origins = "http://localhost:5173")
public class ApplicationActivityLogApi {

    @Autowired
    ApplicationActivityLogService applicationService;


    @GetMapping("/{applicationId}")
    public ResponseEntity<ApiResponse> getTasksByApplicationId(@PathVariable String applicationId) {
//        log.info("Fetching tasks for applicationId: {}", applicationId);
        Map<String, Object> tasksByApplicationId = applicationService.getTasksGroupedByFunnel(applicationId);
        ApiResponse response = new ApiResponse();
        response.setStatusCode(HttpStatus.OK.value());
        response.setSuccess(true);
        response.setMessage("Tasks retrieved successfully");
        response.setService("APPUSER" + HttpStatus.OK.value());
        response.setData(tasksByApplicationId);
        return ResponseEntity.ok().body(response);
    }
}

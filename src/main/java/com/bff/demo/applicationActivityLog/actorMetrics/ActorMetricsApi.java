package com.bff.demo.applicationActivityLog.actorMetrics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/actorMetrics")
public interface ActorMetricsApi {

    @GetMapping(path = "{actorId}/{days}")
    public ResponseEntity getActorPerformance(@PathVariable String actorId, @PathVariable int days);

    @GetMapping(path = "system/{funnel}/{days}")
    public ResponseEntity getSystemPerformance(@PathVariable String funnel, @PathVariable int days);
}

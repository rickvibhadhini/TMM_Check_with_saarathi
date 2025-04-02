package com.bff.demo.applicationActivityLog.actorMetrics.impl;

import com.bff.demo.applicationActivityLog.ActorServiceImpl;
import com.bff.demo.applicationActivityLog.actorMetrics.ActorMetricsApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ActorMetricsApiImpl implements ActorMetricsApi {

    private final ActorServiceImpl actorService;

    @Override
    public ResponseEntity getActorPerformance(@PathVariable String actorId, @PathVariable int days){
        log.info("ActorController [getActorPerformance] {} {}", actorId, days);
        return ResponseEntity.ok().body(actorService.getActorMetrics(actorId, days));
    }

    @Override
    public ResponseEntity getSystemPerformance(@PathVariable String funnel, @PathVariable int days){
        log.info("ActorController [getSystemPerformance] {} {}", funnel, days);
        return ResponseEntity.ok().body(actorService.getSystemMetrics(funnel, days));
    }
}

package com.bff.demo.applicationActivityLogService;

import com.bff.demo.model.applicationActivityLogModel.SubTaskEntity;
import com.bff.demo.model.applicationActivityLogModel.TaskExecutionTimeEntity;
import com.bff.demo.repository.applicationActivityLogRepository.TaskExecutionTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static com.bff.demo.constants.BffConstant.ActivityLogConstants.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaskExecutionTimeService {

    private final TaskExecutionTimeRepository taskExecutionTimeRepository;

    public void updateTaskExecutionTime(
            String taskId, String status, Instant createdAt, Instant updatedAt,
            String funnel, String applicationId, String entityId, String channel
    ) {
        try {
            log.info("Updating task execution time for taskId: {}, funnel: {}, status: {}, channel: {}",
                    taskId, funnel, status, channel);

            TaskExecutionTimeEntity taskTimeEntity = taskExecutionTimeRepository
                    .findByApplicationIdAndEntityId(applicationId, entityId)
                    .orElseGet(() -> {
                        log.info("No existing TaskExecutionTimeEntity found for applicationId: {}, entityId: {}. Creating new entity.",
                                applicationId, entityId);
                        TaskExecutionTimeEntity newEntity = new TaskExecutionTimeEntity();
                        newEntity.setApplicationId(applicationId);
                        newEntity.setEntityId(entityId);
                        newEntity.setChannel(channel); // Set channel for new entity
                        return taskExecutionTimeRepository.save(newEntity); // Save new entity immediately
                    });


            taskTimeEntity.setChannel(channel);

            List<SubTaskEntity> subTaskEntityList;
            switch (funnel.toLowerCase()) {
                case SOURCING:
                    if (taskTimeEntity.getSourcing() == null) taskTimeEntity.setSourcing(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getSourcing();
                    break;
                case CREDIT:
                    if (taskTimeEntity.getCredit() == null) taskTimeEntity.setCredit(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getCredit();
                    break;
                case CONVERSION:
                    if (taskTimeEntity.getConversion() == null) taskTimeEntity.setConversion(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getConversion();
                    break;
                case FULFILLMENT:
                    if (taskTimeEntity.getFulfillment() == null) taskTimeEntity.setFulfillment(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getFulfillment();
                    break;
                case RISK:
                    if (taskTimeEntity.getRisk() == null) taskTimeEntity.setRisk(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getRisk();
                    break;

                case RTO:
                    if (taskTimeEntity.getRto() == null) taskTimeEntity.setRto(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getRto();
                    break;

                case DISBURSAL:
                    if (taskTimeEntity.getDisbursal() == null) taskTimeEntity.setDisbursal(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getDisbursal();
                    break;

                default:
                    log.warn("Unknown funnel type: {}. Task execution time update skipped.", funnel);
                    return;
            }


            SubTaskEntity subTaskEntity = subTaskEntityList.stream()
                    .filter(t -> t.getTaskId().equals(taskId))
                    .findFirst()
                    .orElseGet(() -> {
                        log.info("Creating new subtask for taskId: {}", taskId);
                        SubTaskEntity newSubTaskEntity = new SubTaskEntity(taskId, createdAt);
                        subTaskEntityList.add(newSubTaskEntity);
                        return newSubTaskEntity;
                    });


            subTaskEntity.updateStatus(status, updatedAt);
            taskTimeEntity.setRecordDate(updatedAt);


            taskExecutionTimeRepository.save(taskTimeEntity);

            log.info("Task execution time updated successfully for applicationId={}, entityId={}, taskId={}, channel={}",
                    applicationId, entityId, taskId, channel);

        } catch (Exception e) {

            log.error("Error updating task execution time for taskId: {}, funnel: {}, status: {}, channel: {}",
                    taskId, funnel, status, channel, e);
            throw new RuntimeException("Failed to retrieve task execution with ID: " +  e);
        }
    }

}
package com.bff.demo.applicationActivityLog;



import com.bff.demo.modal.applicationActivityLogModel.SubTaskEntity;
import com.bff.demo.modal.applicationActivityLogModel.TaskExecutionTimeEntity;
import com.bff.demo.repository.applicationActivityLogRepository.TaskExecutionTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskExecutionServiceImpl{




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

            // Ensure the channel is updated in existing entities
            taskTimeEntity.setChannel(channel);

            // Select the correct list based on the funnel type
            List<SubTaskEntity> subTaskEntityList;
            switch (funnel.toLowerCase()) {
                case "sourcing":
                    if (taskTimeEntity.getSourcing() == null) taskTimeEntity.setSourcing(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getSourcing();
                    break;
                case "credit":
                    if (taskTimeEntity.getCredit() == null) taskTimeEntity.setCredit(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getCredit();
                    break;
                case "conversion":
                    if (taskTimeEntity.getConversion() == null) taskTimeEntity.setConversion(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getConversion();
                    break;
                case "fulfillment":
                    if (taskTimeEntity.getFulfillment() == null) taskTimeEntity.setFulfillment(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getFulfillment();
                    break;
                case "risk":
                    if (taskTimeEntity.getRisk() == null) taskTimeEntity.setRisk(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getRisk();
                    break;

                case "rto":
                    if (taskTimeEntity.getRto() == null) taskTimeEntity.setRto(new ArrayList<>());
                    subTaskEntityList = taskTimeEntity.getRto();
                    break;

                case "disbursal":
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
        }
    }

}
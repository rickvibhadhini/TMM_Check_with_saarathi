package com.bff.demo.applicationActivityLog;


import com.bff.demo.applicationActivityLog.changeStreams.Constants;
import com.bff.demo.model.SendbackConfig;
import com.bff.demo.model.SendbackMetadata;
import com.bff.demo.model.TaskExecutionLog;
import com.bff.demo.model.applicationActivityLogModel.SubTaskEntity;
import com.bff.demo.model.applicationActivityLogModel.TaskExecutionTimeEntity;
import com.bff.demo.repository.SendbackConfigRepository;
import com.bff.demo.repository.TaskExecutionLogRepository;
import com.bff.demo.repository.applicationActivityLogRepository.TaskExecutionTimeRepository;
import com.bff.demo.response.applicationActivityLog.StatusLogResponse;
import com.bff.demo.response.applicationActivityLog.TaskDetailsResponse;
import com.bff.demo.response.applicationActivityLog.TaskResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationActivityLogService {

    private final TaskExecutionLogRepository taskExecutionLogRepository;
    private final TaskExecutionTimeRepository taskExecutionTimeRepository;
    private final SendbackConfigRepository sendbackConfigRepository;

    public Map<String, Object> getTasksGroupedByFunnel(String applicationId) {
        log.info("[getTasksGroupedByFunnel] Fetching list view tasks for applicationId: {}", applicationId);

        List<TaskExecutionLog> tasks = taskExecutionLogRepository.findByApplicationId(applicationId);
        Optional<TaskExecutionTimeEntity> taskExecutionTimeOpt = taskExecutionTimeRepository.findByApplicationId(applicationId);
        TaskExecutionTimeEntity taskExecutionTimeEntity = taskExecutionTimeOpt.orElse(null);

        log.info("[getTasksGroupedByFunnel] Retrieved {} tasks for applicationId: {}", tasks.size(), applicationId);

        List<TaskExecutionLog> sendbackTasks = filterSendbackTasks(tasks);
        List<TaskExecutionLog> regularTasks = filterRegularTasks(tasks);

        Map<String, SubTaskEntity> taskMetadata = getAllSubTaskEntities(taskExecutionTimeEntity);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(Constants.ResponseKeys.TASKS_GROUPED_BY_FUNNEL, buildFunnelTasksResponse(regularTasks, taskMetadata));
        response.put(Constants.ResponseKeys.SENDBACK_TASKS, buildSendbackTasksResponse(sendbackTasks, taskMetadata));
        response.put(Constants.ResponseKeys.LATEST_TASK_STATE, buildLatestTaskStateResponse(tasks, taskMetadata));

        return response;
    }

    private List<TaskExecutionLog> filterSendbackTasks(List<TaskExecutionLog> tasks) {
        List<TaskExecutionLog> sendbackTasks = tasks.stream()
                .filter(task -> Constants.TaskTypes.SENDBACK.equalsIgnoreCase(task.getTaskId()))
                .toList();
        log.info("[filterSendbackTasks] Found {} sendback tasks", sendbackTasks.size());
        return sendbackTasks;
    }

    private List<TaskExecutionLog> filterRegularTasks(List<TaskExecutionLog> tasks) {
        List<TaskExecutionLog> regularTasks = tasks.stream()
                .filter(task -> !Constants.TaskTypes.SENDBACK.equalsIgnoreCase(task.getTaskId()))
                .toList();
        log.info("[filterRegularTasks] Found {} regular tasks", regularTasks.size());
        return regularTasks;
    }

    private Map<String, SubTaskEntity> getAllSubTaskEntities(TaskExecutionTimeEntity entity) {
        if (entity == null) {
            return Collections.emptyMap();
        }

        return Stream.of(
                        entity.getSourcing(),
                        entity.getCredit(),
                        entity.getRisk(),
                        entity.getConversion(),
                        entity.getRto(),
                        entity.getFulfillment(),
                        entity.getDisbursal()
                )
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(
                        SubTaskEntity::getTaskId,
                        task -> task,
                        (TaskId1, TaskId2) -> TaskId1
                ));
    }

    private LinkedHashMap<String, Object> buildFunnelTasksResponse(List<TaskExecutionLog> regularTasks,
                                                                   Map<String, SubTaskEntity> taskMetadata) {
        Map<String, Integer> funnelMinOrders = calculateFunnelMinOrders(regularTasks);
        Map<String, Map<String, List<TaskExecutionLog>>> tasksByFunnelAndId = groupTasksByFunnelAndId(regularTasks);
        List<String> sortedFunnels = sortFunnelsByOrder(funnelMinOrders);

        LinkedHashMap<String, Object> tasksGroupedByFunnel = new LinkedHashMap<>();

        for (String funnel : sortedFunnels) {
            Map<String, Object> funnelData = buildFunnelData(funnel, tasksByFunnelAndId, taskMetadata);
            tasksGroupedByFunnel.put(funnel, funnelData);
        }

        return tasksGroupedByFunnel;
    }

    private Map<String, Integer> calculateFunnelMinOrders(List<TaskExecutionLog> regularTasks) {
        Map<String, Integer> funnelMinOrders = regularTasks.stream()
                .collect(Collectors.groupingBy(
                        task -> Optional.ofNullable(task.getFunnel()).orElse(Constants.Funnel.UNKNOWN),
                        Collectors.mapping(TaskExecutionLog::getOrder, Collectors.minBy(Integer::compare))
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().orElse(Integer.MAX_VALUE)
                ));
        log.info("[calculateFunnelMinOrders] Identified {} unique funnels", funnelMinOrders.size());
        return funnelMinOrders;
    }

    private Map<String, Map<String, List<TaskExecutionLog>>> groupTasksByFunnelAndId(List<TaskExecutionLog> regularTasks) {
        return regularTasks.stream()
                .collect(Collectors.groupingBy(
                        task -> Optional.ofNullable(task.getFunnel()).orElse(Constants.Funnel.UNKNOWN),
                        Collectors.groupingBy(task -> Optional.ofNullable(task.getTaskId()).orElse(Constants.TaskTypes.UNKNOWN))
                ));
    }

    private List<String> sortFunnelsByOrder(Map<String, Integer> funnelMinOrders) {
        return funnelMinOrders.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();
    }

    private Map<String, Object> buildFunnelData(String funnel,
                                                Map<String, Map<String, List<TaskExecutionLog>>> tasksByFunnelAndId,
                                                Map<String, SubTaskEntity> taskMetadata) {
        List<TaskResponse> funnelTasks = tasksByFunnelAndId.getOrDefault(funnel, Collections.emptyMap())
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(list -> list.get(0).getOrder())))
                .map(entry -> createTaskResponse(entry.getValue(), taskMetadata, false))
                .collect(Collectors.toList());

        long totalDuration = funnelTasks.stream().mapToLong(TaskResponse::getDuration).sum();
        log.info("[buildFunnelData] Calculated total duration {} for funnel {}", totalDuration, funnel);

        Map<String, Object> funnelData = new LinkedHashMap<>();
        funnelData.put(Constants.FunnelDataKeys.FUNNEL, funnel);
        funnelData.put(Constants.FunnelDataKeys.FUNNEL_DURATION, totalDuration);
        funnelData.put(Constants.FunnelDataKeys.TASKS, funnelTasks);

        return funnelData;
    }

    private Map<String, Map<String, Map<String, TaskResponse>>> buildSendbackTasksResponse(
            List<TaskExecutionLog> sendbackTasks, Map<String, SubTaskEntity> taskMetadata) {
        return sendbackTasks.stream()
                .sorted(Comparator.comparing(TaskExecutionLog::getUpdatedAt))
                .collect(Collectors.groupingBy(
                        task -> Optional.ofNullable(task.getSendbackMetadata())
                                .map(SendbackMetadata::getKey)
                                .orElse(Constants.Funnel.UNKNOWN_KEY),
                        Collectors.groupingBy(
                                task -> {
                                    SendbackMetadata metadata = task.getSendbackMetadata();
                                    return metadata != null ? metadata.getSourceLoanStage() + "_" + metadata.getSourceSubModule()
                                            : Constants.Funnel.UNKNOWN_STAGE_MODULE;
                                },
                                Collectors.groupingBy(
                                        this::fetchTargetTaskId,
                                        Collectors.collectingAndThen(
                                                Collectors.toList(),
                                                logs -> createTaskResponse(logs, taskMetadata, true)
                                        )
                                )
                        )
                ));
    }

    private Map<String, Object> buildLatestTaskStateResponse(List<TaskExecutionLog> tasks,
                                                             Map<String, SubTaskEntity> taskMetadata) {
        TaskExecutionLog latestLog = tasks.stream()
                .max(Comparator.comparing(TaskExecutionLog::getUpdatedAt))
                .orElse(null);

        if (latestLog != null) {
            SubTaskEntity metadata = taskMetadata.getOrDefault(latestLog.getTaskId(),
                    new SubTaskEntity(latestLog.getTaskId(), null));

            Map<String, Object> latestTaskState = new HashMap<>();
            latestTaskState.put(Constants.TaskStateKeys.TASK_ID,
                    Optional.ofNullable(latestLog.getTaskId()).orElse(Constants.TaskTypes.UNKNOWN));
            latestTaskState.put(Constants.TaskStateKeys.ORDER, latestLog.getOrder());
            latestTaskState.put(Constants.TaskStateKeys.HANDLED_BY, latestLog.getHandledBy());
            latestTaskState.put(Constants.TaskStateKeys.CREATED_AT, latestLog.getCreatedAt());
            latestTaskState.put(Constants.TaskStateKeys.STATUS, latestLog.getStatus());
            latestTaskState.put(Constants.TaskStateKeys.UPDATED_AT, latestLog.getUpdatedAt());
            latestTaskState.put(Constants.TaskStateKeys.DURATION, metadata.getDuration());
            latestTaskState.put(Constants.TaskStateKeys.SENDBACKS, metadata.getSendbacks());
            latestTaskState.put(Constants.TaskStateKeys.VISITED, metadata.getVisited());

            log.info("[buildLatestTaskStateResponse] Latest task state recorded for taskId {}", latestLog.getTaskId());
            return latestTaskState;
        } else {
            return null;
        }
    }

    private TaskResponse createTaskResponse(List<TaskExecutionLog> logs,
                                            Map<String, SubTaskEntity> taskMetadata,
                                            boolean isSendback) {
        TaskExecutionLog firstLog = logs.get(0);
        log.info("[createTaskResponse] Creating response for taskId: {} isSendback: {}", firstLog.getTaskId(), isSendback);

        List<StatusLogResponse> statusLogs = logs.stream()
                .sorted(Comparator.comparing(TaskExecutionLog::getUpdatedAt))
                .map(applicationLog -> new StatusLogResponse(applicationLog.getStatus(), applicationLog.getUpdatedAt()))
                .collect(Collectors.toList());

        SubTaskEntity metadata = taskMetadata.getOrDefault(firstLog.getTaskId(), null);
        long duration = metadata != null ? metadata.getDuration() : 0;
        int sendbacks = metadata != null ? metadata.getSendbacks() : 0;
        int visited = metadata != null ? metadata.getVisited() : 0;

        String targetTaskId = isSendback ? fetchTargetTaskId(firstLog) : null;
        String sourceLoanStage = isSendback ? fetchSourceModule(firstLog) : null;
        String sourceSubModule = isSendback ? fetchSubModule(firstLog) : null;

        return new TaskResponse(
                firstLog.getTaskId(),
                firstLog.getOrder(),
                firstLog.getHandledBy(),
                firstLog.getCreatedAt(),
                statusLogs,
                targetTaskId,
                duration,
                sendbacks,
                visited,
                sourceLoanStage,
                sourceSubModule
        );
    }

    private String fetchTargetTaskId(TaskExecutionLog applicationLog) {
        log.info("[fetchTargetTaskId] Fetching target task ID for log: {}", applicationLog.getTaskId());

        SendbackMetadata sendbackMetadata = applicationLog.getSendbackMetadata();
        if (sendbackMetadata != null && sendbackMetadata.getKey() != null) {
            String sendbackKey = sendbackMetadata.getKey();
            SendbackConfig config = sendbackConfigRepository.findBySendbackKey(sendbackKey);

            if (config != null && !config.getSubReasonList().isEmpty()) {
                return config.getSubReasonList().get(0).getTargetTaskId();
            }
        }
        return null;
    }

    private String fetchSourceModule(TaskExecutionLog applicationLog) {
        log.info("[fetchSourceModule] Fetching source module for log: {}", applicationLog.getTaskId());

        SendbackMetadata sendbackMetadata = applicationLog.getSendbackMetadata();
        return sendbackMetadata != null ? sendbackMetadata.getSourceLoanStage() : null;
    }

    private String fetchSubModule(TaskExecutionLog applicationLog) {
        log.info("[fetchSubModule] Fetching sub module for log: {}", applicationLog.getTaskId());

        SendbackMetadata sendbackMetadata = applicationLog.getSendbackMetadata();
        return sendbackMetadata != null ? sendbackMetadata.getSourceSubModule() : null;
    }

    private TaskDetailsResponse convertToTaskDetails(TaskExecutionLog applicationLog) {
        log.info("[convertToTaskDetails] Converting task details for taskId: {}", applicationLog.getTaskId());
        String targetTaskId = Constants.TaskTypes.SENDBACK.equalsIgnoreCase(applicationLog.getTaskId())
                ? fetchTargetTaskId(applicationLog) : null;

        return new TaskDetailsResponse(
                Optional.ofNullable(applicationLog.getFunnel()).orElse(Constants.Funnel.UNKNOWN),
                applicationLog.getActorId(),
                applicationLog.getStatus(),
                applicationLog.getUpdatedAt(),
                applicationLog.getTaskId(),
                targetTaskId,
                0,
                0,
                applicationLog.getMetadata()
        );
    }
}
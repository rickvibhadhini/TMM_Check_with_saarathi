package com.bff.demo.applicationActivityLog;

import com.bff.demo.model.SendbackConfig;
import com.bff.demo.model.SendbackMetadata;
import com.bff.demo.model.TaskExecutionLog;
import com.bff.demo.model.applicationActivityLogModel.SubTaskEntity;
import com.bff.demo.model.applicationActivityLogModel.TaskExecutionTimeEntity;
import com.bff.demo.repository.SendbackConfigRepository;
import com.bff.demo.repository.TaskExecutionLogRepository;
import com.bff.demo.repository.applicationActivityLogRepository.TaskExecutionTimeRepository;
import com.bff.demo.response.applicationActivityLog.*;
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

    private static final String UNKNOWN_FUNNEL = "Unknown Funnel";
    private final TaskExecutionLogRepository taskExecutionLogRepository;
    private final TaskExecutionTimeRepository taskExecutionTimeRepository;
    private final SendbackConfigRepository sendbackConfigRepository;

    public TasksGroupedByFunnelDTO getTasksGroupedByFunnel(String applicationId) {
        log.info("[getTasksGroupedByFunnel] Fetching list view tasks for applicationId: {}", applicationId);

        // Fetch data
        List<TaskExecutionLog> tasks = taskExecutionLogRepository.findByApplicationId(applicationId);
        Optional<TaskExecutionTimeEntity> taskExecutionTimeOpt = taskExecutionTimeRepository.findByApplicationId(applicationId);
        TaskExecutionTimeEntity taskExecutionTimeEntity = taskExecutionTimeOpt.orElse(null);

        log.info("[getTasksGroupedByFunnel] Retrieved {} tasks for applicationId: {}", tasks.size(), applicationId);

        // Split tasks
        List<TaskExecutionLog> sendbackTasks = filterSendbackTasks(tasks);
        List<TaskExecutionLog> regularTasks = filterRegularTasks(tasks);

        // Get task metadata
        Map<String, SubTaskEntity> taskMetadata = getAllSubTaskEntities(taskExecutionTimeEntity);

        // Build response using DTO
        return TasksGroupedByFunnelDTO.builder()
                .tasksGroupedByFunnel(buildFunnelTasksResponse(regularTasks, taskMetadata))
                .sendbackTasks(buildSendbackTasksResponse(sendbackTasks, taskMetadata))
                .latestTaskState(buildLatestTaskStateResponse(tasks, taskMetadata))
                .build();
    }

    private List<TaskExecutionLog> filterSendbackTasks(List<TaskExecutionLog> tasks) {
        List<TaskExecutionLog> sendbackTasks = tasks.stream()
                .filter(task -> "sendback".equalsIgnoreCase(task.getTaskId()))
                .toList();
        log.info("[filterSendbackTasks] Found {} sendback tasks", sendbackTasks.size());
        return sendbackTasks;
    }

    private List<TaskExecutionLog> filterRegularTasks(List<TaskExecutionLog> tasks) {
        List<TaskExecutionLog> regularTasks = tasks.stream()
                .filter(task -> !"sendback".equalsIgnoreCase(task.getTaskId()))
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
                        (a, b) -> a // Handle duplicate taskIds by keeping the first occurrence
                ));
    }

    private LinkedHashMap<String, FunnelDataDTO> buildFunnelTasksResponse(List<TaskExecutionLog> regularTasks,
                                                                          Map<String, SubTaskEntity> taskMetadata) {
        // Get funnel order information
        Map<String, Integer> funnelMinOrders = calculateFunnelMinOrders(regularTasks);

        // Group tasks by funnel and task ID
        Map<String, Map<String, List<TaskExecutionLog>>> tasksByFunnelAndId = groupTasksByFunnelAndId(regularTasks);

        // Sort funnels by order
        List<String> sortedFunnels = sortFunnelsByOrder(funnelMinOrders);

        // Build response
        LinkedHashMap<String, FunnelDataDTO> tasksGroupedByFunnel = new LinkedHashMap<>();

        for (String funnel : sortedFunnels) {
            tasksGroupedByFunnel.put(funnel, buildFunnelData(funnel, tasksByFunnelAndId, taskMetadata));
        }

        return tasksGroupedByFunnel;
    }

    private Map<String, Integer> calculateFunnelMinOrders(List<TaskExecutionLog> regularTasks) {
        Map<String, Integer> funnelMinOrders = regularTasks.stream()
                .collect(Collectors.groupingBy(
                        task -> Optional.ofNullable(task.getFunnel()).orElse(UNKNOWN_FUNNEL),
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
                        task -> Optional.ofNullable(task.getFunnel()).orElse(UNKNOWN_FUNNEL),
                        Collectors.groupingBy(task -> Optional.ofNullable(task.getTaskId()).orElse("UNKNOWN_TASK"))
                ));
    }

    private List<String> sortFunnelsByOrder(Map<String, Integer> funnelMinOrders) {
        return funnelMinOrders.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();
    }

    private FunnelDataDTO buildFunnelData(String funnel,
                                          Map<String, Map<String, List<TaskExecutionLog>>> tasksByFunnelAndId,
                                          Map<String, SubTaskEntity> taskMetadata) {
        List<TaskResponse> funnelTasks = tasksByFunnelAndId.getOrDefault(funnel, Collections.emptyMap())
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparing(list -> list.get(0).getOrder())))
                .map(entry -> createTaskResponse(entry.getValue(), taskMetadata, false))
                .collect(Collectors.toList());

        long totalDuration = funnelTasks.stream().mapToLong(TaskResponse::getDuration).sum();
        log.info("[buildFunnelData] Calculated total duration {} for funnel {}", totalDuration, funnel);

        return FunnelDataDTO.builder()
                .funnel(funnel)
                .funnelDuration(totalDuration)
                .tasks(funnelTasks)
                .build();
    }

    private Map<String, Map<String, Map<String, TaskResponse>>> buildSendbackTasksResponse(
            List<TaskExecutionLog> sendbackTasks, Map<String, SubTaskEntity> taskMetadata) {
        return sendbackTasks.stream()
                .sorted(Comparator.comparing(TaskExecutionLog::getUpdatedAt))
                .collect(Collectors.groupingBy(
                        task -> Optional.ofNullable(task.getSendbackMetadata())
                                .map(SendbackMetadata::getKey)
                                .orElse("UNKNOWN_KEY"),
                        Collectors.groupingBy(
                                task -> {
                                    SendbackMetadata metadata = task.getSendbackMetadata();
                                    return metadata != null ? metadata.getSourceLoanStage() + "_" + metadata.getSourceSubModule() : "UNKNOWN_STAGE_MODULE";
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

    private LatestTaskStateDTO buildLatestTaskStateResponse(List<TaskExecutionLog> tasks,
                                                            Map<String, SubTaskEntity> taskMetadata) {
        TaskExecutionLog latestLog = tasks.stream()
                .max(Comparator.comparing(TaskExecutionLog::getUpdatedAt))
                .orElse(null);

        if (latestLog != null) {
            SubTaskEntity metadata = taskMetadata.getOrDefault(latestLog.getTaskId(),
                    new SubTaskEntity(latestLog.getTaskId(), null));

            log.info("[buildLatestTaskStateResponse] Latest task state recorded for taskId {}", latestLog.getTaskId());

            return LatestTaskStateDTO.builder()
                    .taskId(Optional.ofNullable(latestLog.getTaskId()).orElse("UNKNOWN_TASK"))
                    .order(latestLog.getOrder())
                    .handledBy(latestLog.getHandledBy())
                    .createdAt(latestLog.getCreatedAt())
                    .status(latestLog.getStatus().toString())
                    .updatedAt(latestLog.getUpdatedAt())
                    .duration(metadata.getDuration())
                    .sendbacks(metadata.getSendbacks())
                    .visited(metadata.getVisited())
                    .build();
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

            if (config != null && config.getSubReasonList() != null) {
                // Find the specific subReason that matches the sendbackKey
                return config.getSubReasonList().stream()
                        .filter(subReason -> sendbackKey.equals(subReason.getSendbackKey()))
                        .findFirst()
                        .map(subReason -> subReason.getTargetTaskId())
                        .orElse(null);
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
        String targetTaskId = "sendback".equalsIgnoreCase(applicationLog.getTaskId()) ? fetchTargetTaskId(applicationLog) : null;

        return new TaskDetailsResponse(
                Optional.ofNullable(applicationLog.getFunnel()).orElse(UNKNOWN_FUNNEL),
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
package com.bff.demo.repository;


import com.bff.demo.model.ApplicantType;
import com.bff.demo.model.TaskDefinition;
import com.bff.demo.model.TaskExecution;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskExecutionRepository extends MongoRepository<TaskExecution, String> {

    List<TaskExecution> findByApplicationId(String applicationId);

    List<TaskExecution> findByTaskIdAndApplicationId(String taskId, String applicationId);

    List<TaskExecution> findByTaskIdInAndApplicationId(Set<String> taskIds, String applicationId);

    List<TaskExecution> findByTaskIdInAndApplicationIdAndStatus(Set<String> taskIds, String applicationId, TaskDefinition.TaskStatus status);

    List<TaskExecution> findByTaskIdAndApplicationIdAndEntityIdentifier(String taskId, String applicationId, String entityIdentifier);

    TaskExecution findByTaskIdAndApplicationIdAndStatusIn(String taskId, String applicationId, Set<TaskDefinition.TaskStatus> status);

    boolean existsByTemplateIdAndApplicationId(String templateId, String applicationId);
    
    List<TaskExecution> findByApplicationIdAndStatusIn(String applicationId, Set<TaskDefinition.TaskStatus> taskStatus);

    List<TaskExecution> findByApplicationIdAndStatusInAndActorType(String applicationId, Set<TaskDefinition.TaskStatus> status, String actorType);

    boolean existsByApplicationIdAndFunnelAndStatusIn(String applicationId, String funnel, Set<TaskDefinition.TaskStatus> status);

    boolean existsByApplicationIdAndFunnelAndOptionalAndStatusIn(String applicationId, String funnel, boolean optional, Set<TaskDefinition.TaskStatus> status);

    boolean existsByApplicationIdAndOptionalAndStatusInAndOrderLessThan(String applicationId, Boolean optional, Set<TaskDefinition.TaskStatus> status, Integer order);

    List<TaskExecution> findByApplicationIdAndOptionalAndStatusInAndOrderLessThan(String applicationId, Boolean optional, Set<TaskDefinition.TaskStatus> status, Integer order);

    List<TaskExecution> findByApplicationIdAndActorIdAndStatusIn(String applicationId, String actorId, Set<TaskDefinition.TaskStatus> taskStatus);

    @Query("{ 'applicationId': ?0, 'actorId': ?1, 'status': { $in: ?2 }, " +
            " $or: [ { 'applicantType': ?3 }, { 'applicantType': null } ] }")
    List<TaskExecution> findByApplicationIdAndActorIdAndStatusInAndApplicantTypeOrApplicantTypeIsNull(
            String applicationId,
            String actorId,
            Set<TaskDefinition.TaskStatus> taskStatus,
            ApplicantType applicantType
    );

    List<TaskExecution> findByApplicationIdAndActorIdAndStatusInAndApplicantType(String applicationId, String actorId, Set<TaskDefinition.TaskStatus> taskStatus, ApplicantType applicantType);

    List<TaskExecution> findByApplicationIdAndStatusInAndOrderGreaterThan(String applicationId, Set<TaskDefinition.TaskStatus> taskStatus, int order);

    List<TaskExecution> findByApplicationIdAndStatusInAndFunnel(String applicationId, Set<TaskDefinition.TaskStatus> taskStatus, String funnel);

    @Query(value ="{ 'taskId': ?0, 'applicationId': ?1, 'sendbackMetadata.key': ?2 }")
    Optional<TaskExecution> findByTaskIdAndApplicationIdAndSendbackKey(String taskId, String applicationId, String sendbackKey);

    List<TaskExecution> findByTaskIdAndApplicationIdAndApplicantIdAndApplicantType(String taskId, String applicationId, String applicantId, ApplicantType applicantType);

    boolean existsByTemplateIdAndApplicationIdAndApplicantIdAndApplicantType(String templateId, String applicationId, String applicantId, ApplicantType applicantType);

    List<TaskExecution> findByTaskIdInAndApplicationIdAndApplicantIdAndApplicantType(Set<String> taskIds, String applicationId, String applicantId, ApplicantType applicantType);

    boolean existsByTemplateIdAndApplicationIdAndEntityIdentifier(String templateId, String applicationId, String entityIdentifier);

    boolean existsByApplicationIdAndActorIdAndStatusIn(String applicationId, String actorId, Set<TaskDefinition.TaskStatus> status);

    Optional<TaskExecution> findTopByTaskIdAndApplicationIdOrderByCreatedAtDesc(String taskId, String applicationId);

    @Aggregation(pipeline = {
            "{ '$match': { 'taskId': { '$in': ?0 }, 'applicationId': ?1 } }",
            "{ '$sort': { 'taskId': 1, 'createdAt': -1 } }",
            "{ '$group': { '_id': '$taskId', 'latestTask': { '$first': '$$ROOT' } } }",
            "{ '$replaceRoot': { 'newRoot': '$latestTask' } }"
    })
    List<TaskExecution> findLatestTasksByTaskIdInAndApplicationId(Set<String> taskIds, String applicationId);

    Optional<TaskExecution> findTopByTaskIdAndApplicationIdAndApplicantTypeAndApplicantIdOrderByCreatedAtDesc(String taskId, String applicationId, ApplicantType applicantType, String applicantId);

    Optional<TaskExecution> findTopByTaskIdAndApplicationIdAndEntityIdentifierAndApplicantTypeAndApplicantIdOrderByCreatedAtDesc(String taskId, String applicationId, String entityIdentifier, ApplicantType applicantType, String applicantId);

    List<TaskExecution> findByTaskIdInAndApplicationIdAndStatusIn(Set<String> taskIds, String applicationId, Set<TaskDefinition.TaskStatus> taskStatus);

    List<TaskExecution> findByTaskIdInAndEntityId(List<String> taskIds, String entityId);

    List<TaskExecution> findByApplicationIdAndActorIdAndStatusInAndApplicantTypeAndApplicantId(String applicationId, String actorId, Set<TaskDefinition.TaskStatus> taskStatus, ApplicantType applicantType, String applicantId);

    List<TaskExecution> findByApplicationIdAndFunnel(String appId, String funnel);

    TaskExecution findTopByTaskIdAndEntityIdentifierOrderByCreatedAtDesc(String taskId, String entityIdentifer);
}

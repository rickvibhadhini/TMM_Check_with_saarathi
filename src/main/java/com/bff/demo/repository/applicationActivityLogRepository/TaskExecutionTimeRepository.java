package com.bff.demo.repository.applicationActivityLogRepository;


import com.bff.demo.model.applicationActivityLogModel.TaskExecutionTimeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskExecutionTimeRepository extends MongoRepository<TaskExecutionTimeEntity, String> {
    Optional<TaskExecutionTimeEntity> findByApplicationIdAndEntityId(String applicationId, String entityId);
    List<TaskExecutionTimeEntity> findByChannel(String channel);
    Optional<TaskExecutionTimeEntity> findByApplicationId(String applicationId);

}
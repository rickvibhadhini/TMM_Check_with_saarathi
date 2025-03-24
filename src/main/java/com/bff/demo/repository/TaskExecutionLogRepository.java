package com.bff.demo.repository;


import com.bff.demo.model.TaskExecutionLog;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskExecutionLogRepository extends MongoRepository<TaskExecutionLog, String> {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    default TaskExecutionLog persistIsolated(TaskExecutionLog taskExecutionLog) {
        return save(taskExecutionLog);
    }

    @Query(value = "{ 'applicationId': ?0 }", sort = "{ '_id': -1 }")
    List<TaskExecutionLog> findByApplicationIdOrderByObjectIdDesc(String applicationId);
    @Aggregation(pipeline = {
            "{ $match: { applicationId: ?0 } }",
            "{ $sort: { updatedAt: 1 } }"
    })
    List<TaskExecutionLog> findTasksByApplicationIdSortedByUpdatedAt(String applicationId);
    List<TaskExecutionLog> findByApplicationId(String applicationId);
}




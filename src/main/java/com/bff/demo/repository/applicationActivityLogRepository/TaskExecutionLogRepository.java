package com.bff.demo.repository.applicationActivityLogRepository;//package com.cars24.fintech.bff.repository.applicationActivityLogRepository;
//
//import com.cars24.fintech.bff.model.applicationActivityLogModel.TaskExecutionLogEntity;
//import org.springframework.data.mongodb.repository.Aggregation;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface TaskExecutionLogRepository extends MongoRepository<TaskExecutionLogEntity, String> {
//
//    @Aggregation(pipeline = {
//            "{ $match: { applicationId: ?0 } }",
//            "{ $sort: { updatedAt: 1 } }"
//    })
//    List<TaskExecutionLogEntity> findTasksByApplicationIdSortedByUpdatedAt(String applicationId);
//    List<TaskExecutionLogEntity> findByApplicationId(String applicationId);
//}
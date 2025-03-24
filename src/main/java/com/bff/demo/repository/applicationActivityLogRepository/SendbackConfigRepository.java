package com.bff.demo.repository.applicationActivityLogRepository;//package com.cars24.fintech.bff.repository.applicationActivityLogRepository;
//
//import com.cars24.fintech.bff.model.applicationActivityLogModel.SendbackConfigEntity;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface SendbackConfigRepository extends MongoRepository<SendbackConfigEntity,String> {
//    @Query("{'subReasonList.sendbackKey': ?0}")
//    Optional<SendbackConfigEntity> findBySendbackKey(String sendbackKey);
//}

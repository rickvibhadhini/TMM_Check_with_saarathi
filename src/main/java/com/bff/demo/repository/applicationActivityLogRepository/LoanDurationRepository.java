//package com.bff.demo.repository.applicationActivityLogRepository;
//
//
//import com.bff.demo.modal.applicationActivityLogModel.LoanDurationEntity;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface LoanDurationRepository extends MongoRepository<LoanDurationEntity, String> {
//    Optional<LoanDurationEntity> findByApplicationId(String applicationId);
//}
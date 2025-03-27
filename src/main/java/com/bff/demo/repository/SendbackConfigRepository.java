package com.bff.demo.repository;


import com.bff.demo.model.SendbackConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SendbackConfigRepository extends MongoRepository<SendbackConfig, String> {

	List<SendbackConfig> findBySourceFunnel(String sourceFunnel);

	Optional<SendbackConfig> findByReasonAndSourceFunnel(String reason, String sourceFunnel);

	Optional<SendbackConfig> findByReasonAndSourceSubModuleAndCategory(String reason, String sourceSubModule, String category);

	@Query(
			value = "{ 'subReasonList.sendbackKey': ?0 }"
	)
	SendbackConfig findBySendbackKey(String sendbackKey);



}
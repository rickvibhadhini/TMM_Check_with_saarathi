package com.bff.demo.repository.actorMetricsRepository;

import com.bff.demo.model.actorMetricsModel.ActorEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ActorRepository extends MongoRepository<ActorEntity, String> {

    @Query("{'actorId': ?0, 'lastUpdatedAt': { $gte: ?1 }}")
    List<ActorEntity> findAllByActorIdAndLastUpdatedAtAfter(String actorId, Date lastUpdatedAt);

    @Query("{ 'actorType': ?0, 'lastUpdatedAt': { $gte: ?1 } }")
    List<ActorEntity> findAllByLastUpdatedAtAfter(String actorType, Date lastUpdatedAt);

    @Query("{'funnel': ?0, 'lastUpdatedAt': { $gte: ?1 }}")
    List<ActorEntity> findAllByFunnelAndLastUpdatedAtAfter(String funnel, Date lastUpdatedAt);

}

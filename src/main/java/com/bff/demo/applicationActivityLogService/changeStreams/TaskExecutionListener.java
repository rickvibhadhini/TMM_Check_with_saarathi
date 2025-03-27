package com.bff.demo.applicationActivityLogService.changeStreams;

import com.bff.demo.applicationActivityLogService.TaskExecutionTimeService;
import com.bff.demo.constants.BffConstant;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
@Component
public class TaskExecutionListener {

    private static final Logger logger = LoggerFactory.getLogger(TaskExecutionListener.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TaskExecutionTimeService timeService;

    @Autowired
    private RedissonClient redissonClient;

    private ExecutorService executorService;

    @PostConstruct
    public void startChangeStream() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                mongoTemplate.getCollection("task_execution")
                        .watch()
                        .fullDocument(FullDocument.UPDATE_LOOKUP)  // Ensure full document is returned on updates
                        .forEach(this::processChangeStreamDocument);
            } catch (Exception e) {
                log.error("Error in change stream processing", e);
            }
        });
    }

    private void processChangeStreamDocument(ChangeStreamDocument<Document> changeStreamDocument) {

        Document fullDocument = changeStreamDocument.getFullDocument();
        if (fullDocument == null) {
            log.info("TaskExecutionListener [processChangeStreamDocument] Received null document in change stream event");
            return;
        }

        String lockKey = "lock:task:" + fullDocument.getObjectId("_id").toHexString();
        RLock lock = redissonClient.getLock(lockKey);

        boolean isLocked = false;

        try {
            isLocked = lock.tryLock(BffConstant.RedissonLockConstants.REDISSON_CLIENT_WAIT_TIME, BffConstant.RedissonLockConstants.REDISSON_CLIENT_LEASE_TIME, java.util.concurrent.TimeUnit.SECONDS);

            if(isLocked){
                log.info("TaskExecutionListener [processChange] Acquired distributed lock on key: {}", lockKey);
                String taskId = getString(fullDocument, "taskId");
                String status = getString(fullDocument, "status");
                String funnel = getString(fullDocument, "funnel");
                String applicationId = getString(fullDocument, "applicationId");
                String entityId = getString(fullDocument, "entityId");
                String channel = getString(fullDocument, "channel");

                Instant createdAt = getInstant(fullDocument, "createdAt");
                Instant updatedAt = getInstant(fullDocument, "updatedAt");

                // Use createdAt for NEW status, updatedAt for others
                Instant eventTime = status.equalsIgnoreCase("NEW") ? createdAt : updatedAt;

                // Call service to update task execution time
                timeService.updateTaskExecutionTime(taskId, status, createdAt, updatedAt, funnel, applicationId, entityId, channel);
            }
            else{
                log.info("TaskExecutionListener [processChangeStreamDocument] Unable to acquire lock for task: {}", fullDocument.getObjectId("_id").toHexString());
            }
        } catch (Exception e) {
            log.error("Error processing change stream event", e);
            Thread.currentThread().interrupt();
        }finally {
            if (isLocked) {
                lock.unlock();
                log.info("TaskExecutionListener [processChangeStreamDocument] Released distributed lock on key: {}", lockKey);
            }
        }
    }

    private String getString(Document document, String field) {
        Object value = document.get(field);
        return value instanceof String ? (String) value : null;
    }

    private Instant getInstant(Document document, String field) {
        Object value = document.get(field);
        if (value instanceof java.util.Date) {
            return ((java.util.Date) value).toInstant();
        }
        return Instant.now();
    }

    public void shutdown() {
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}

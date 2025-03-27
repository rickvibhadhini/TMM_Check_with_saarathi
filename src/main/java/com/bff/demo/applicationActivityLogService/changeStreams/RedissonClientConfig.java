package com.bff.demo.applicationActivityLogService.changeStreams;




import com.bff.demo.constants.BffConstant;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonClientConfig {

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+ BffConstant.RedissonLockConstants.REDIS_HOST + ":" + BffConstant.RedissonLockConstants.REDIS_PORT)
                .setPassword(BffConstant.RedissonLockConstants.REDIS_PASSWORD)
                .setConnectionMinimumIdleSize(BffConstant.RedissonLockConstants.REDISSON_MIN_IDLE_CONNECTIONS)
                .setConnectionPoolSize(BffConstant.RedissonLockConstants.TOTAL_CONNECTIONS)
                .setRetryAttempts(BffConstant.RedissonLockConstants.REDISSON_RETRY_ATTEMPTS);
        return Redisson.create(config);
    }
}

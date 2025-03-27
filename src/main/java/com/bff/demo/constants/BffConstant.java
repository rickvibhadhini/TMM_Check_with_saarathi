package com.bff.demo.constants;



import java.text.SimpleDateFormat;
import java.util.Set;

public class BffConstant {


    public class RedissonLockConstants {

        public static final String REDIS_HOST = "redis-19810.crce182.ap-south-1-1.ec2.redns.redis-cloud.com";
        public static final int REDIS_PORT = 19810;
        public static final String REDIS_PASSWORD = "5YnWMTjGEyIM3EOPemIE1A5OPEV3C0hR";
        public static final boolean REDIS_SSL = false;
        public static final int MAX_IDLE = 5;
        public static final int MIN_IDLE = 2;
        public static final int TOTAL_CONNECTIONS = 10;
        public static final String RESUME_TOKEN_COLLECTION = "resume_tokens";
        public static final String RESUME_TOKEN_KEY = "change_stream_resume_token";
        public static final int TTL = 1080;
        public static final int REDISSON_CLIENT_WAIT_TIME = 10;
        public static final int REDISSON_CLIENT_LEASE_TIME = 30;
        public static final int REDISSON_MIN_IDLE_CONNECTIONS = 4;
        public static final int REDISSON_RETRY_ATTEMPTS = 5;
    }

    public static class ActivityLogConstants{
        public static  final String UNKNOWN_FUNNEL = "Unknown Funnel";
        public static final String UNKNOWN_TASK = "UNKNOWN_TASK";
        public static final String UNKNOWN_KEY = "UNKNOWN_KEY";
        public static final String UNKNOWN_STAGE_MODULE = "UNKNOWN_STAGE_MODULE";
        public static final String SOURCING = "sourcing";
        public static final String CREDIT = "credit";
        public static final String CONVERSION = "conversion";
        public static final String FULFILLMENT = "fulfillment";
        public static final String RISK = "risk";
        public static final String RTO = "rto";
        public static final String DISBURSAL = "disbursal";
    }

}

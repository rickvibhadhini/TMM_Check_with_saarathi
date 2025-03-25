package com.bff.demo.applicationActivityLog.changeStreams;



public final class ActivityLogConstants {
    private ActivityLogConstants() {}

    public static final class TaskTypes {
        public static final String SENDBACK = "sendback";
        public static final String UNKNOWN = "UNKNOWN_TASK";
    }

    public static final class Funnel {
        public static final String UNKNOWN = "Unknown Funnel";
        public static final String UNKNOWN_KEY = "UNKNOWN_KEY";
        public static final String UNKNOWN_STAGE_MODULE = "UNKNOWN_STAGE_MODULE";
    }

    public static final class ResponseKeys {
        public static final String TASKS_GROUPED_BY_FUNNEL = "tasksGroupedByFunnel";
        public static final String SENDBACK_TASKS = "sendbackTasks";
        public static final String LATEST_TASK_STATE = "latestTaskState";
    }

    public static final class FunnelDataKeys {
        public static final String FUNNEL = "funnel";
        public static final String FUNNEL_DURATION = "funnelDuration";
        public static final String TASKS = "tasks";
    }

    public static final class TaskStateKeys {
        public static final String TASK_ID = "taskId";
        public static final String ORDER = "order";
        public static final String HANDLED_BY = "handledBy";
        public static final String CREATED_AT = "createdAt";
        public static final String STATUS = "status";
        public static final String UPDATED_AT = "updatedAt";
        public static final String DURATION = "duration";
        public static final String SENDBACKS = "sendbacks";
        public static final String VISITED = "visited";
    }
}
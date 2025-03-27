package com.bff.demo.constants;

public class SendbackConstant {
	public static final String SENDBACK_KEY = "sendbackKey";
	public static final String SENDBACK_STATUS = "sendbackStatus";
	public static final String SENDBACK_REMARK = "sendbackRemark";
	public static final String SOURCE_TASK_ID = "sourceTaskId";
	public static final String SOURCE_FUNNEL = "sourceFunnel";
	public static final String SOURCE_AGENT_ID = "sourceAgentId";
	public static final String INITIATED_AT = "initiatedAt";
	public static final String INITIATED_BY = "initiatedBy";
	public static final String SENDBACK_PROCESSED_AT = "sendbackProcessedAt";
	public static final String SENDBACK_PROCESSED_BY = "sendbackProcessedBy";
	public static final String SENDBACK_REJECTION_REASON = "sendbackRejectionReason";
	public static final String CAN_VALIDATE_SOURCE_SENDBACK_TASKS = "canValidateSourceSendbackTasks";
	public static final String SENDBACK_TASK_ID = "sendback";
	public static String updateByAgent(String firstName, String lastName, String hierarchy, String role){
		return String.format("%s %s (%s %s)", firstName, lastName, role, hierarchy);
	}
}

package com.bff.demo.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum APIResponseCode {

    SUCCESS("BLL-200", "Success", HttpStatus.OK),
    ACCEPTED("BLL-202", "Accepted", HttpStatus.ACCEPTED),
    BAD_REQUEST("BLL-400", "Bad Request!", HttpStatus.BAD_REQUEST),
    FORBIDDEN("BLL-403", "Access Denied! Please contact your administrator", HttpStatus.FORBIDDEN),
    OTP_VERIFY_ERROR("BLL-5005","Unable to verify OTP. Please try again !", HttpStatus.BAD_REQUEST),
    CUSTOMER_REJECTED("BLL-5015","Customer Rejected !", HttpStatus.BAD_REQUEST),
    PAN_DEDUPE("BLL-5015","Pan already exists.", HttpStatus.BAD_REQUEST),
    NOT_FOUND("BLL-404", "Data Not found", HttpStatus.NOT_FOUND),

    PINCODE_NOT_SERVICEABLE("BLL-4001", "Sorry we’re not yet serviceable on this pin code. Retry to check for a different pin code. Continue if this is the correct pin code.", HttpStatus.BAD_REQUEST),

    PAN_DATA_NAME_MISMATCH("BLL-4002", "The Name does not match with PAN details. Check again.", HttpStatus.BAD_REQUEST),
    PAN_DATA_DOB_MISMATCH("BLL-4010", "The DOB does not match with PAN details. Check again.", HttpStatus.BAD_REQUEST),
    PAN_DETAIL_NOT_FOUND("BLL-4007", "Invalid PAN, details not found", HttpStatus.BAD_REQUEST),

    PINCODE_NOT_FOUND("BLL-4003", "Invalid Pincode", HttpStatus.BAD_REQUEST),
    ADDRESS_UPDATE_FAILED("BLL-4004", "Unable to update Address", HttpStatus.BAD_REQUEST),
    ADDRESS_NOT_FOUND("BLL-4005", "Lead not found", HttpStatus.NOT_FOUND),
    LEAD_NOT_FOUND("BLL-4006", "Lead not found", HttpStatus.NOT_FOUND),
    MOBILE_VALIDATION_FAILED("BLL-4015","Mobile validation failed",HttpStatus.PRECONDITION_FAILED),
    AGE_VALIDATION_FAILED("BLL-4008", "Age validation failed", HttpStatus.BAD_REQUEST),
    AUTH_TOKEN_REQUIRED("BLL-401", "Authentication token is required to access this resource", HttpStatus.UNAUTHORIZED),
    DOCUMENT_UPLOAD_SIZE_EXCEEDED("BLL-402", "Document upload size exceeded", HttpStatus.BAD_REQUEST),
    DOCUMENT_EXTENSION_NOT_SUPPORTED("BLL-406", "Document extension is not supported", HttpStatus.BAD_REQUEST),
    INVALID_MULTIPART_REQUEST("BLL-407","Document must be of type MultipartFile", HttpStatus.BAD_REQUEST),
    DEALER_CODE_NOT_FOUND("BLL-408","Dealer Code Not Found", HttpStatus.BAD_REQUEST),
    AGENT_NOT_FOUND("BLL-409","Agent Not Found", HttpStatus.BAD_REQUEST),
    DOCUMENT_GET_ERROR("BLL-404","Documents Fetch Problem", HttpStatus.BAD_REQUEST ),
    RTO_DOCS_INCOMPLETE("BLL-410","RTO Document completion is incomplete", HttpStatus.BAD_REQUEST),
    DOCUMENT_INVALID_PASSWORD("BLL-411", "Document Password is incorrect", HttpStatus.BAD_REQUEST),
    INSPECTION_ALREADY_EXISTS_IN_DEALER_LIST("BLL-4012", "The car already exists in the dealer list", HttpStatus.NOT_FOUND),
    SLOTS_NOT_AVAILABLE("BLL-4013", "No available slots.", HttpStatus.NOT_FOUND),

    CALL_LIMIT_EXCEED("BLL-429", "Call limit exceeded", HttpStatus.TOO_MANY_REQUESTS),
    AGENT_NOT_AUTHORIZED_TO_CALL("BLL-4011", "Lead is not assigned to the agent trying to call", HttpStatus.BAD_REQUEST),
    SECONDARY_PHONE_SAME_AS_PRIMARY("BLL-4012", "Secondary phone number cannot be same as primary phone number", HttpStatus.BAD_REQUEST),

    //banking errors
    BANKING_MONTHS_DATA_ERROR("BLL-4100","Success",HttpStatus.OK),
    BANKING_RETRY_ERROR("BLL-4101","Retry",HttpStatus.BAD_REQUEST),
    BANKING_EMPLOYMENT_DETAILS_ERROR("BLL-4102","Please provide all the employment details before initiating Banking Upload",HttpStatus.BAD_REQUEST),

    PENDING_TASKS("BLL-4200", "Requested operation not allowed: Pending Tasks found", HttpStatus.BAD_REQUEST),
    PENDING_LOWER_ORDER_TASKS("BLL-4201","Requested operation not allowed: Lower Order Tasks Pending", HttpStatus.BAD_REQUEST),
    SENDBACK_TASK("BLL-4202","Requested operation not allowed: Task In SendBack State", HttpStatus.BAD_REQUEST),
    FUNNEL_LOCKED("BLL-4300", "Requested operation not allowed: Funnel Closed", HttpStatus.BAD_REQUEST),
    TASK_TODO_REQUIRED("BLL-4301", "Requested operation not allowed: Task needs to be in TODO", HttpStatus.BAD_REQUEST),
    DISBURSAL_TASK_NOT_INITIATED("BLL-4302", "Lead Application has not reached to Disbursal stage", HttpStatus.BAD_REQUEST),
    TASK_NOT_INITIATED("BLL-4303", "The loan application has not reached to this step yet. Please wait for the previous steps to complete", HttpStatus.BAD_REQUEST),
    TASK_ACTION_TAKEN("BLL-4304", "This process has already completed, please request sendback if rework is required", HttpStatus.BAD_REQUEST),
    //500 errors
    BANKING_INTERNAL_SERVER_ERROR("BLL-5004", "Oops something went wrong, please try again after sometime!", HttpStatus.INTERNAL_SERVER_ERROR),
    INTERNAL_SERVER_ERROR("BLL-500", "Oops something went wrong, please try again after sometime!", HttpStatus.INTERNAL_SERVER_ERROR),
    SVC_CLIENT_EXCEPTION("BLL-5003", "Oops something went wrong, please try again after sometime!", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE("BLL-503", "Service Unavailable, please try again after sometime!", HttpStatus.SERVICE_UNAVAILABLE),
    PAN_VALIDATION_FAILED("BLL-5001", "Unable to validate PAN", HttpStatus.INTERNAL_SERVER_ERROR),
    OGL_CHECK_FAILED("BLL-5002", "Unable to check pincode serviceability", HttpStatus.INTERNAL_SERVER_ERROR),
    CALLING_INTERNAL_SERVER_ERROR("BLL-500", "Call Initiation failed", HttpStatus.INTERNAL_SERVER_ERROR),
    TASK_NOT_FOUND("BLL-5004", "Oops something went wrong, please try again after sometime!", HttpStatus.NOT_FOUND),
    LEAD_ENTITY_ALREADY_IN_TERMINAL_STATE("BLL-412", "Loan Application already exist in terminal state", HttpStatus.BAD_REQUEST),

    //sendback errors
    SENDBACK_ALREADY_RAISED("BLL-404", "Sendback already raised for given applicationId", HttpStatus.BAD_REQUEST),
    SENDBACK_STATUS_NOT_AVAILABLE("BLL-404", "No such sendback status available", HttpStatus.BAD_REQUEST),

    //bookingTransfer errors
    INVALID_LEAD("BLL-406", "Booking Transfer Not Allowed", HttpStatus.NOT_ACCEPTABLE),
    FAILED_TO_FETCH_BOOKING_DATA("BLL-5006", "Failed to fetch booking details", HttpStatus.SERVICE_UNAVAILABLE),
    INVALID_PHONE_NUMBER("BLL-5007", "Invalid Phone Number", HttpStatus.INTERNAL_SERVER_ERROR);


    private final String statusCode;
    private final String message;
    private final HttpStatus httpStatus;

    APIResponseCode(String statusCode, String message, HttpStatus httpStatus) {
        this.statusCode = statusCode;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

package com.bff.demo.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class APIResponse<T>{

    private boolean success;
    private String statusCode;
    private String message;
    private T data;

    public static <T> APIResponse<T> ok(T data) {
        return APIResponse.<T>builder()
                .success(Boolean.TRUE)
                .statusCode(APIResponseCode.SUCCESS.getStatusCode())
                .message(APIResponseCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }
    public static <T> APIResponse<T> ok(T data,String message) {
        return APIResponse.<T>builder()
                .success(Boolean.TRUE)
                .statusCode(APIResponseCode.SUCCESS.getStatusCode())
                .message(message)
                .data(data)
                .build();
    }
    public static <T> APIResponse<T> ok(T data, APIResponseCode apiResponseCode) {
        return APIResponse.<T>builder()
                .success(Boolean.TRUE)
                .statusCode(apiResponseCode.getStatusCode())
                .data(data)
                .build();
    }

    public static <T> APIResponse<T> accepted(T data, APIResponseCode apiResponseCode) {
        return APIResponse.<T>builder()
                .success(Boolean.TRUE)
                .statusCode(apiResponseCode.getStatusCode())
                .message(apiResponseCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> APIResponse<T> accepted() {
        return APIResponse.<T>builder()
                .success(Boolean.TRUE)
                .statusCode(APIResponseCode.ACCEPTED.getStatusCode())
                .message(APIResponseCode.ACCEPTED.getMessage())
                .build();
    }

    public static <T> APIResponse<T> error(T data, APIResponseCode apiResponseCode) {
        return APIResponse.<T>builder()
                .success(Boolean.FALSE)
                .statusCode(apiResponseCode.getStatusCode())
                .message(apiResponseCode.getMessage())
                .data(data)
                .build();
    }
    public static <T> APIResponse<T> error(APIResponseCode apiResponseCode) {
        return APIResponse.<T>builder()
                .success(Boolean.FALSE)
                .statusCode(apiResponseCode.getStatusCode())
                .message(apiResponseCode.getMessage())
                .build();
    }

    public static <T> APIResponse<T> error(APIResponseCode apiResponseCode, String message) {
        return APIResponse.<T>builder()
                .success(Boolean.FALSE)
                .statusCode(apiResponseCode.getStatusCode())
                .message(message)
                .build();
    }
}

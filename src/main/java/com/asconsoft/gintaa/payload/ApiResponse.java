package com.asconsoft.gintaa.payload;

import com.asconsoft.gintaa.common.exception.GintaaException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private int code;
    private String message;
    private T payload;
    private ApiError error;

    public static <T extends Object> ApiResponse<T> ofSuccess(String message) {
        return ofSuccess(HttpStatus.OK.value(), null, message);
    }

    public static <T extends Object> ApiResponse<T> ofSuccess(int code, String message) {
        return ofSuccess(code, null, message);
    }

    public static <T extends Object> ApiResponse<T> ofSuccess(int code, T payload) {
        return ofSuccess(code, payload, null);
    }

    public static <T extends Object> ApiResponse<T> ofSuccess(T payload, String message) {
        return ofSuccess(HttpStatus.OK.value(), payload, message);
    }

    public static <T extends Object> ApiResponse<T> ofSuccess(int code, T payload, String message) {
        return new ApiResponse<>(true, code, message, payload, null);
    }

    public static <T extends Object> ApiResponse<T> ofFailure(String message) {
        return ofFailure(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, message);
    }

    public static <T extends Object> ApiResponse<T> ofFailure(int code, String message) {
        return ofFailure(code, null, message);
    }

    public static <T extends Object> ApiResponse<T> ofFailure(int code, ApiError error) {
        return ofFailure(code, error, null);
    }

    public static <T extends Object> ApiResponse<T> ofFailure(ApiError error, String message) {
        return ofFailure(HttpStatus.INTERNAL_SERVER_ERROR.value(), error, message);
    }

    public static <T extends Object> ApiResponse<T> ofFailure(int code, ApiError error, String message) {
        return new ApiResponse<>(false, code, message, null, error);
    }

    public static <T extends Object> ApiResponse<T> fromException(GintaaException e) {
        return new ApiResponse<>(false, e.getCode(), e.getMessage(), null, null);
    }

}

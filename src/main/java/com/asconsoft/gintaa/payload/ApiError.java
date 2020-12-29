package com.asconsoft.gintaa.payload;

import org.eclipse.collections.impl.list.mutable.FastList;

import java.util.List;


public class ApiError {

    private String message;

    private List<FieldError> errors = FastList.newList();

    public ApiError() {
    }

    public ApiError(String message, List<FieldError> errors) {
        super();
        this.message = message;
        this.errors = errors;
    }

    public static ApiError of(String message, List<FieldError> errors) {
        return new ApiError(message, errors);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldError> errors) {
        this.errors = errors;
    }

    public void addErrors(List<FieldError> fieldErrors) {
        if (!fieldErrors.isEmpty()) {
            this.errors.addAll(fieldErrors);
        }
    }

    public static ApiError newApiError() {
        return new ApiError();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

}

package com.seeds.common.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenericDto<T> {

    private boolean success;
    private int code;
    private String message;
    private T data;

    public static <T> GenericDto<T> success(T data) {
        return GenericDto.<T>builder()
                .code(200)
                .success(true)
                .data(data)
                .build();
    }

    public static <T> GenericDto<T> failure(String message, int code) {
        return GenericDto.<T>builder()
                .code(code)
                .success(false)
                .message(message)
                .build();
    }

    public static <T> GenericDto<T> failure(int code, String message) {
        return GenericDto.<T>builder()
                .code(code)
                .success(false)
                .message(message)
                .build();
    }

    public static <T> GenericDto<T> failure(String message, int code, T data) {
        return GenericDto.<T>builder()
                .code(code)
                .success(false)
                .data(data)
                .message(message)
                .build();
    }

}

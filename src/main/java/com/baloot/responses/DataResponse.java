package com.baloot.responses;

import lombok.Getter;

public class DataResponse<T> extends Response {
    @Getter
    private T data;

    private DataResponse(boolean success, String message, T data) {
        super(success, message);
        this.data = data;
    }

    public static <K> DataResponse<K> Successful(K data) {
        return new DataResponse<>(true, "", data);
    }
    public static DataResponse<Object> Failed(String message) {
        return new DataResponse<>(false, message, null);
    }
    public static DataResponse<Object> Successful() {
        return new DataResponse<>(true, "", null);
    }
}

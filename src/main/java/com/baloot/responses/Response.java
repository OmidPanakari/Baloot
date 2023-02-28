package com.baloot.responses;

import lombok.Getter;

public class Response<T> {
    @Getter
    private boolean success;
    @Getter
    private T data;

    public Response(boolean success, T data){
        this.data = data;
        this.success = success;
    }
}

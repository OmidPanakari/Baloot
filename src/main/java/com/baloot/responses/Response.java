package com.baloot.responses;

import lombok.Getter;
import lombok.Setter;

public class Response<T> {
    private boolean success;
    private T data;

    public Response(boolean success, T data){
        this.data = data;
        this.success = success;
    }
}

package com.baloot.responses;

import lombok.Getter;

public class DataResponse<T> extends Response {
    @Getter
    private T data;

    public DataResponse(boolean success, T data){
        super(success);
        this.data = data;
    }
}

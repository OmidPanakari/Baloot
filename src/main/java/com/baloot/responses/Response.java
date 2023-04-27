package com.baloot.responses;

import lombok.Getter;

public abstract class Response {
    @Getter
    private boolean success;
    @Getter
    private String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}

package com.baloot.responses;

import lombok.Getter;

public abstract class Response {
    @Getter
    private boolean success;

    public Response(boolean success) {
        this.success = success;
    }
}

package com.baloot.core.entities;

import lombok.Getter;
import lombok.Setter;

public class CommentVote {
    public CommentVote(String username, int vote) {
        this.username = username;
        this.vote = vote;
    }

    @Getter
    private String username;
    @Getter @Setter
    private int vote;
}

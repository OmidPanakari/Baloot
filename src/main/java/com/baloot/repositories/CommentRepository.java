package com.baloot.repositories;

import com.baloot.entities.Comment;

public class CommentRepository {
    private final Database database;

    public CommentRepository(Database database){
        this.database = database;
    }

    public Comment getComment(int commentId){
        for (var comment: database.getComments()) {
            if (comment.getId() == commentId)
                return comment;
        }
        return null;
    }
}

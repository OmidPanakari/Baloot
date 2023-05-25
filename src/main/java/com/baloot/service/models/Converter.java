package com.baloot.service.models;

import com.baloot.core.entities.Comment;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Converter {
    public static CommentModel convertToModel(Comment comment) {
        return new CommentModel(comment.getId(), comment.getText(), comment.getUser().getUsername(), comment.getLikes(),
            comment.getDislikes(), comment.getDate());
    }
    public static List<CommentModel> convertToModel(Set<Comment> comments) {
        return comments.stream().map(Converter::convertToModel).toList();
    }
}

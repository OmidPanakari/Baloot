package com.baloot.service.models;

public record CommentModel(int id, String text, String username, int likes, int dislikes, String date) {
}

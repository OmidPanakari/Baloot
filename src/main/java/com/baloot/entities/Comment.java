package com.baloot.entities;

import lombok.Getter;

public class Comment {

    @Getter
    private static int NextID = 0;
    @Getter
    private String userEmail;
    @Getter
    private int commodityId;
    @Getter
    private String text;
    @Getter
    private String date;
    @Getter
    private transient int id;
    @Getter
    private int rating;

    public Comment(Comment comment) {
        this.userEmail = comment.userEmail;
        this.commodityId = comment.commodityId;
        this.text = comment.text;
        this.date = comment.date;
        this.id = NextID++;
        rating = 0;
    }

    public void addRating(int score){
        rating += score;
    }


}

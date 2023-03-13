package com.baloot.entities;

import lombok.Getter;
import lombok.Setter;

public class Comment {
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
    private int likes;
    @Getter
    private int dislikes;
    @Getter @Setter
    private transient String username;

    public Comment(Comment comment) {
        this.userEmail = comment.userEmail;
        this.commodityId = comment.commodityId;
        this.text = comment.text;
        this.date = comment.date;
        this.id = NextID++;
        likes = 0;
        dislikes = 0;
    }

    public void voteComment(int vote){
        if (vote == 1)
            likes += 1;
        else if (vote == -1)
            dislikes += 1;
    }


}

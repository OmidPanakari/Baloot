package com.baloot.core.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    private String username;
    private transient List<CommentVote> votes;

    public Comment(String username, String userEmail, int commodityId, String text, String date) {
        this.username = username;
        this.commodityId = commodityId;
        this.userEmail = userEmail;
        this.text = text;
        this.date = date;
        this.id = NextID++;
        likes = 0;
        dislikes = 0;
        votes = new ArrayList<>();
    }

    public Comment(Comment comment) {
        this.userEmail = comment.userEmail;
        this.commodityId = comment.commodityId;
        this.text = comment.text;
        this.date = comment.date;
        this.id = NextID++;
        likes = 0;
        dislikes = 0;
        votes = new ArrayList<>();
    }

    public List<CommentVote> getVotes() {
        if (votes == null)
            votes = new ArrayList<>();
        return votes;
    }

    public void voteComment(String username, int vote){
        var prev = getVotes().stream().filter(v -> v.getUsername().equals(username)).findFirst().orElse(null);
        if (prev == null) {
            getVotes().add(new CommentVote(username, vote));
            if (vote == 1)
                likes += 1;
            else if (vote == -1)
                dislikes += 1;
        }
        else {
            if (vote == 1) {
                if (prev.getVote() == 1) {
                    getVotes().remove(prev);
                    likes -= 1;
                }
                else {
                    prev.setVote(1);
                    likes += 1;
                    dislikes -= 1;
                }
            }
            else {
                if (prev.getVote() == -1) {
                    getVotes().remove(prev);
                    dislikes -= 1;
                }
                else {
                    prev.setVote(-1);
                    likes -= 1;
                    dislikes += 1;
                }
            }
        }
    }


}

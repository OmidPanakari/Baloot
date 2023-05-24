package com.baloot.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment {
    private static int NextID = 1;

    @Id
    @Getter
    private int id;
    @ManyToOne
    @JoinColumn(name = "commodityId")
    @Getter
    private Commodity commodity;
    @Getter
    private String text;
    @Getter
    private String date;
    @Getter
    private int likes;
    @Getter
    private int dislikes;
    @ManyToOne
    @JoinColumn(name = "username")
    @Getter @Setter
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment")
    private List<Vote> votes;

    public Comment(String username, int commodityId, String text, String date) {
        this.user = new User();
        this.user.setUsername(username);
        this.commodity = new Commodity();
        this.commodity.setId(commodityId);
        this.text = text;
        this.date = date;
        this.id = NextID++;
        likes = 0;
        dislikes = 0;
        votes = new ArrayList<>();
    }

    public List<Vote> getVotes() {
        if (votes == null)
            votes = new ArrayList<>();
        return votes;
    }

    public void voteComment(String username, int vote){
        var prev = getVotes().stream().filter(v -> v.getUser().getUsername().equals(username)).findFirst().orElse(null);
        if (prev == null) {
            getVotes().add(new Vote(username, vote));
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

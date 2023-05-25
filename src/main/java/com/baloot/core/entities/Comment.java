package com.baloot.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    @Transient
    private int likes;
    @Transient
    private int dislikes;
    @ManyToOne
    @JoinColumn(name = "username")
    @Getter @Setter
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "comment", fetch = FetchType.EAGER)
    @Getter
    private Set<Vote> votes;

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
        votes = new HashSet<>();
    }

    public void addVote(Vote vote) {
        votes.add(vote);
    }

    public int getLikes() {
        return votes.stream().mapToInt(Vote::getVote).filter(v -> v > 0).sum();
    }
    public int getDislikes() {
        return -votes.stream().mapToInt(Vote::getVote).filter(v -> v < 0).sum();
    }

}

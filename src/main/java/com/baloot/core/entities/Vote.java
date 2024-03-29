package com.baloot.core.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "votes")
public class Vote {
    public Vote(String username, int vote) {
        this.user = new User();
        this.user.setUsername(username);
        this.vote = vote;
    }

    public Vote(User user, int vote, Comment comment) {
        this.user = user;
        this.vote = vote;
        this.comment = comment;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private int id;
    @ManyToOne
    @JoinColumn(name = "username")
    @Getter
    private User user;
    @Getter @Setter
    private int vote;
    @ManyToOne
    @JoinColumn(name = "commentId")
    @Getter
    private Comment comment;
}

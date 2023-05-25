package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.Comment;
import com.baloot.core.entities.User;
import com.baloot.core.entities.Vote;
import com.baloot.dataAccess.Database;
import com.baloot.utils.HibernateUtil;
import org.hibernate.Session;

public class CommentRepository {
    private final Database database;

    public CommentRepository(Database database){
        this.database = database;
    }

    public Comment getComment(int commentId){
        Session session = HibernateUtil.getSessionFactory().openSession();
        var comment = session.get(Comment.class, commentId);
        session.close();
        return comment;
    }

    public void addComment(Comment comment) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        session.save(comment);
        transaction.commit();
        session.close();
    }

    public Comment voteComment(Comment comment, User user, int vote) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        var commentVote = session.createQuery("FROM Vote WHERE commentId = :commentId AND username = :username",
            Vote.class)
            .setParameter("commentId", comment.getId())
            .setParameter("username", user.getUsername())
            .uniqueResult();
        if (commentVote != null) {
            commentVote.setVote(vote);
            session.update(commentVote);
        }
        else {
            commentVote = new Vote(user, vote, comment);
            session.save(commentVote);
            comment.addVote(commentVote);
        }
        transaction.commit();
        session.close();
        return commentVote.getComment();
    }
}

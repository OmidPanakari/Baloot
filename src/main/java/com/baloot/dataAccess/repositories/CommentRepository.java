package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.Comment;
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
}

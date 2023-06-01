package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.Discount;
import com.baloot.core.entities.User;
import com.baloot.dataAccess.Database;
import com.baloot.utils.HibernateUtil;
import org.hibernate.Session;

import java.io.IOException;
import java.util.Objects;

public class UserRepository {
    private final Database database;

    public UserRepository(Database database){
        this.database = database;
    }

    public void addUser(User user){
        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        session.saveOrUpdate(user);
        transaction.commit();
        session.close();
    }

    public void updateUser(User oldUser, User newUser){
        oldUser.setCredit(newUser.getCredit());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setAddress(newUser.getAddress());
        oldUser.setPassword(newUser.getPassword());
        oldUser.setBirthDate(newUser.getBirthDate());
    }

    public User findUser(String username){
        Session session = HibernateUtil.getSessionFactory().openSession();
        var user = session.get(User.class, username);
        session.close();
        return user;
    }

    public User findUserByEmail(String email){
        Session session = HibernateUtil.getSessionFactory().openSession();
        var query = session.createQuery("FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        var user = (User) query.uniqueResult();
        session.close();
        return user;
    }

    public boolean addCredit(String username, int credit) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        var user = session.get(User.class, username);
        if (user == null) {
            session.close();
            return false;
        }
        user.setCredit(user.getCredit() + credit);
        transaction.commit();
        session.close();
        return true;
    }

    public boolean purchaseBuyList(User user, Discount discount) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        var price = user.getBuyList().stream().mapToInt(c -> c.getCommodity().getPrice() * c.getInCart()).sum();
        if (discount != null) {
            price = price * (100 - discount.getDiscount()) / 100;
        }
        if (user.getCredit() < price) {
            session.close();
            return false;
        }
        user.setCredit(user.getCredit() - price);
        user.addToPurchaseList(user.getBuyList());
        user.getBuyList().forEach(session::remove);
        user.getBuyList().clear();
        session.update(user);
        transaction.commit();
        session.close();
        return true;
    }

    public void addToBuyList(User user, Commodity commodity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        user.addToBuyList(commodity);
        commodity.setInStock(commodity.getInStock() - 1);
        session.update(user);
        session.update(commodity);
        transaction.commit();
        session.close();
    }
    public boolean removeFromBuyList(User user, Commodity commodity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        if (!user.removeFromBuyList(commodity, session))
            return false;
        commodity.setInStock(commodity.getInStock() + 1);
        session.update(user);
        session.update(commodity);
        transaction.commit();
        session.close();
        return true;
    }
}

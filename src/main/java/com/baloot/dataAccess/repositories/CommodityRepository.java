package com.baloot.dataAccess.repositories;

import com.baloot.core.entities.Commodity;
import com.baloot.core.entities.CommodityRating;
import com.baloot.core.entities.User;
import com.baloot.dataAccess.Database;
import com.baloot.dataAccess.models.CommodityList;
import com.baloot.dataAccess.utils.QueryModel;
import com.baloot.utils.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommodityRepository {
    private final Database database;

    public CommodityRepository(Database database) {
        this.database = database;
    }

    public boolean addCommodity(Commodity commodity) {
        var existingCommodity = findCommodity(commodity.getId());
        if (existingCommodity != null)
            return false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        session.save(commodity);
        transaction.commit();
        session.close();
        return true;
    }

    public CommodityList getCommodities(QueryModel queryModel) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var result = applyQuery(session, queryModel);
        session.close();
        return result;
    }

    public Commodity findCommodity(int commodityId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var commodity = session.get(Commodity.class, commodityId);
        session.close();
        return commodity;
    }

    public List<Commodity> getSuggestions(Commodity commodity) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var categoryList = new ArrayList<>(session.get(Commodity.class, commodity.getId()).getCategories());
        var suggestions = session
            .createQuery("SELECT c FROM Commodity c JOIN c.categories cat WHERE c.id <> :commodityId AND cat IN :selectedCategories ORDER BY SIZE(c.categories) DESC",
                Commodity.class)
            .setParameter("commodityId", commodity.getId())
            .setParameterList("selectedCategories", categoryList)
            .setMaxResults(4)
            .list();
        session.close();
        return suggestions;
    }

    public CommodityRating rateCommodity(Commodity commodity, User user, double rate) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        var transaction = session.beginTransaction();
        var rating = session
            .createQuery("FROM CommodityRating WHERE commodityId = :commodityId AND username = :username", CommodityRating.class)
            .setParameter("commodityId", commodity.getId())
            .setParameter("username", user.getUsername())
            .uniqueResult();
        if (rating != null) {
            rating.setRating(rate);
            session.update(rating);
        }
        else {
            rating = new CommodityRating(commodity, user, rate);
            session.save(rating);
            commodity.addRating(rating);
        }
        session.update(rating.getCommodity());
        transaction.commit();
        session.close();
        return rating;
    }

    private CommodityList applyQuery(Session session, QueryModel queryModel) {
        return new CommodityList(getFilteredResult(session, queryModel), getTotalPages(session, queryModel));
    }

    private List<Commodity> getFilteredResult(Session session, QueryModel queryModel) {
        var builder = session.getCriteriaBuilder();
        var query = builder.createQuery(Commodity.class);
        var root = query.from(Commodity.class);
        query = query.select(root);
        if (queryModel == null)
            return session.createQuery(query).getResultList();
        query = applyFilters(root, query, builder, queryModel);
        return applyPagination(query, builder, root, session, queryModel.page(), queryModel.limit());
    }

    private int getTotalPages(Session session, QueryModel queryModel) {
        if (queryModel == null || queryModel.limit() == null || queryModel.limit() == 0)
            return 1;
        var builder = session.getCriteriaBuilder();
        var query = builder.createQuery(Long.class);
        var root = query.from(Commodity.class);
        query = query.select(builder.count(root));
        query = applyFilters(root, query, builder, queryModel);
        var totalResults = session.createQuery(query).getSingleResult();
        return (Math.toIntExact(totalResults) + queryModel.limit() - 1) / queryModel.limit();
    }

    private <T> CriteriaQuery<T>  applyFilters(Root<Commodity> root, CriteriaQuery<T> query,
                                               CriteriaBuilder builder, QueryModel queryModel) {
        var filteredQuery = query;
        filteredQuery = applyAvailableFilter(root, filteredQuery, builder, queryModel.available());
        filteredQuery = applySearch(root, filteredQuery, builder, queryModel.search(), queryModel.searchType());
        filteredQuery = applySort(root, filteredQuery, builder, queryModel.sort());
        return filteredQuery;
    }

    private <T> CriteriaQuery<T> applySearch(Root<Commodity> root, CriteriaQuery<T> query,
                                        CriteriaBuilder builder, String search, String searchType) {
        if (search != null) {
            if (Objects.equals(searchType, "category")) {
                Join<Commodity, String> categoryJoin = root.join("categories");
                return query.where(builder.like(categoryJoin, "%" + search + "%"));
            }
            else if (Objects.equals(searchType, "name"))
                return query.where(builder.like(root.get("name"), "%" + search + "%"));
            else if (Objects.equals(searchType, "provider")) {
                var providerJoin = root.join("provider");
                return query.where(builder.like(providerJoin.get("name"), "%" + search + "%"));
            }
        }
        return query;
    }

    private <T> CriteriaQuery<T> applySort(Root<Commodity> root, CriteriaQuery<T> query,
                           CriteriaBuilder builder, String sort) {
        if (Objects.equals(sort, "name")) {
            return query.orderBy(builder.asc(root.get("name")));
        }
        else if (Objects.equals(sort, "price")) {
            return query.orderBy(builder.asc(root.get("price")));
        }
        else return query;
    }

    private List<Commodity> applyPagination(CriteriaQuery<Commodity> query, CriteriaBuilder builder, Root<Commodity> root,
                                                  Session session, Integer page, Integer limit) {
        var typedQuery = session.createQuery(query);
        if (limit == null || limit == 0)
            return typedQuery.getResultList();
        if (page == null || page == 0)
            return typedQuery.setMaxResults(limit).getResultList();
        return typedQuery.setFirstResult((page - 1) * limit).setMaxResults(limit).getResultList();
    }

    private <T> CriteriaQuery<T> applyAvailableFilter(Root<Commodity> root, CriteriaQuery<T> query,
                                                          CriteriaBuilder builder, Boolean available) {
        if (available == null || !available)
            return query;
        return query.where(builder.gt(root.get("inStock"), 0));
    }
}

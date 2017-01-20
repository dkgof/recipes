/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.db;

import java.util.LinkedHashSet;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

/**
 *
 * @author Gof
 */
public class Database {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        return new Configuration().configure().buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void doAction(DomainObject obj, Action action) {
        final Session session = Database.getSessionFactory().getCurrentSession();
        boolean newTransaction = false;
        try {
            if(session.getTransaction() != null && !session.getTransaction().isActive()) {
                session.beginTransaction();
                newTransaction = true;
            }
            switch (action) {
                case SAVE:
                    session.save(obj);
                    break;
                case SAVE_OR_UPDATE:
                    session.saveOrUpdate(obj);
                    break;
                case UPDATE:
                    session.update(obj);
                    break;
                case DELETE:
                    session.delete(obj);
                    break;
                case REFRESH:
                    session.refresh(obj);
                    break;
            }
        } finally {
            if(newTransaction) {
                session.getTransaction().commit();
            }
        }
    }

    public static void saveOrUpdate(DomainObject obj) {
        doAction(obj, Action.SAVE_OR_UPDATE);
    }

    public static void save(DomainObject obj) {
        doAction(obj, Action.SAVE);
    }

    public static void update(DomainObject obj) {
        doAction(obj, Action.UPDATE);
    }

    public static void delete(DomainObject obj) {
        doAction(obj, Action.DELETE);
    }

    public static void refresh(DomainObject obj) {
        doAction(obj, Action.REFRESH);
    }
    
    public static <T extends DomainObject> T get(String whereClause, Class<T> type) {
        return query("from " + type.getName() + " where " + whereClause, type);
    }    

    public static <T extends DomainObject> Set<T> getAll(Class<T> type) {
        return getAll("1=1", type);
    }
    
    public static <T extends DomainObject> Set<T> getAll(String whereClause, Class<T> type) {
        return queryAll("from " + type.getName() + " where " + whereClause, type);
    }
    
    public static <T extends DomainObject> T query(String hql, Class<T> aClass) {
        final Session session = Database.getSessionFactory().getCurrentSession();
        boolean newTransaction = false;
        try {
            if(session.getTransaction() != null && !session.getTransaction().isActive()) {
                session.beginTransaction();
                newTransaction = true;
            }
            
            return (T) session.createQuery(hql).uniqueResult();
        } finally {
            if(newTransaction) {
                session.getTransaction().commit();
            }
        }
    }

    public static <T extends DomainObject> Set<T> queryAll(String hql, Class<T> type) {
        return queryAll(hql, type, -1, -1);
    }

    public static <T extends DomainObject> Set<T> queryAll(String hql, Class<T> type, int firstResult, int maxResults) {
        final Session session = Database.getSessionFactory().getCurrentSession();
        boolean newTransaction = false;
        try {
            if(session.getTransaction() != null && !session.getTransaction().isActive()) {
                session.beginTransaction();
                newTransaction = true;
            }
            final Set<T> namedResult = new LinkedHashSet<>();
            
            Query query = session.createQuery(hql);
            if(firstResult != -1) {
                query.setFirstResult(firstResult);
            }
            
            if(maxResults != -1) {
                query.setMaxResults(maxResults);
            }
            
            for (Object resultObj : query.list()) {
                namedResult.add((T) resultObj);
            }
            return namedResult;
        } finally {
            if(newTransaction) {
                session.getTransaction().commit();
            }
        }
    }
    
    public enum Action {
        SAVE,
        UPDATE,
        DELETE,
        SAVE_OR_UPDATE,
        REFRESH
    }
}

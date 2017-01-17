/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.db;

import dk.fambagge.recipes.domain.Recipe;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
    
    public static <T extends DomainObject> Set<T> getAll(Class<T> type) {
        final Session session = Database.getSessionFactory().getCurrentSession();
        boolean newTransaction = false;
        try {
            if(session.getTransaction() != null && !session.getTransaction().isActive()) {
                session.beginTransaction();
                newTransaction = true;
            }
            final List result = session.createQuery("from "+type.getName()).list();
            final Set<T> namedResult = new LinkedHashSet<>();
            for (final Object resultObj : result) {
                T dbObject = (T) resultObj;
                dbObject.initializeLazy();
                namedResult.add(dbObject);
            }
            return namedResult;
        } finally {
            if(newTransaction) {
                session.getTransaction().commit();
            }
        }
    }

    public static <T extends DomainObject> T get(String query, Class<T> aClass) {
        final Session session = Database.getSessionFactory().getCurrentSession();
        boolean newTransaction = false;
        try {
            if(session.getTransaction() != null && !session.getTransaction().isActive()) {
                session.beginTransaction();
                newTransaction = true;
            }
            
            T dbObject = (T) session.createQuery(query).uniqueResult();
            dbObject.initializeLazy();
            
            return dbObject;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.fambagge.recipes.db;

import java.util.LinkedList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Gof
 */
public class Database {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Configuration configuration = new Configuration();
        configuration.configure();
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void doAction(DomainObject obj, Action action) {
        final Session session = Database.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
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
            }
        } finally {
            session.getTransaction().commit();
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

    public static <T extends DomainObject> List<T> getAll(Class<T> type) {
        final Session session = Database.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            final List result = session.createQuery("from "+type.getSimpleName()).list();
            final List<T> namedResult = new LinkedList<>();
            for (final Object resultObj : result) {
                namedResult.add((T) resultObj);
            }
            return namedResult;
        } finally {
            session.getTransaction().commit();
        }
    }

    public static <T extends DomainObject> T get(String query, Class<T> aClass) {
        final Session session = Database.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            return (T) session.createQuery(query).uniqueResult();
        } finally {
            session.getTransaction().commit();
        }
    }
    
    public enum Action {
        SAVE,
        UPDATE,
        DELETE,
        SAVE_OR_UPDATE
    }
}

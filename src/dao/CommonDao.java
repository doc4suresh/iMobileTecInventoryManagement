/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Query;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

/**
 *
 * @author USER
 */
public class CommonDao {

    public static void insert(Object object) throws DaoException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            session.save(object);
            transaction = session.beginTransaction();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            DaoException exeption = new DaoException();
            exeption.setMessage(e.getMessage());
            throw exeption;
        }
    }

    public static boolean insert(Object object, List list) {
        boolean status = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            session.save(object);

            for (Object st : list) {

                session.update(st);
            }
            transaction.commit();
            status = true;

        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }

            status = false;

        } finally {
            session.close();
        }

        return status;
    }

    public static ObservableList select(String nameQuery) {

        ObservableList obList = FXCollections.observableArrayList();
        List list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Query query = session.getNamedQuery(nameQuery);
            list = (List) query.list();

            for (Object element : list) {
                obList.add(element);
            }
        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return obList;
    }

    public static ObservableList select(String nameQuery, HashMap properties) {

        ObservableList obList = FXCollections.observableArrayList();
        List list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {

            transaction = session.beginTransaction();
            Query query = session.getNamedQuery(nameQuery).setProperties(properties);

            list = (List) query.list();
            for (Object element : list) {
                obList.add(element);
            }

        } catch (HibernateException e) {
            System.out.println(e.getMessage());

            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
        return obList;
    }

    public static void update(Object object) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(object);
            transaction.commit();

        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    public static void delete(Object object) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(object);
            transaction.commit();

        } catch (HibernateException e) {
            System.out.println(e.getMessage());
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

}

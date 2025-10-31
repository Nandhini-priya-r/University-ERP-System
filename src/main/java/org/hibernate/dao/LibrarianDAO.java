package org.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.entity.Librarian;
import org.hibernate.query.Query;
import org.hibernate.util.HibernateUtil;

import java.util.List;

public class LibrarianDAO {

    public Librarian save(Librarian l) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.persist(l);
        tx.commit();
        s.close();
        return l;
    }

    public Librarian update(Librarian l) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.merge(l);
        tx.commit();
        s.close();
        return l;
    }

    public void delete(int id) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        Librarian l = s.get(Librarian.class, id);
        if (l != null) s.remove(l);
        tx.commit();
        s.close();
    }

    public Librarian findById(int id) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Librarian l = s.get(Librarian.class, id);
        s.close();
        return l;
    }

    public List<Librarian> findAll() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Librarian> list = s.createQuery("from Librarian", Librarian.class).list();
        s.close();
        return list;
    }

    public Librarian findByCredentials(String username, String password) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Query<Librarian> q = s.createQuery("from Librarian where username=:u and password=:p", Librarian.class);
        q.setParameter("u", username);
        q.setParameter("p", password);
        Librarian l = q.uniqueResult();
        s.close();
        return l;
    }
}

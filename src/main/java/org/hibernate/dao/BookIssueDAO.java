package org.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.entity.BookIssue;
import org.hibernate.util.HibernateUtil;
import java.util.List;

public class BookIssueDAO extends GenericDAO<BookIssue> {

    public BookIssueDAO() {
        super(BookIssue.class);
    }

    // Save new book issue (issue record)
    @Override
    public BookIssue save(BookIssue issue) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(issue);
            tx.commit();
            return issue;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("❌ Error saving BookIssue: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Update issue when returning book
    @Override
    public BookIssue update(BookIssue issue) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(issue);
            tx.commit();
            return issue;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("❌ Error updating BookIssue: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Fetch issue record by ID
    @Override
    public BookIssue findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(BookIssue.class, id);
        } catch (Exception e) {
            System.err.println("❌ Error fetching BookIssue by ID: " + e.getMessage());
            return null;
        }
    }

    // List all issue records
    @Override
    public List<BookIssue> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from BookIssue", BookIssue.class).list();
        } catch (Exception e) {
            System.err.println("❌ Error listing BookIssues: " + e.getMessage());
            return List.of();
        }
    }

    // Delete a record
    @Override
    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            BookIssue issue = session.get(BookIssue.class, id);
            if (issue != null) {
                session.remove(issue);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("❌ Error deleting BookIssue: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public BookIssue save(BookIssue issue, Session session) {
        session.persist(issue); // do not open/close session here
        return issue;
    }

    // Update using existing session
    public BookIssue update(BookIssue issue, Session session) {
        session.merge(issue); // do not open/close session here
        return issue;
    }
}

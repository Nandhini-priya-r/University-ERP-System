package org.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.entity.Admin;
import org.hibernate.util.HibernateUtil;

public class AdminDAO extends GenericDAO<Admin> {
    public AdminDAO() {
        super(Admin.class);
    }

    public Admin login(String username, String password) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Admin WHERE username = :user AND password = :pass", Admin.class)
                .setParameter("user", username)
                .setParameter("pass", password)
                .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

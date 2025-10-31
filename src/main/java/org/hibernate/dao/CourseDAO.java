package org.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.entity.Course;
import org.hibernate.util.HibernateUtil;
import java.util.List;

public class CourseDAO {

    public void save(Course course) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(course);
            tx.commit();
        }
    }

    public void update(Course course) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(course);
            tx.commit();
        }
    }

    public void delete(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course != null) session.remove(course);
            tx.commit();
        }
    }

    public Course findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        }
    }

    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course", Course.class).list();
        }
    }

    public List<Course> findByFacultyId(int facultyId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course c where c.faculty.id = :fid", Course.class)
                    .setParameter("fid", facultyId)
                    .list();
        }
    }
}

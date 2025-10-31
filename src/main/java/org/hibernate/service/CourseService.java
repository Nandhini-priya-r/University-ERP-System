package org.hibernate.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dao.CourseDAO;
import org.hibernate.entity.Course;
import org.hibernate.util.HibernateUtil;

import java.util.List;

public class CourseService {

    private final CourseDAO dao = new CourseDAO();

    // ----- CRUD -----
    public boolean addCourse(Course course) {
        try {
            dao.save(course);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateCourse(Course course) {
        try {
            dao.update(course);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Course course = session.get(Course.class, id);
            if (course == null) {
                // ❌ No such course
                return false;
            }

            session.remove(course);
            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }


    public Course getCourseById(int id) {
        return dao.findById(id);
    }

    public List<Course> listAllCourses() {
        return dao.findAll();
    }

    public List<Course> listCoursesByFaculty(int facultyId) {
        return dao.findByFacultyId(facultyId);
    }
}

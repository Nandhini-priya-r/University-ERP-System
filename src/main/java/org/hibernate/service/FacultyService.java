package org.hibernate.service;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dao.FacultyDAO;
import org.hibernate.dao.StudentDAO;
import org.hibernate.entity.Faculty;
import org.hibernate.entity.Student;
import org.hibernate.util.HibernateUtil;

public class FacultyService {
    private final FacultyDAO dao = new FacultyDAO();
    private final StudentDAO studentDao = new StudentDAO();

    // ===== Faculty CRUD =====
    public Faculty create(Faculty f) { return dao.save(f); }
    public Faculty update(Faculty f) { return dao.update(f); }
    public boolean delete(int id) {
        try {
            dao.delete(id);
            System.out.println("🗑️ Faculty deleted successfully (ID: " + id + ")");
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error deleting faculty: " + e.getMessage());
            return false;
        }
    }
    public Faculty get(int id) { return dao.findById(id); }
    public List<Faculty> list() { return dao.findAll(); }
    
    public Faculty findById(int id) {
        return dao.findById(id);
    }

    // ===== Faculty → Department-restricted Student Operations =====
    public boolean updateStudentAttendance(int facultyId, int studentId, double percent) {
        Faculty faculty = dao.findById(facultyId);
        if (faculty == null) return false;

        Student s = studentDao.findById(studentId);
        if (s == null || !s.getDepartment().equalsIgnoreCase(faculty.getDepartment()))
            return false;

        s.setAttendancePercentage(percent);
        studentDao.update(s);
        return true;
    }

    public boolean updateStudentMarks(int facultyId, int studentId, double marks) {
        Faculty faculty = dao.findById(facultyId);
        if (faculty == null) return false;

        Student s = studentDao.findById(studentId);
        if (s == null || !s.getDepartment().equalsIgnoreCase(faculty.getDepartment()))
            return false;

        s.setMarksPercentage(marks);
        studentDao.update(s);
        return true;
    }

    public void updateWorkingStatus(int id, String status) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Faculty f = session.get(Faculty.class, id);
        if (f != null) {
            f.setWorkingStatus(status);
            session.update(f);
            tx.commit();
        } else {
            System.out.println("❌ Faculty not found!");
        }
        session.close();
    }

    // ===== List students of a specific department =====
    public List<Student> listStudentsByDepartment(String department) {
        List<Student> allStudents = studentDao.findAll();
        return allStudents.stream()
                          .filter(s -> s.getDepartment() != null && s.getDepartment().equalsIgnoreCase(department))
                          .toList();
    }
    
    
}

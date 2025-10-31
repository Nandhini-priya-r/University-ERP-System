
package org.hibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

// Import all entity classes here
import org.hibernate.entity.Student;
import org.hibernate.entity.StudentAttendance;
import org.hibernate.entity.StudentMark;
import org.hibernate.entity.Faculty;
import org.hibernate.entity.Fees;
import org.hibernate.entity.Librarian;
import org.hibernate.entity.Library;
import org.hibernate.entity.Member;
import org.hibernate.entity.Admin;
import org.hibernate.entity.BookIssue;
import org.hibernate.entity.Course;
import org.hibernate.entity.Department;
import org.hibernate.entity.Exam;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");

            // Register annotated entity classes
            configuration.addAnnotatedClass(Student.class);
            configuration.addAnnotatedClass(Faculty.class);
            configuration.addAnnotatedClass(Course.class);
            configuration.addAnnotatedClass(Library.class);
            configuration.addAnnotatedClass(Admin.class);
            configuration.addAnnotatedClass(Department.class);
            configuration.addAnnotatedClass(Exam.class);
            configuration.addAnnotatedClass(Fees.class);
            configuration.addAnnotatedClass(StudentAttendance.class);
            configuration.addAnnotatedClass(StudentMark.class);
            configuration.addAnnotatedClass(Member.class);
            configuration.addAnnotatedClass(BookIssue.class);
            configuration.addAnnotatedClass(Librarian.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .build();

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            System.out.println("✅ Hibernate SessionFactory created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ SessionFactory creation failed: " + e.getMessage());
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("🧹 Hibernate SessionFactory closed successfully.");
        }
    }
}

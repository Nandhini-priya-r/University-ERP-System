package org.hibernate.service;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.dao.StudentDAO;
import org.hibernate.entity.Student;
import org.hibernate.util.HibernateUtil;
import org.hibernate.entity.Member;
import org.hibernate.entity.Library;
import org.hibernate.entity.BookIssue;
import org.hibernate.entity.Course;

import java.util.List;

public class StudentService {
    private final StudentDAO dao = new StudentDAO();
    private final LibraryService libraryService = new LibraryService();
    private final BookIssueService issueService = new BookIssueService();
    private final MemberService memberService = new MemberService();

    // ---------------- Existing Student CRUD ----------------
    public boolean addStudent(Student s) {
        try {
            dao.save(s);
            System.out.println("✅ Student added successfully: " + s.getName());
            return true;
        } catch (Exception e) {
            System.err.println("❌ Error adding student: " + e.getMessage());
            return false;
        }
    }

    public List<Student> listAll() {
        try {
            return dao.findAll();
        } catch (Exception e) {
            System.err.println("❌ Error fetching students: " + e.getMessage());
            return List.of();
        }
    }

    public Student findById(int id) {
        return dao.findById(id);
    }

    public boolean updateAttendance(int id, double percent) {
        Student s = dao.findById(id);
        if (s == null) return false;
        s.setAttendancePercentage(percent);
        dao.update(s);
        return true;
    }

    public boolean updateMarks(int id, double marks) {
        Student s = dao.findById(id);
        if (s == null) return false;
        s.setMarksPercentage(marks);
        dao.update(s);
        return true;
    }

    public boolean updateFees(int id, double paid) {
        Student s = dao.findById(id);
        if (s == null) return false;
        s.setFeesPaid(paid);
        dao.update(s);
        return true;
    }

    public boolean updateStudent(Student s) {
        if (s == null) return false;
        dao.update(s);
        return true;
    }

    public boolean delete(int id) {
        Transaction tx = null;
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            // 1️⃣ Fetch student
            Student student = session.get(Student.class, id);
            if (student == null) {
                System.out.println("⚠ Student not found!");
                return false;
            }

            // 2️⃣ Remove student from all enrolled courses (bi-directional)
            for (Course c : student.getCourses()) {
                c.getEnrolledStudents().remove(student);
                session.update(c);  // update course to reflect removal
            }
            student.getCourses().clear(); // clear student's course set

            // 3️⃣ Delete library member linked to student (optional)
            Member member = memberService.findByStudentId(id);
            if (member != null) {
                session.remove(member); // remove library member if exists
            }

            // 4️⃣ Delete student
            session.remove(student);

            tx.commit();
            System.out.println("✅ Student deleted successfully! (ID: " + id + ")");
            return true;

        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            System.err.println("❌ Error deleting student: " + e.getMessage());
            return false;

        } finally {
            if (session != null && session.isOpen()) session.close();
        }
    }



    // ---------------- Library Integration for Students ----------------

    public void viewAvailableBooks() {
        List<Library> books = libraryService.listAllBooks();
        if (books.isEmpty()) {
            System.out.println("📚 No books available in library.");
        } else {
            System.out.println("\n📘 Available Books:");
            books.forEach(b -> System.out.println(b.getId() + " - " + b.getTitle()));
        }
    }

    public void issueBook(int studentId, int bookId) {
        // Find the student first
        Student student = findById(studentId);
        if (student == null) {
            System.out.println("❌ Invalid student ID.");
            return;
        }

        // Try to find an existing library member by name
        List<Member> members = memberService.list();
        Member member = members.stream()
                .filter(m -> m.getName().equalsIgnoreCase(student.getName()))
                .findFirst()
                .orElse(null);

        // If not exists, create a new library member for this student
        if (member == null) {
            System.out.println("ℹ️ Creating library member for student...");
            Member newMember = new Member();
            newMember.setName(student.getName());
            newMember.setRole("student");
            member = memberService.create(newMember);
        }

        // Issue the book using that member’s auto-generated ID
        issueService.issueBook(member.getId(), bookId);
    }


    public void returnBook(int studentId, int bookId) {
        issueService.returnBook(studentId, bookId);
    }
    
 // ✅ Get all students by department
    public List<Student> getStudentsByDepartment(String department) {
        try {
            return dao.findByDepartment(department);
        } catch (Exception e) {
            System.err.println("❌ Error fetching students by department: " + e.getMessage());
            return List.of();
        }
    }


    public void viewMyIssuedBooks(int studentId) {
        List<BookIssue> issues = issueService.listAll().stream()
                .filter(i -> i.getMember().getId() == studentId)
                .toList();

        if (issues.isEmpty()) {
            System.out.println("📖 No books currently issued.");
        } else {
            System.out.println("\n📗 Your Issued Books:");
            for (BookIssue i : issues) {
                System.out.println(
                        "Book: " + i.getBook().getTitle() +
                        " | Issued: " + i.getIssueDate() +
                        " | Due: " + i.getDueDate() +
                        (i.getReturnDate() == null
                                ? " | Not Returned"
                                : " | Returned: " + i.getReturnDate()) +
                        " | Fine: ₹" + i.getFine());
            }
        }
    }
    
 // 🧾 Get Students with Pending Fees
    public List<Student> getStudentsWithPendingFees() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Student WHERE totalFees > feesPaid", Student.class
            ).list();
        }
    }

    // 📉 Get Students with Attendance Below Threshold
    public List<Student> getLowAttendanceStudents(double threshold) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "FROM Student WHERE attendancePercentage < :th", Student.class
            ).setParameter("th", threshold).list();
        }
    }
    
    

}

package org.hibernate.service;

import org.hibernate.dao.BookIssueDAO;
import org.hibernate.dao.LibraryDAO;
import org.hibernate.dao.MemberDAO;
import org.hibernate.entity.BookIssue;
import org.hibernate.entity.Faculty;
import org.hibernate.entity.Librarian;
import org.hibernate.entity.Library;
import org.hibernate.entity.Member;
import org.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;

public class BookIssueService {

    private final BookIssueDAO issueDao = new BookIssueDAO();
    private final MemberDAO memberDao = new MemberDAO();
    private final LibraryDAO libraryDao = new LibraryDAO();
    private final MemberService memberService = new MemberService();

    // 📚 List all book issues
    public List<BookIssue> listAll() {
        return issueDao.findAll();
    }

    // 📖 Issue book — for librarian operation
    public boolean issueBook(int memberId, int bookId, Librarian librarian) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            Member member = memberDao.findById(memberId);
            Library book = libraryDao.findById(bookId);

            if (member == null || book == null) {
                System.out.println("❌ Invalid Member ID or Book ID.");
                tx.rollback();
                return false;
            }

            BookIssue issue = new BookIssue(book, member, LocalDate.now(), LocalDate.now().plusDays(14));
            issue.setLibrarian(librarian);

            issueDao.save(issue);
            tx.commit();

            System.out.println("✅ Book issued to " + member.getName() + " by Librarian: " + librarian.getName());
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("❌ Error issuing book: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    // 🧩 For backward compatibility — old method (no librarian param)
    // ⚠️ Used only by StudentController or test modules (if any)
    public boolean issueBook(int memberId, int bookId) {
        return issueBook(memberId, bookId, new Librarian("System", "N/A", "auto", "auto"));
    }

    // 🔁 Return book — for librarian operation
    public boolean returnBook(int memberId, int bookId, Librarian librarian) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {
            BookIssue issue = session.createQuery(
                    "from BookIssue where member.id = :memberId and book.id = :bookId and returnDate is null",
                    BookIssue.class)
                    .setParameter("memberId", memberId)
                    .setParameter("bookId", bookId)
                    .uniqueResult();

            if (issue == null) {
                System.out.println("❌ No active issue found for this member/book.");
                tx.rollback();
                return false;
            }

            issue.setReturnDate(LocalDate.now());
            long delayDays = java.time.temporal.ChronoUnit.DAYS.between(issue.getDueDate(), issue.getReturnDate());
            issue.setFine(delayDays > 0 ? delayDays * 5 : 0);

            issueDao.update(issue);
            tx.commit();

            System.out.println("✅ Book returned by " + librarian.getName() + ". Fine: ₹" + issue.getFine());
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.out.println("❌ Error returning book: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            session.close();
        }
    }

    // 🧩 Backward compatible version — for any old call without librarian
    public boolean returnBook(int memberId, int bookId) {
        return returnBook(memberId, bookId, new Librarian("System", "N/A", "auto", "auto"));
    }

    // 🧩 Used when student needs library member record auto-created
    public Member ensureStudentMembership(int studentId, String studentName) {
        return memberService.createLibraryAccessForStudent(studentId, studentName);
    }
    
    public boolean issueBookForStudent(int studentId, String studentName, int bookId, Librarian librarian) {
        // Step 1: Check if the student already has a Member record
        Member member = memberDao.findByStudentId(studentId);
        if (member == null) {
            // Create a Member for the student
            member = new Member(studentName, "student"); // adjust constructor if needed
            memberDao.save(member);
        }

        // Step 2: Fetch the Book
        Library book = libraryDao.findById(bookId);
        if (book == null) {
            System.out.println("❌ Book not found.");
            return false;
        }
        // Step 3: Create BookIssue
        BookIssue issue = new BookIssue();
        issue.setMember(member);
        issue.setBook(book);
        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(LocalDate.now().plusDays(14));
        issue.setLibrarian(librarian);

        // Step 4: Save issue
        issueDao.save(issue);

        System.out.println("✅ Book issued to " + member.getName() + " by Librarian: " + librarian.getName());
        return true;
    }

 // Return book for a student by studentId
    public boolean returnBookForStudent(int studentId, int bookId) {
        // Step 1: Find the Member corresponding to the student
        Member member = memberDao.findByStudentId(studentId);
        if (member == null) {
            System.out.println("❌ No library member record found for this student.");
            return false;
        }

        // Step 2: Use the existing returnBook method for members
        return returnBook(member.getId(), bookId);
    }

 // Faculty issues a book (like student)


 // Faculty issues a book safely
 // Faculty issues a book safely, auto-creating Member if missing
    public boolean issueBookByFaculty(int facultyId, int bookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Step 1: Get or create Member for faculty
            Member member = session.createQuery("from Member where linkedFacultyId = :fid", Member.class)
                                   .setParameter("fid", facultyId)
                                   .uniqueResult();
            if (member == null) {
                // Fetch Faculty info to create Member
                Faculty faculty = session.get(Faculty.class, facultyId);
                if (faculty == null) {
                    System.out.println("❌ Faculty not found!");
                    tx.rollback();
                    return false;
                }

                member = new Member();
                member.setName(faculty.getName());
                member.setRole("faculty");
                member.setLinkedFacultyId(facultyId);

                session.persist(member);
                System.out.println("🆕 Library membership created for " + faculty.getName());
            }

            // Step 2: Fetch the Book
            Library book = session.get(Library.class, bookId);
            if (book == null) {
                System.out.println("❌ Book not found!");
                tx.rollback();
                return false;
            }

            // Step 3: Create BookIssue
            BookIssue issue = new BookIssue();
            issue.setMember(member);
            issue.setBook(book);
            issue.setIssueDate(LocalDate.now());
            issue.setDueDate(LocalDate.now().plusDays(14));

            // Assign any librarian
            Librarian librarian = getAnyLibrarian(session);
            issue.setLibrarian(librarian);

            session.persist(issue);
            tx.commit();

            System.out.println("✅ Faculty " + member.getName() + " issued book " + book.getTitle() +
                               " via Librarian " + librarian.getName());
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


 // Faculty returns a book safely
    public boolean returnBookByFaculty(int facultyMemberId, int bookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Get latest active issue (avoids NonUniqueResultException)
            BookIssue issue = session.createQuery(
                    "from BookIssue where member.id = :memberId and book.id = :bookId and returnDate is null order by issueDate desc",
                    BookIssue.class)
                    .setParameter("memberId", facultyMemberId)
                    .setParameter("bookId", bookId)
                    .setMaxResults(1)
                    .uniqueResult();

            if (issue == null) {
                System.out.println("❌ No active issue found for this faculty/book.");
                tx.rollback();
                return false;
            }

            Librarian librarian = getAnyLibrarian(session);

            issue.setReturnDate(LocalDate.now());
            long delayDays = java.time.temporal.ChronoUnit.DAYS.between(issue.getDueDate(), issue.getReturnDate());
            issue.setFine(delayDays > 0 ? delayDays * 5 : 0);
            issue.setLibrarian(librarian);

            session.merge(issue);
            tx.commit();

            System.out.println("✅ Book returned by " + librarian.getName() +
                    ". Fine: ₹" + issue.getFine());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

 // Helper: get any existing librarian, or create one if none exist
 private Librarian getAnyLibrarian(Session session) {
     List<Librarian> list = session.createQuery("from Librarian", Librarian.class).list();
     if (!list.isEmpty()) return list.get(0);

     // Create default librarian
     Librarian defaultLib = new Librarian();
     defaultLib.setName("Default Librarian");
     defaultLib.setContact("000");
     defaultLib.setUsername("default");
     defaultLib.setPassword("1234");
     session.persist(defaultLib); // managed in the current session
     return defaultLib;
 }
 public void issueBookToMember(Member member, Library book) {
     Session session = HibernateUtil.getSessionFactory().openSession();
     Transaction tx = null;

     try {
         tx = session.beginTransaction();

         BookIssue issue = new BookIssue();
         issue.setMember(member);
         issue.setBook(book);
         issue.setIssueDate(LocalDate.now());

         // Auto-generate due date based on role
         if(member.getRole().equalsIgnoreCase("student")) {
             issue.setDueDate(LocalDate.now().plusDays(7)); // students: 7 days
         } else if(member.getRole().equalsIgnoreCase("faculty")) {
             issue.setDueDate(LocalDate.now().plusDays(14)); // faculty: 14 days
         }

         session.save(issue);
         tx.commit();

         System.out.println("✅ Book issued to " + member.getName() +
                            ". Due date: " + issue.getDueDate());

     } catch (Exception e) {
         if(tx != null) tx.rollback();
         e.printStackTrace();
     } finally {
         session.close();
     }
 }
//Fetch the last issued book for a member (to display due date)
public BookIssue getLatestIssueForMember(int memberId, int bookId) {
  try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery(
              "from BookIssue where member.id = :memberId and book.id = :bookId order by issueDate desc",
              BookIssue.class)
              .setParameter("memberId", memberId)
              .setParameter("bookId", bookId)
              .setMaxResults(1)
              .uniqueResult();
  }
}

//✅ Fetch the last issued book for a faculty member to show due date
public BookIssue getLatestIssueForFaculty(int facultyMemberId, int bookId) {
 try (Session session = HibernateUtil.getSessionFactory().openSession()) {
     return session.createQuery(
             "from BookIssue where member.id = :memberId and book.id = :bookId order by issueDate desc",
             BookIssue.class)
             .setParameter("memberId", facultyMemberId)
             .setParameter("bookId", bookId)
             .setMaxResults(1)
             .uniqueResult();
 }
}
public void deleteIssue(int id) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
        Transaction tx = session.beginTransaction();

        BookIssue issue = session.get(BookIssue.class, id);
        if (issue != null) {
            // If a book was issued, return the copy
            if (issue.getBook() != null) {
                issue.getBook().setAvailableCopies(issue.getBook().getAvailableCopies() + 1);
                session.merge(issue.getBook());
            }
            session.remove(issue);
            System.out.println("🗑️ Book Issue deleted successfully (ID: " + id + ")");
        } else {
            System.out.println("⚠️ No Book Issue found with ID: " + id);
        }

        tx.commit();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


}

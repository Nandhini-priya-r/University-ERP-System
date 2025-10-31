package org.hibernate.controller;

import java.util.List;
import org.hibernate.entity.Student;
import org.hibernate.entity.BookIssue;
import org.hibernate.entity.Librarian;
import org.hibernate.entity.Member;
import org.hibernate.service.StudentService;
import org.hibernate.service.MemberService;
import org.hibernate.service.BookIssueService;
import org.hibernate.util.InputUtil;

public class StudentController {

    private final StudentService service = new StudentService();
    private final MemberService memberService = new MemberService();
    private final BookIssueService bookIssueService = new BookIssueService();

    // --- Students can only view ---
    public void viewProfile() {
        System.out.println("\n--- View Profile ---");
        int id = InputUtil.nextInt("Enter Student ID: ");
        Student s = service.findById(id);
        if (s != null) {
            System.out.println(s);
        } else {
            System.out.println("❌ Student not found.");
        }
    }

    public void viewAttendanceAndMarks() {
        System.out.println("\n--- Attendance & Marks ---");
        int id = InputUtil.nextInt("Enter Student ID: ");
        Student s = service.findById(id);
        if (s != null) {
            System.out.println("Attendance: " + s.getAttendancePercentage() + "%");
            System.out.println("Marks: " + s.getMarksPercentage() + "%");
        } else {
            System.out.println("❌ Student not found.");
        }
    }

    // --- Admin / Faculty functions ---
    public void addStudent() {
        System.out.println("\n--- Add New Student ---");
        Student s = new Student();
        s.setName(InputUtil.nextLine("Name: "));
        s.setRollNo(InputUtil.nextLine("Roll No: "));
        s.setCourse(InputUtil.nextLine("Course: "));
        s.setDepartment(InputUtil.nextLine("Department: "));
        s.setContact(InputUtil.nextLine("Contact: "));
        s.setAddress(InputUtil.nextLine("Address: "));
        s.setAdmissionYear(InputUtil.nextLine("Admission Year: "));
        s.setEnrollmentStatus("Active");
        s.setAttendancePercentage(0);
        s.setMarksPercentage(0);
        s.setTotalFees(InputUtil.nextDouble("Total Fees: "));
        s.setFeesPaid(InputUtil.nextDouble("Fees Paid: "));
        s.setHostelStatus(InputUtil.nextLine("Hostel (YES/NO): "));
        s.setLibraryCardNo(InputUtil.nextLine("Library Card No: "));

        if (service.addStudent(s)) System.out.println("✅ Student added successfully!");
        else System.out.println("❌ Failed to add student.");
    }

    public void listStudents() {
        System.out.println("\n--- Student List ---");
        List<Student> students = service.listAll();
        for (Student s : students) {
            System.out.println(s);
        }
    }

    public void updateAttendance() {
        System.out.println("\n--- Update Attendance (Faculty Only) ---");
        int id = InputUtil.nextInt("Student ID: ");
        double percent = InputUtil.nextDouble("Attendance %: ");
        if (service.updateAttendance(id, percent))
            System.out.println("✅ Attendance updated!");
        else System.out.println("❌ Student not found.");
    }

    public void updateMarks() {
        System.out.println("\n--- Update Marks (Faculty Only) ---");
        int id = InputUtil.nextInt("Student ID: ");
        double marks = InputUtil.nextDouble("Marks %: ");
        if (service.updateMarks(id, marks))
            System.out.println("✅ Marks updated!");
        else System.out.println("❌ Student not found.");
    }

    public void deleteStudent() {
        System.out.println("\n--- Delete Student ---");
        int id = InputUtil.nextInt("Student ID: ");
        
        Student s = service.findById(id);
        if (s == null) {
            System.out.println("❌ Student not found.");
            return;
        }

        System.out.println("⚠ You are about to delete: " + s.getName() + " (ID: " + id + ")");
        String confirm = InputUtil.nextLine("Type 'YES' to confirm deletion: ");
        
        if (confirm.equalsIgnoreCase("YES")) {
            if (service.delete(id)) {
                System.out.println("✅ Student deleted successfully!");
            } else {
                System.out.println("❌ Failed to delete student.");
            }
        } else {
            System.out.println("❌ Deletion cancelled.");
        }
    }


    public void showStudentCourses(int studentId) {
        Student s = service.findById(studentId);
        if (s == null) {
            System.out.println("❌ Student not found!");
            return;
        }
        System.out.println("\nCourses enrolled by " + s.getName() + ":");
        s.getCourses().forEach(c -> System.out.println("- " + c.getCourseName()));
    }

    // ============================
    // ✅ NEW: Library Access for Students
    // ============================

   
    public void issueBook() {
        System.out.println("\n--- Issue Book (Student) ---");
        
        int studentId = InputUtil.nextInt("Enter your Student ID: ");
        Student s = service.findById(studentId);
        if (s == null) {
            System.out.println("❌ Student not found!");
            return;
        }

        // Create or find Member record
        Member m = memberService.findByStudentId(studentId);
        if (m == null) {
            m = new Member();
            m.setName(s.getName());
            m.setRole("student");
            m.setLinkedStudentId(studentId);
            memberService.create(m);
            System.out.println("🆕 Library membership created for " + s.getName());
        }

        int bookId = InputUtil.nextInt("Enter Book ID to issue: ");
        boolean success = bookIssueService.issueBookForStudent(studentId, s.getName(), bookId, new Librarian("System", "N/A", "auto", "auto"));

        if (success) {
            BookIssue issued = bookIssueService.getLatestIssueForMember(m.getId(), bookId); // helper method to fetch last issued
            System.out.println("✅ Book issued successfully! Due Date: " + issued.getDueDate());
        } else {
            System.out.println("❌ Failed to issue book (maybe unavailable).");
        }
    }


    public void returnBook() {
        System.out.println("\n--- Return Book (Student) ---");
        int studentId = InputUtil.nextInt("Enter your Student ID: ");
        Member m = memberService.findByStudentId(studentId);
        if (m == null) {
            System.out.println("❌ You are not a library member.");
            return;
        }
        int bookId = InputUtil.nextInt("Enter Book ID to return: ");
        if (bookIssueService.returnBook(m.getId(), bookId)) {
            System.out.println("✅ Book returned successfully!");
        } else {
            System.out.println("❌ Book return failed (check book ID).");
        }
    }
    
 // ============================
 // ✅ Student Fees Payment
 // ============================
 public void payFees() {
     System.out.println("\n--- Pay Fees (Student) ---");
     int studentId = InputUtil.nextInt("Enter your Student ID: ");
     Student s = service.findById(studentId);

     if (s == null) {
         System.out.println("❌ Student not found!");
         return;
     }

     double remaining = s.getTotalFees() - s.getFeesPaid();
     if (remaining <= 0) {
         System.out.println("🎉 You have already paid all your fees!");
         return;
     }

     System.out.println("Total Fees: ₹" + s.getTotalFees());
     System.out.println("Fees Paid: ₹" + s.getFeesPaid());
     System.out.println("Remaining Balance: ₹" + remaining);

     double amount = InputUtil.nextDouble("Enter amount to pay: ");
     if (amount <= 0) {
         System.out.println("❌ Invalid amount entered.");
         return;
     }

     if (amount > remaining) {
         System.out.println("❌ Amount exceeds remaining balance! Please enter a valid amount.");
         return;
     }

     double newPaid = s.getFeesPaid() + amount;

     // Update student fees in database
     if (service.updateFees(s.getId(), newPaid)) {
         System.out.println("✅ Payment successful!");
         System.out.println("Updated Fees Paid: ₹" + newPaid);
         System.out.println("Remaining Balance: ₹" + (s.getTotalFees() - newPaid));
     } else {
         System.out.println("❌ Failed to update fees.");
     }
 }

}

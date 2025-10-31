package org.hibernate.controller;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.entity.BookIssue;
import org.hibernate.entity.Faculty;
import org.hibernate.entity.Member;
import org.hibernate.entity.Student;
import org.hibernate.service.BookIssueService;
import org.hibernate.service.FacultyService;
import org.hibernate.service.MemberService;
import org.hibernate.util.InputUtil;

public class FacultyController {

    private final FacultyService service = new FacultyService();
    private final MemberService memberService = new MemberService();
    private final BookIssueService bookIssueService = new BookIssueService();

   // private Faculty loggedInFaculty = null;

    // ----- Faculty CRUD -----
    public Faculty add(String name, String dept, String qualification, String contact, String specialization) {
        Faculty f = new Faculty(name, dept, qualification, contact, specialization);
        return service.create(f);
    }

    public Faculty update(int id, String dept, String qualification, String contact, String specialization) {
        Faculty existing = service.get(id);
        if (existing == null) {
            System.out.println("❌ Faculty not found.");
            return null;
        }
        existing.setDepartment(dept);
        existing.setQualification(qualification);
        existing.setContact(contact);
        existing.setSpecialization(specialization);
        return service.update(existing);
    }

    public void delete(int id) { service.delete(id); }
    public List<Faculty> list() { return service.list(); }
    public Faculty get(int id) { return service.get(id); }

    // ===== Faculty Login =====
 

    // ===== Faculty Menu (Console Mode) =====
 // ===== Faculty Menu (Console Mode) =====
    public void menu(String role) {
        while (true) {
            System.out.println("\n-- Faculty Menu --");
            System.out.println("1) Add Faculty");
            System.out.println("2) List Faculty");
            System.out.println("3) Update Faculty");

            if (role.equalsIgnoreCase("admin")) {
                System.out.println("4) Delete Faculty");
                System.out.println("5) Update Faculty Working Status");
                System.out.println("6) Manage Student Attendance");
                System.out.println("7) Manage Student Marks");
                System.out.println("8) View Updated Students");
                System.out.println("9) Back");
            } else { // Faculty role
                System.out.println("4) Manage Student Attendance");
                System.out.println("5) Manage Student Marks");
                System.out.println("6) View Updated Students");
                System.out.println("7) Back");
            }

            int opt = InputUtil.nextInt("> ");

            switch (opt) {
                // ---- Common actions ----
                case 1 -> {
                    String name = InputUtil.nextLine("Name: ");
                    String dept = InputUtil.nextLine("Department: ");
                    String qual = InputUtil.nextLine("Qualification: ");
                    String contact = InputUtil.nextLine("Contact: ");
                    String spec = InputUtil.nextLine("Specialization: ");
                    System.out.println("✅ Added: " + add(name, dept, qual, contact, spec));
                }
                case 2 -> list().forEach(System.out::println);
                case 3 -> {
                    int id = InputUtil.nextInt("Faculty ID to update: ");
                    String dept = InputUtil.nextLine("New Department: ");
                    String qual = InputUtil.nextLine("New Qualification: ");
                    String contact = InputUtil.nextLine("New Contact: ");
                    String spec = InputUtil.nextLine("New Specialization: ");
                    System.out.println("✅ Updated: " + update(id, dept, qual, contact, spec));
                }

                // ---- Admin-only actions ----
                case 4 -> {
                    if (role.equalsIgnoreCase("admin")) {
                        delete(InputUtil.nextInt("Faculty ID to delete: "));
                        System.out.println("✅ Deleted (if existed).");
                    } else {
                        manageAttendance(); // faculty
                    }
                }
                case 5 -> {
                    if (role.equalsIgnoreCase("admin")) updateFacultyWorkingStatus();
                    else manageMarks(); // faculty
                }
                case 6 -> {
                    if (role.equalsIgnoreCase("admin")) manageAttendance();
                    else viewUpdatedStudents(); // faculty
                }
                case 7 -> {
                    if (role.equalsIgnoreCase("admin")) manageMarks();
                    else { System.out.println("⬅ Returning to Main Menu..."); return; }
                }
                case 8 -> {
                    if (role.equalsIgnoreCase("admin")) viewUpdatedStudents();
                    else System.out.println("⚠ Invalid option.");
                }
                case 9 -> {
                    if (role.equalsIgnoreCase("admin")) { System.out.println("⬅ Returning to Main Menu..."); return; }
                    else System.out.println("⚠ Invalid option.");
                }

                default -> System.out.println("⚠ Invalid option. Try again.");
            }
        }
    }

    // ===== Student Operations restricted by Department =====
    private void manageAttendance() {
        int facultyId = InputUtil.nextInt("Enter your Faculty ID: ");
        Faculty faculty = service.get(facultyId);
        if (faculty == null) {
            System.out.println("❌ Invalid Faculty ID!");
            return;
        }

        List<Student> students = service.listStudentsByDepartment(faculty.getDepartment());
        if (students.isEmpty()) {
            System.out.println("❌ No students found in department: " + faculty.getDepartment());
            return;
        }

        System.out.println("\n-- Students in " + faculty.getDepartment() + " Department --");
        for (Student s : students) {
            System.out.println(s.getId() + ") " + s.getName() + " | Attendance: " + s.getAttendancePercentage() + "%");
        }

        int studentId = InputUtil.nextInt("Select Student ID to update attendance: ");
        double percent = InputUtil.nextDouble("Enter new Attendance %: ");

        if (service.updateStudentAttendance(facultyId, studentId, percent))
            System.out.println("✅ Attendance updated successfully!");
        else
            System.out.println("❌ Update failed (student not in your department).");
    }
    
    public void updateFacultyWorkingStatus() {
        int id = InputUtil.nextInt("Enter Faculty ID: ");
        String status = InputUtil.nextLine("Enter new working status (Active/Inactive): ");
        service.updateWorkingStatus(id, status);
        System.out.println("✅ Faculty working status updated successfully!");
    }


    private void manageMarks() {
        int facultyId = InputUtil.nextInt("Enter your Faculty ID: ");
        Faculty faculty = service.get(facultyId);
        if (faculty == null) {
            System.out.println("❌ Invalid Faculty ID!");
            return;
        }

        List<Student> students = service.listStudentsByDepartment(faculty.getDepartment());
        if (students.isEmpty()) {
            System.out.println("❌ No students found in department: " + faculty.getDepartment());
            return;
        }

        System.out.println("\n-- Students in " + faculty.getDepartment() + " Department --");
        for (Student s : students) {
            System.out.println(s.getId() + ") " + s.getName() + " | Marks: " + s.getMarksPercentage() + "%");
        }

        int studentId = InputUtil.nextInt("Select Student ID to update marks: ");
        double marks = InputUtil.nextDouble("Enter new Marks %: ");

        if (service.updateStudentMarks(facultyId, studentId, marks))
            System.out.println("✅ Marks updated successfully!");
        else
            System.out.println("❌ Update failed (student not in your department).");
    }
    
    public void issueBook() {
        System.out.println("\n--- Issue Book (Faculty) ---");

        int facultyId = InputUtil.nextInt("Enter your Faculty ID: ");
        Faculty f = service.get(facultyId);
        if (f == null) {
            System.out.println("❌ Faculty not found!");
            return;
        }

        // Check if faculty has a Member record in library
        Member m = memberService.findByFacultyId(facultyId);
        if (m == null) {
            m = new Member();
            m.setName(f.getName());
            m.setRole("faculty");
            m.setLinkedFacultyId(facultyId);
            memberService.create(m);
            System.out.println("🆕 Library membership created for " + f.getName());
        }

        int bookId = InputUtil.nextInt("Enter Book ID to issue: ");

        boolean success = bookIssueService.issueBookByFaculty(m.getId(), bookId);

        if (success) {
            // Fetch the latest issue to display due date
           // BookIssue issued = bookIssueService.getLatestIssueForMember(m.getId(), bookId);
            BookIssue issued = bookIssueService.getLatestIssueForFaculty(m.getId(), bookId);

            if (issued != null) {
                System.out.println("✅ Book issued successfully! Due Date: " + issued.getDueDate());
            } else {
                System.out.println("⚠ Book issued, but could not fetch due date.");
            }
        } else {
            System.out.println("❌ Failed to issue book (maybe unavailable).");
        }
    }

    private void viewUpdatedStudents() {
        int facultyId = InputUtil.nextInt("Enter your Faculty ID: ");
        Faculty f = service.get(facultyId);
        if (f == null) {
            System.out.println("❌ Invalid Faculty ID!");
            return;
        }

        List<Student> students = service.listStudentsByDepartment(f.getDepartment());
        if (students.isEmpty()) {
            System.out.println("❌ No students found in your department.");
            return;
        }

        System.out.println("\n-- Recently Updated Students --");
        for (Student s : students) {
            String attendanceUpdate = s.getLastUpdatedAttendance() != null ? s.getLastUpdatedAttendance().toString() : "N/A";
            String marksUpdate = s.getLastUpdatedMarks() != null ? s.getLastUpdatedMarks().toString() : "N/A";

            System.out.println(s.getId() + ") " + s.getName() +
                               " | Attendance: " + s.getAttendancePercentage() + "% (Last Updated: " + attendanceUpdate + ")" +
                               " | Marks: " + s.getMarksPercentage() + "% (Last Updated: " + marksUpdate + ")");
        }
    }


}
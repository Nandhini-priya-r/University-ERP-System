package org.hiberante.mainapp;

import org.hibernate.controller.*;
import org.hibernate.util.HibernateUtil;
import org.hibernate.util.InputUtil;

public class App {
    public static void main(String[] args) {

        StudentController studentCtrl = new StudentController();
        FacultyController facultyCtrl = new FacultyController();
        CourseController courseCtrl = new CourseController();
        LibraryController libraryCtrl = new LibraryController();
        MemberController memberCtrl = new MemberController();
        BookIssueController bookIssueCtrl = new BookIssueController();
        LibrarianController librarianCtrl = new LibrarianController();
        AdminController adminCtrl = new AdminController();

        System.out.println("University ERP System - CLI (MySQL)");

        while (true) {
            String role = InputUtil.nextLine("\nEnter your role (student/faculty/admin/librarian or 'exit' to quit): ")
                                   .trim().toLowerCase();

            if (role.equals("exit")) {
                System.out.println("Shutting down...");
                HibernateUtil.shutdown();
                return;
            }

            switch (role) {
                case "student" -> studentRoleMenu(studentCtrl, libraryCtrl, memberCtrl, bookIssueCtrl);
                case "faculty", "admin" -> facultyAdminRoleMenu(role, studentCtrl, facultyCtrl, courseCtrl,
                        libraryCtrl, memberCtrl, bookIssueCtrl, librarianCtrl, adminCtrl);
                case "librarian" -> librarianCtrl.menu(); // direct librarian menu
                default -> System.out.println("❌ Invalid role. Try again.");
            }
        }
    }

    // ------------------------ Student Role Menu ------------------------
    private static void studentRoleMenu(StudentController studentCtrl, LibraryController libraryCtrl,
                                        MemberController memberCtrl, BookIssueController bookIssueCtrl) {
        while (true) {
            System.out.println("\n=== Student Menu ===");
            System.out.println("1) View Profile");
            System.out.println("2) View Attendance & Marks");
            System.out.println("3) Access Library");
            System.out.println("4) Pay Fees");
            System.out.println("5) Back");

            int opt = InputUtil.nextInt("> ");
            switch (opt) {
                case 1 -> studentCtrl.viewProfile();
                case 2 -> studentCtrl.viewAttendanceAndMarks();
                case 3 -> studentLibraryMenu(studentCtrl, libraryCtrl, memberCtrl, bookIssueCtrl);
                case 4 -> studentCtrl.payFees();
                case 5 -> { return; } // go back to role selection
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ------------------------ Faculty/Admin Role Menu ------------------------
    private static void facultyAdminRoleMenu(String role, StudentController studentCtrl, FacultyController facultyCtrl,
                                             CourseController courseCtrl, LibraryController libraryCtrl,
                                             MemberController memberCtrl, BookIssueController bookIssueCtrl,
                                             LibrarianController librarianCtrl, AdminController adminCtrl) {
        while (true) {
            System.out.println("\n=== " + role.toUpperCase() + " Menu ===");
            System.out.println("1) Student Operations");
            System.out.println("2) Faculty Operations");
            System.out.println("3) Course Operations");
            System.out.println("4) Library Operations");
            System.out.println("5) Admin Reports");
            System.out.println("6) Back");

            int opt = InputUtil.nextInt("> ");
            switch (opt) {
                case 1 -> studentMenu(studentCtrl);
                case 2 -> facultyCtrl.menu(role);
                case 3 -> courseMenu(courseCtrl);
                case 4 -> libraryMenu(libraryCtrl, memberCtrl, bookIssueCtrl, librarianCtrl, role);
                case 5 -> adminMenu(adminCtrl);
                case 6 -> { return; } // back to main role selection
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ------------------------ Library Menu ------------------------
    private static void libraryMenu(LibraryController libraryCtrl, MemberController memberCtrl,
                                    BookIssueController bookIssueCtrl, LibrarianController librarianCtrl,
                                    String role) {
        while (true) {
            System.out.println("\n-- Library Menu --");

            if (role.equals("faculty")) {
                System.out.println("1) List Books");
                System.out.println("2) Issue Book");
                System.out.println("3) Return Book");
                System.out.println("4) List Members");
                System.out.println("5) Add Member");
                System.out.println("6) Back");

                int opt = InputUtil.nextInt("> ");
                switch (opt) {
                    case 1 -> libraryCtrl.listBooks();
                    case 2 -> bookIssueCtrl.issueBookByFaculty();
                    case 3 -> bookIssueCtrl.returnBookByFaculty();
                    case 4 -> memberCtrl.listMembers();
                    case 5 -> memberCtrl.addMember();
                    case 6 -> { return; } // back to role menu
                    default -> System.out.println("Invalid option.");
                }

            } else if (role.equals("librarian") || role.equals("admin")) {
                System.out.println("1) Add Book");
                System.out.println("2) List Books");
                System.out.println("3) Delete Book");
                System.out.println("4) Issue Book");
                System.out.println("5) Return Book");
                System.out.println("6) List Members");
                System.out.println("7) Add Member");
                System.out.println("8) Librarian Menu");
                System.out.println("9) Back");

                int opt = InputUtil.nextInt("> ");
                switch (opt) {
                    case 1 -> libraryCtrl.addBook();
                    case 2 -> libraryCtrl.listBooks();
                    case 3 -> libraryCtrl.deleteBook();
                    case 4 -> bookIssueCtrl.issueBookWithLibrarian();
                    case 5 -> bookIssueCtrl.returnBookWithLibrarian();
                    case 6 -> memberCtrl.listMembers();
                    case 7 -> memberCtrl.addMember();
                    case 8 -> librarianCtrl.menu();
                    case 9 -> { return; } // back to role menu
                    default -> System.out.println("Invalid option.");
                }
            }
        }
    }

    // ------------------------ Student Library Menu ------------------------
    private static void studentLibraryMenu(StudentController studentCtrl, LibraryController libraryCtrl,
                                           MemberController memberCtrl, BookIssueController bookIssueCtrl) {
        while (true) {
            System.out.println("\n-- Library Access (Student) --");
            System.out.println("1) View Available Books");
            System.out.println("2) Get Book");
            System.out.println("3) Return Book");
            System.out.println("4) Back");

            int opt = InputUtil.nextInt("> ");
            switch (opt) {
                case 1 -> libraryCtrl.listBooks();
                case 2 -> studentCtrl.issueBook();
                case 3 -> studentCtrl.returnBook();
                case 4 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ------------------------ Student Menu ------------------------
    private static void studentMenu(StudentController ctrl) {
        while (true) {
            System.out.println("\n-- Student Operations (Faculty/Admin) --");
            System.out.println("1) Add Student");
            System.out.println("2) List Students");
            System.out.println("3) Update Attendance");
            System.out.println("4) Update Marks");
            System.out.println("5) Delete Student");
            System.out.println("6) Back");
            int opt = InputUtil.nextInt("> ");
            switch (opt) {
                case 1 -> ctrl.addStudent();
                case 2 -> ctrl.listStudents();
                case 3 -> ctrl.updateAttendance();
                case 4 -> ctrl.updateMarks();
                case 5 -> ctrl.deleteStudent();
                case 6 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ------------------------ Course Menu ------------------------
    private static void courseMenu(CourseController ctrl) {
        while (true) {
            System.out.println("\n-- Course Menu --");
            System.out.println("1) Add / Update Course");
            System.out.println("2) List Courses");
            System.out.println("3) Enroll Student");
            System.out.println("4) Delete Course");
            System.out.println("5) Back");
            int opt = InputUtil.nextInt("> ");
            switch (opt) {
                case 1 -> ctrl.addOrUpdateCourse();
                case 2 -> ctrl.listCourses();
                case 3 -> ctrl.enrollStudent();
                case 4 -> {
                    int id = InputUtil.nextInt("Enter Course ID to delete: ");
                    if (ctrl.deleteCourse(id)) System.out.println("✅ Course deleted successfully!");
                    else System.out.println("❌ Course not found or deletion failed.");
                }
                case 5 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // ------------------------ Admin Menu ------------------------
    private static void adminMenu(AdminController adminCtrl) {
        while (true) {
            System.out.println("\n-- Admin Dashboard --");
            System.out.println("1) Login");
            System.out.println("2) View Summary Dashboard");
            System.out.println("3) Remove Any Entity (Admin Power)");
            System.out.println("4) View Reports"); // ✅ new option
            System.out.println("5) Logout");
            System.out.println("6) Back");

            int opt = InputUtil.nextInt("> ");
            switch (opt) {
                case 1 -> adminCtrl.login();
                case 2 -> adminCtrl.viewSummaryDashboard();
                case 3 -> adminCtrl.removeAnyEntity();
                case 4 -> adminCtrl.reportsMenu();  // ✅ new reports menu method
                case 5 -> adminCtrl.logout();
                case 6 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }

}

package org.hibernate.controller;

import java.util.Properties;
import java.util.Scanner;

import org.hibernate.entity.*;
import org.hibernate.service.*;
import org.hibernate.util.HibernateUtil;

public class AdminController {

    private final StudentService studentService = new StudentService();
    private final CourseService courseService = new CourseService();
    private final FacultyService facultyService = new FacultyService();
    private final LibraryService libraryService = new LibraryService();
    private final BookIssueService bookIssueService = new BookIssueService();

    private boolean isLoggedIn = false;
    private String loggedInUser;
    private final Scanner sc = new Scanner(System.in);

    // 🔐 Admin Login using db.properties credentials
    public boolean login() {
        try {
            System.out.print("Enter Admin Username: ");
            String username = sc.nextLine().trim();
            System.out.print("Enter Admin Password: ");
            String password = sc.nextLine().trim();

            Properties props = new Properties();
            props.load(getClass().getClassLoader().getResourceAsStream("db.properties"));

            String dbUser = props.getProperty("hibernate.connection.username");
            String dbPass = props.getProperty("hibernate.connection.password");

            if (username.equals(dbUser) && password.equals(dbPass)) {
                isLoggedIn = true;
                loggedInUser = username;
                System.out.println("✅ Login successful! Welcome, " + username);
                return true;
            } else {
                System.out.println("❌ Invalid Admin credentials. Try again.");
                return false;
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error reading database credentials: " + e.getMessage());
            return false;
        }
    }

    public void logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
            loggedInUser = null;
            System.out.println("👋 Logged out successfully!");
        } else {
            System.out.println("⚠️ No admin logged in currently.");
        }
    }

    // 📊 Summary Dashboard
    public void viewSummaryDashboard() {
        if (!isLoggedIn) {
            System.out.println("⚠️ Please login first!");
            return;
        }

        System.out.println("\n📊 ====== University Summary ======");
        int totalStudents = studentService.listAll().size();
        int totalFaculty = facultyService.list().size();
        int totalCourses = courseService.listAllCourses().size();
        int totalBooks = libraryService.listAllBooks().size();
        int totalBookIssues = bookIssueService.listAll().size();

        long enrolledStudents = studentService.listAll().stream()
                .filter(s -> s.getCourse() != null)
                .count();

        System.out.println("👨‍🎓 Total Students       : " + totalStudents);
        System.out.println("🎓 Enrolled Students     : " + enrolledStudents);
        System.out.println("👩‍🏫 Total Faculty        : " + totalFaculty);
        System.out.println("📘 Total Courses         : " + totalCourses);
        System.out.println("📚 Total Books           : " + totalBooks);
        System.out.println("📦 Active Book Issues    : " + totalBookIssues);
        System.out.println("================================================\n");
    }

    // 🗑️ Remove any entity (Admin power)
    public void removeAnyEntity() {
        if (!isLoggedIn) {
            System.out.println("⚠️ Please login first!");
            return;
        }

        System.out.println("\n-- Remove Entity Menu --");
        System.out.println("1) Remove Student");
        System.out.println("2) Remove Faculty");
        System.out.println("3) Remove Course");
        System.out.println("4) Remove Book");
        System.out.println("5) Remove Book Issue");
        System.out.print("> ");

        int choice = sc.nextInt();
        sc.nextLine(); // consume newline

        switch (choice) {
            case 1 -> {
                System.out.print("Enter Student ID to remove: ");
                int id = sc.nextInt();
                Student s = studentService.findById(id);
                if (s == null) {
                    System.out.println("❌ No student found with ID: " + id);
                } else {
                    studentService.delete(id);
                    System.out.println("🗑️ Student removed successfully (ID: " + id + ")");
                }
            }
            case 2 -> {
                System.out.print("Enter Faculty ID to remove: ");
                int id = sc.nextInt();
                Faculty f = facultyService.findById(id);
                if (f == null) {
                    System.out.println("❌ No faculty found with ID: " + id);
                } else {
                    facultyService.delete(id);
                    System.out.println("🗑️ Faculty removed successfully (ID: " + id + ")");
                }
            }
            case 3 -> {
                System.out.print("Enter Course ID to remove: ");
                int id = sc.nextInt();
                Course c = courseService.getCourseById(id); // ✅ matches your existing method
                if (c == null) {
                    System.out.println("❌ No course found with ID: " + id);
                } else {
                    courseService.deleteCourse(id);
                    System.out.println("🗑️ Course removed successfully (ID: " + id + ")");
                }
            }
            case 4 -> {
                System.out.print("Enter Book ID to remove: ");
                int id = sc.nextInt();
                Library b = libraryService.get(id); // ✅ uses existing get() method
                if (b == null) {
                    System.out.println("❌ No book found with ID: " + id);
                } else {
                    libraryService.delete(id);
                    System.out.println("🗑️ Book removed successfully (ID: " + id + ")");
                }
            }
            case 5 -> {
                System.out.print("Enter Book Issue ID to remove: ");
                int id = sc.nextInt();
                BookIssue issue = bookIssueService.listAll().stream()
                        .filter(i -> i.getId() == id)
                        .findFirst()
                        .orElse(null); // ✅ no findById in your BookIssueService, safe fallback
                if (issue == null) {
                    System.out.println("❌ No book issue found with ID: " + id);
                } else {
                    HibernateUtil.getSessionFactory().openSession()
                            .beginTransaction();
                    issue.getBook().setAvailableCopies(
                            issue.getBook().getAvailableCopies() + 1);
                    bookIssueService.listAll().remove(issue);
                    System.out.println("🗑️ Book Issue removed successfully (ID: " + id + ")");
                }
            }
            default -> System.out.println("⚠️ Invalid choice.");
        }
    }
    
 // 📊 View Reports (New Feature)
    public void reportsMenu() {
        if (!isLoggedIn) {
            System.out.println("⚠️ Please login first!");
            return;
        }

        while (true) {
            System.out.println("\n-- Admin Reports --");
            System.out.println("1) View Pending Fees Students");
            System.out.println("2) View Low Attendance Students");
            System.out.println("3) Back");
            System.out.print("> ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> showPendingFeesReport();
                case 2 -> showLowAttendanceReport();
                case 3 -> { return; }
                default -> System.out.println("Invalid option.");
            }
        }
    }


    // 🔸 Students with Pending Fees
    private void showPendingFeesReport() {
        System.out.println("\n--- Students with Pending Fees ---");
        studentService.getStudentsWithPendingFees().forEach(s -> {
            double pending = s.getTotalFees() - s.getFeesPaid();
            System.out.printf("ID: %d | Name: %s | Total Fees: %.2f | Paid: %.2f | Pending: %.2f\n",
                    s.getId(), s.getName(), s.getTotalFees(), s.getFeesPaid(), pending);
        });
    }

 // 🔸 Students with Low Attendance (Safe fix)
    private void showLowAttendanceReport() {
        System.out.print("\nEnter attendance threshold (e.g., 75): ");
        double threshold = sc.nextDouble();
        sc.nextLine();

        System.out.println("\n--- Students Below " + threshold + "% Attendance ---");
        studentService.getLowAttendanceStudents(threshold).forEach(s -> {
            String courseName;
            try {
                // If getCourse() returns a Course object
                Object courseObj = s.getCourse();
                if (courseObj instanceof String) {
                    courseName = (String) courseObj;
                } else if (courseObj != null) {
                    // If it's a Course entity
                    courseName = ((Course) courseObj).getCourseName();
                } else {
                    courseName = "N/A";
                }
            } catch (Exception e) {
                courseName = "N/A";
            }

            System.out.printf("ID: %d | Name: %s | Attendance: %.2f%% | Course: %s\n",
                    s.getId(), s.getName(), s.getAttendancePercentage(), courseName);
        });
    }

}

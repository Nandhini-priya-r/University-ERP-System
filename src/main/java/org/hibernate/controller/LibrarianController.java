package org.hibernate.controller;

import java.util.List;

import org.hibernate.entity.BookIssue;
import org.hibernate.entity.Librarian;
import org.hibernate.entity.Student;
import org.hibernate.service.BookIssueService;
import org.hibernate.service.LibrarianService;
import org.hibernate.service.LibraryService;
import org.hibernate.service.StudentService;
import org.hibernate.util.InputUtil;

public class LibrarianController {
    private final LibrarianService librarianService = new LibrarianService();
    private final BookIssueService bookIssueService = new BookIssueService();
    private final StudentService studentService = new StudentService();

    public void menu() {
        while (true) {
            System.out.println("\n-- Librarian Menu --");
            System.out.println("1) Add Librarian");
            System.out.println("2) List Librarians");
            System.out.println("3) Issue Book");
            System.out.println("4) Return Book");
            System.out.println("5) View Issued Books");
            System.out.println("6) Manage Library Books"); // ✅ new option
            System.out.println("7) Back");
            int opt = InputUtil.nextInt("> ");

            switch (opt) {
                case 1 -> addLibrarian();
                case 2 -> librarianService.list().forEach(System.out::println);
                case 3 -> issueBookFlow();
                case 4 -> returnBookFlow();
                case 5 -> viewIssuedBooks();
                case 6 -> manageLibraryBooks(); // ✅ new case
                case 7 -> { return; }
                default -> System.out.println("Invalid option!");
            }
        }
    }


    private void addLibrarian() {
        String name = InputUtil.nextLine("Name: ");
        String contact = InputUtil.nextLine("Contact: ");
        String username = InputUtil.nextLine("Username: ");
        String password = InputUtil.nextLine("Password: ");
        librarianService.create(new Librarian(name, contact, username, password));
        System.out.println("✅ Librarian added!");
    }

    private void issueBookFlow() {
        System.out.println("\n-- Issue Book --");
        System.out.println("1) Issue by Member ID");
        System.out.println("2) Issue by Student ID (auto-create Member if needed)");
        int choice = InputUtil.nextInt("> ");

        // Use a librarian for all issues (can be logged or actual logged-in librarian)
        Librarian librarian = new Librarian("System", "N/A", "auto", "auto");

        if (choice == 1) {
            int memberId = InputUtil.nextInt("Member ID: ");
            int bookId = InputUtil.nextInt("Book ID: ");
            boolean ok = bookIssueService.issueBook(memberId, bookId, librarian);
            System.out.println(ok ? "✅ Book issued." : "❌ Issue failed.");
        } else if (choice == 2) {
            int studentId = InputUtil.nextInt("Student ID: ");
            Student s = studentService.findById(studentId);
            if (s == null) {
                System.out.println("❌ Student not found.");
                return;
            }
            int bookId = InputUtil.nextInt("Book ID: ");
            // Auto-create a member record if missing and issue book
            boolean ok = bookIssueService.issueBookForStudent(studentId, s.getName(), bookId, librarian);
            System.out.println(ok ? "✅ Book issued." : "❌ Issue failed.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void returnBookFlow() {
        System.out.println("\n-- Return Book --");
        System.out.println("1) Return by Member ID");
        System.out.println("2) Return by Student ID");
        int choice = InputUtil.nextInt("> ");

        Librarian librarian = new Librarian("System", "N/A", "auto", "auto");

        if (choice == 1) {
            int memberId = InputUtil.nextInt("Member ID: ");
            int bookId = InputUtil.nextInt("Book ID: ");
            boolean ok = bookIssueService.returnBook(memberId, bookId, librarian);
            System.out.println(ok ? "✅ Book returned." : "❌ Return failed.");
        } else if (choice == 2) {
            int studentId = InputUtil.nextInt("Student ID: ");
            int bookId = InputUtil.nextInt("Book ID: ");
            boolean ok = bookIssueService.returnBookForStudent(studentId, bookId);
            System.out.println(ok ? "✅ Book returned." : "❌ Return failed.");
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void viewIssuedBooks() {
        System.out.println("\n-- Issued Books --");
        List<BookIssue> issues = bookIssueService.listAll();
        if (issues == null || issues.isEmpty()) {
            System.out.println("No issued books found.");
            return;
        }
        for (BookIssue bi : issues) {
            System.out.println(bi);
        }
    }
    
 // --- Add this inside LibrarianController class (not outside) ---

    private final LibraryService libraryService = new LibraryService();

    private void manageLibraryBooks() {
        while (true) {
            System.out.println("\n-- Library Management --");
            System.out.println("1) Add New Book");
            System.out.println("2) List All Books");
            System.out.println("3) Update Book Copies");
            System.out.println("4) Back");
            int choice = InputUtil.nextInt("> ");

            switch (choice) {
                case 1 -> {
                    String title = InputUtil.nextLine("Title: ");
                    String author = InputUtil.nextLine("Author: ");
                    String category = InputUtil.nextLine("Category: ");
                    String isbn = InputUtil.nextLine("ISBN: ");
                    int copies = InputUtil.nextInt("Total Copies: ");
                    libraryService.addBook(title, author, category, isbn, copies);
                    System.out.println("✅ Book added successfully.");
                }
                case 2 -> {
                    var books = libraryService.listAllBooks();
                    if (books.isEmpty()) System.out.println("No books available.");
                    else books.forEach(System.out::println);
                }
                case 3 -> {
                    int bookId = InputUtil.nextInt("Book ID: ");
                    int delta = InputUtil.nextInt("Change in copies (+/-): ");
                    boolean ok = libraryService.updateCopies(bookId, delta);
                    System.out.println(ok ? "✅ Copies updated." : "❌ Update failed.");
                }
                case 4 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

}

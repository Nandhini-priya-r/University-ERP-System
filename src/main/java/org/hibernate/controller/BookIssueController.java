package org.hibernate.controller;

import org.hibernate.entity.Librarian;
import org.hibernate.service.BookIssueService;
import org.hibernate.util.InputUtil;

public class BookIssueController {

    private final BookIssueService issueService = new BookIssueService();

    // Existing methods remain unchanged
    public void issueBook() {
        System.out.println("\n--- Issue Book ---");
        int memberId = InputUtil.nextInt("Enter Member ID: ");
        int bookId = InputUtil.nextInt("Enter Book ID: ");
        issueService.issueBook(memberId, bookId);
    }

    public void returnBook() {
        System.out.println("\n--- Return Book ---");
        int memberId = InputUtil.nextInt("Enter Member ID: ");
        int bookId = InputUtil.nextInt("Enter Book ID: ");
        issueService.returnBook(memberId, bookId);
    }

    // ---------------- New librarian-aware methods ----------------

    public void issueBookWithLibrarian() {
        System.out.println("\n--- Issue Book (Librarian) ---");
        int memberId = InputUtil.nextInt("Enter Member ID: ");
        int bookId = InputUtil.nextInt("Enter Book ID: ");
        String libName = InputUtil.nextLine("Enter Librarian Name: ");
        String libContact = InputUtil.nextLine("Enter Librarian Contact: ");
        String libUsername = InputUtil.nextLine("Enter Librarian Username: ");
        String libPassword = InputUtil.nextLine("Enter Librarian Password: ");

        Librarian librarian = new Librarian(libName, libContact, libUsername, libPassword);
        boolean ok = issueService.issueBook(memberId, bookId, librarian);

        System.out.println(ok ? "✅ Book issued by " + librarian.getName()
                              : "❌ Issue failed.");
    }

    public void returnBookWithLibrarian() {
        System.out.println("\n--- Return Book (Librarian) ---");
        int memberId = InputUtil.nextInt("Enter Member ID: ");
        int bookId = InputUtil.nextInt("Enter Book ID: ");
        String libName = InputUtil.nextLine("Enter Librarian Name: ");
        String libContact = InputUtil.nextLine("Enter Librarian Contact: ");
        String libUsername = InputUtil.nextLine("Enter Librarian Username: ");
        String libPassword = InputUtil.nextLine("Enter Librarian Password: ");

        Librarian librarian = new Librarian(libName, libContact, libUsername, libPassword);
        boolean ok = issueService.returnBook(memberId, bookId, librarian);

        System.out.println(ok ? "✅ Book returned by " + librarian.getName()
                              : "❌ Return failed.");
    }
    
    public void issueBookByFaculty() {
        System.out.println("\n--- Issue Book (Faculty) ---");
        int memberId = InputUtil.nextInt("Enter Faculty Member ID: ");
        int bookId = InputUtil.nextInt("Enter Book ID: ");

        boolean success = issueService.issueBookByFaculty(memberId, bookId);
        if (success) System.out.println("✅ Book issued successfully.");
        else System.out.println("❌ Issue failed.");
    }

    public void returnBookByFaculty() {
        System.out.println("\n--- Return Book (Faculty) ---");
        int facultyId = InputUtil.nextInt("Enter Faculty Member ID: ");
        int bookId = InputUtil.nextInt("Enter Book ID: ");

        boolean success = issueService.returnBookByFaculty(facultyId, bookId);
        System.out.println(success ? "✅ Book returned successfully"
                                   : "❌ Return failed.");
    }
}

package org.hibernate.controller;

import org.hibernate.entity.Library;
import org.hibernate.service.LibraryService;
import org.hibernate.util.InputUtil;
import java.util.List;

public class LibraryController {
    private final LibraryService service = new LibraryService();

    // Add new book interactively
    public void addBook() {
        String title = InputUtil.nextLine("Enter Book Title: ");
        String author = InputUtil.nextLine("Enter Author Name: ");
        String isbn = InputUtil.nextLine("Enter ISBN: ");
        String category = InputUtil.nextLine("Enter Category: ");
        int totalCopies = InputUtil.nextInt("Enter Total Copies: ");

        Library book = new Library(title, author, isbn, category, totalCopies);
        service.create(book);
        System.out.println("✅ Book added successfully!");
    }

    // List all books
    public void listBooks() {
        List<Library> books = service.list();
        if (books.isEmpty()) {
            System.out.println("📚 No books found in the library.");
            return;
        }
        books.forEach(System.out::println);
    }

    // Delete a book
    public void deleteBook() {
        int id = InputUtil.nextInt("Enter Book ID to delete: ");
        service.delete(id);
        System.out.println("🗑️ Book deleted (if existed).");
    }

    // Get a book by ID
    public Library get(int id) {
        return service.get(id);
    }
}

package org.hibernate.service;

import org.hibernate.dao.LibraryDAO;
import org.hibernate.entity.Library;
import org.hibernate.util.InputUtil;

import java.util.List;

public class LibraryService {
    private final LibraryDAO dao = new LibraryDAO();

    public Library create(Library l) { return dao.save(l); }
    public Library update(Library l) { return dao.update(l); }
    public void delete(int id) { dao.delete(id); }
    public Library get(int id) { return dao.findById(id); }
    public List<Library> list() { return dao.findAll(); }

    public List<Library> listAllBooks() {
        return dao.findAll();
    }

    // ✅ Add a new book to the library
    public Library addBook(String title, String author, String category, String isbn, int totalCopies) {
        Library book = new Library();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setIsbn(isbn);
        book.setTotalCopies(totalCopies);
        book.setAvailableCopies(totalCopies);
        return dao.save(book);
    }

    // ✅ Update available copies (for add/remove stock)
    public boolean updateCopies(int bookId, int delta) {
        Library book = dao.findById(bookId);
        if (book == null) return false;
        int newCopies = book.getAvailableCopies() + delta;
        if (newCopies < 0) return false; // prevent negative
        book.setAvailableCopies(newCopies);
        dao.update(book);
        return true;
    }
    
   

}

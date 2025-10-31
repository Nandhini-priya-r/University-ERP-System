package org.hibernate.service;

import org.hibernate.dao.LibrarianDAO;
import org.hibernate.entity.Librarian;

import java.util.List;

public class LibrarianService {
    private final LibrarianDAO dao = new LibrarianDAO();

    public Librarian create(Librarian l) { return dao.save(l); }
    public Librarian update(Librarian l) { return dao.update(l); }
    public void delete(int id) { dao.delete(id); }
    public Librarian get(int id) { return dao.findById(id); }
    public List<Librarian> list() { return dao.findAll(); }

    public Librarian login(String username, String password) {
        return dao.findByCredentials(username, password);
    }
}

package org.hibernate.dao;

import org.hibernate.entity.Library;

public class LibraryDAO extends GenericDAO<Library> {
    public LibraryDAO() {
        super(Library.class);
    }
}

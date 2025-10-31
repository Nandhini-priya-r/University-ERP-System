package org.hibernate.dao;

import org.hibernate.entity.Faculty;

public class FacultyDAO extends GenericDAO<Faculty> {
    public FacultyDAO() {
        super(Faculty.class);
    }
}

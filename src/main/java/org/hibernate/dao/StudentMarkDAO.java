package org.hibernate.dao;

import org.hibernate.entity.StudentMark;

public class StudentMarkDAO extends GenericDAO<StudentMark> {
    public StudentMarkDAO() { super(StudentMark.class); }
}

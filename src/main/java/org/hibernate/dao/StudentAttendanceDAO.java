package org.hibernate.dao;

import org.hibernate.entity.StudentAttendance;

public class StudentAttendanceDAO extends GenericDAO<StudentAttendance> {
    public StudentAttendanceDAO() { super(StudentAttendance.class); }
}

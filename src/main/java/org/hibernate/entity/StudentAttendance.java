package org.hibernate.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "student_attendance")
public class StudentAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private boolean present;

    private String remarks;

    public StudentAttendance() {}

    public StudentAttendance(Student student, Faculty faculty, LocalDate date, boolean present, String remarks) {
        this.student = student;
        this.faculty = faculty;
        this.date = date;
        this.present = present;
        this.remarks = remarks;
    }

    // getters & setters
    public Integer getAttendanceId() { return attendanceId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public boolean isPresent() { return present; }
    public void setPresent(boolean present) { this.present = present; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    @Override
    public String toString() {
        return "Attendance[id=" + attendanceId + ", student=" + (student != null ? student.getId() : "null")
                + ", faculty=" + (faculty != null ? faculty.getId() : "null") + ", date=" + date + ", present=" + present + "]";
    }
}

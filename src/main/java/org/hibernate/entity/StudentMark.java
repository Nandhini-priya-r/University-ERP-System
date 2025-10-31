package org.hibernate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "student_marks")
public class StudentMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer markId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    private String subject;
    private Integer internalMarks;
    private Integer externalMarks;
    private Integer totalMarks;
    private String remarks;

    public StudentMark() {}

    public StudentMark(Student student, Faculty faculty, String subject, Integer internalMarks, Integer externalMarks, String remarks) {
        this.student = student;
        this.faculty = faculty;
        this.subject = subject;
        this.internalMarks = internalMarks;
        this.externalMarks = externalMarks;
        this.totalMarks = (internalMarks == null ? 0 : internalMarks) + (externalMarks == null ? 0 : externalMarks);
        this.remarks = remarks;
    }

    // getters & setters
    public Integer getMarkId() { return markId; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public Integer getInternalMarks() { return internalMarks; }
    public void setInternalMarks(Integer internalMarks) { this.internalMarks = internalMarks; }
    public Integer getExternalMarks() { return externalMarks; }
    public void setExternalMarks(Integer externalMarks) { this.externalMarks = externalMarks; }
    public Integer getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Integer totalMarks) { this.totalMarks = totalMarks; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    @PrePersist @PreUpdate
    private void computeTotal() {
        int i = internalMarks == null ? 0 : internalMarks;
        int e = externalMarks == null ? 0 : externalMarks;
        this.totalMarks = i + e;
    }

    @Override
    public String toString() {
        return "Mark[id=" + markId + ", student=" + (student!=null?student.getId():"null") + ", subject=" + subject + ", total=" + totalMarks + "]";
    }
}

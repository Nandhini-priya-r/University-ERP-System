package org.hibernate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String role; // student/faculty

    @OneToOne
    @JoinColumn(name = "linked_student_id", insertable=false, updatable=false)
    private Student student;

    @Column(name = "linked_student_id")
    private Integer linkedStudentId;
    
    private Integer linkedFacultyId;


    public Member() {}
    public Member(String name, String role) {
        this.name = name;
        this.role = role;
    }

    // ✅ Getters / Setters
    public int getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getLinkedStudentId() { return linkedStudentId; }
    public void setLinkedStudentId(Integer linkedStudentId) { this.linkedStudentId = linkedStudentId; }

    public Integer getLinkedFacultyId() { return linkedFacultyId; }
    public void setLinkedFacultyId(Integer linkedFacultyId) { this.linkedFacultyId = linkedFacultyId; }

    @Override
    public String toString() {
        return "Member{id=" + id + ", name='" + name + "', role='" + role +
               "', linkedStudentId=" + linkedStudentId + "}";
    }
}

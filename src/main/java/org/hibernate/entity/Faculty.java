package org.hibernate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "faculty")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String department;
    private String qualification;
    private String contact;
    private String specialization;

    // ✅ New Field — Won’t break anything in existing logic
    private String workingStatus = "Active";  // default status

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.Set<Course> courses = new java.util.HashSet<>();

    public java.util.Set<Course> getCourses() { return courses; }
    public void setCourses(java.util.Set<Course> courses) { this.courses = courses; }

    public Faculty() {}

    public Faculty(String name, String department, String qualification, String contact, String specialization) {
        this.name = name;
        this.department = department;
        this.qualification = qualification;
        this.contact = contact;
        this.specialization = specialization;
        this.workingStatus = "Active"; // default when added
    }

    // ===== Getters & Setters =====
    public Integer getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    // ✅ Getter and Setter for Working Status
    public String getWorkingStatus() { return workingStatus; }
    public void setWorkingStatus(String workingStatus) { this.workingStatus = workingStatus; }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dept='" + department + '\'' +
                ", qual='" + qualification + '\'' +
                ", contact='" + contact + '\'' +
                ", spec='" + specialization + '\'' +
                ", status='" + workingStatus + '\'' +
                '}';
    }
}

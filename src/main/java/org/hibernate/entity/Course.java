package org.hibernate.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String courseName;
    private String courseCode;
    private int durationInMonths;
    private String semesterStructure; // e.g., "Semester 1,2,3"
    private int credits;
    private String gradeSystem;

    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private Faculty faculty; // linked faculty

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department; // linked department
    

    // Many-to-many for student enrollments
    
    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
 
    private Set<Student> enrolledStudents = new HashSet<>();


    // ======= Constructors =======
    public Course() {}

    public Course(String courseName, String courseCode, int durationInMonths, String semesterStructure,
                  int credits, String gradeSystem, Faculty faculty, Department department) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.durationInMonths = durationInMonths;
        this.semesterStructure = semesterStructure;
        this.credits = credits;
        this.gradeSystem = gradeSystem;
        this.faculty = faculty;
        this.department = department;
    }

    // ======= Getters & Setters =======
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public int getDurationInMonths() { return durationInMonths; }
    public void setDurationInMonths(int durationInMonths) { this.durationInMonths = durationInMonths; }

    public String getSemesterStructure() { return semesterStructure; }
    public void setSemesterStructure(String semesterStructure) { this.semesterStructure = semesterStructure; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public String getGradeSystem() { return gradeSystem; }
    public void setGradeSystem(String gradeSystem) { this.gradeSystem = gradeSystem; }

    public Faculty getFaculty() { return faculty; }
    public void setFaculty(Faculty faculty) { this.faculty = faculty; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public Set<Student> getEnrolledStudents() { return enrolledStudents; }
    public void setEnrolledStudents(Set<Student> enrolledStudents) { this.enrolledStudents = enrolledStudents; }

    public void addStudent(Student s) {
        enrolledStudents.add(s);
        s.getCourses().add(this); // ensure bi-directional
    }

    public void removeStudent(Student s) {
        enrolledStudents.remove(s);
        s.getCourses().remove(this);
    }
    
    public void enrollStudent(Student student) {
        enrolledStudents.add(student);
        student.getCourses().add(this);
    }

    public void withdrawStudent(Student student) {
        enrolledStudents.remove(student);
        student.getCourses().remove(this);
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", name=" + courseName + ", code=" + courseCode +
               ", duration=" + durationInMonths + " months, semester=" + semesterStructure +
               ", credits=" + credits + ", gradeSystem=" + gradeSystem +
               ", faculty=" + (faculty != null ? faculty.getName() : "N/A") +
               ", department=" + (department != null ? department.getName() : "N/A") + "]";
    }

}

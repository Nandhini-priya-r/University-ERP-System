package org.hibernate.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String rollNo;

    private String course; 
    private String department;
    private String contact;
    private String address;
    private String admissionYear;
    private String enrollmentStatus;
    private double attendancePercentage;
    private double marksPercentage;
    private double totalFees;
    private double feesPaid;
    private String hostelStatus;
    private String libraryCardNo;
    private LocalDate lastUpdatedAttendance;
    private LocalDate lastUpdatedMarks;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();


    
    // ======= Constructors =======
    public Student() {}

    public Student(String name, String rollNo, String course, String department,
                   String contact, String address, String admissionYear,
                   String enrollmentStatus, double attendancePercentage,
                   double marksPercentage, double totalFees, double feesPaid,
                   String hostelStatus, String libraryCardNo) {
        this.name = name;
        this.rollNo = rollNo;
        this.course = course;
        this.department = department;
        this.contact = contact;
        this.address = address;
        this.admissionYear = admissionYear;
        this.enrollmentStatus = enrollmentStatus;
        this.attendancePercentage = attendancePercentage;
        this.marksPercentage = marksPercentage;
        this.totalFees = totalFees;
        this.feesPaid = feesPaid;
        this.hostelStatus = hostelStatus;
        this.libraryCardNo = libraryCardNo;
    }

    // ======= Getters & Setters =======
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRollNo() { return rollNo; }
    public void setRollNo(String rollNo) { this.rollNo = rollNo; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAdmissionYear() { return admissionYear; }
    public void setAdmissionYear(String admissionYear) { this.admissionYear = admissionYear; }

    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }

    public double getAttendancePercentage() { return attendancePercentage; }
    public void setAttendancePercentage(double attendancePercentage) { this.attendancePercentage = attendancePercentage; }

    public double getMarksPercentage() { return marksPercentage; }
    public void setMarksPercentage(double marksPercentage) { this.marksPercentage = marksPercentage; }

    public double getTotalFees() { return totalFees; }
    public void setTotalFees(double totalFees) { this.totalFees = totalFees; }

    public double getFeesPaid() { return feesPaid; }
    public void setFeesPaid(double feesPaid) { this.feesPaid = feesPaid; }

    public String getHostelStatus() { return hostelStatus; }
    public void setHostelStatus(String hostelStatus) { this.hostelStatus = hostelStatus; }

    public String getLibraryCardNo() { return libraryCardNo; }
    public void setLibraryCardNo(String libraryCardNo) { this.libraryCardNo = libraryCardNo; }

    public Set<Course> getCourses() { return courses; }
    public void setCourses(Set<Course> courses) { this.courses = courses; }
    
    public LocalDate getLastUpdatedAttendance() { return lastUpdatedAttendance; }
    public void setLastUpdatedAttendance(LocalDate lastUpdatedAttendance) { this.lastUpdatedAttendance = lastUpdatedAttendance; }

    public LocalDate getLastUpdatedMarks() { return lastUpdatedMarks; }
    public void setLastUpdatedMarks(LocalDate lastUpdatedMarks) { this.lastUpdatedMarks = lastUpdatedMarks; }
 // inside Student.java
 // Getter for enrolled courses as a Set
    public Set<Course> getEnrolledCourses() {
        return courses;
    }

    // Setter
    public void setEnrolledCourses(Set<Course> courses) {
        this.courses = courses;
    }
    
    public List<Course> getEnrolledCoursesList() {
        return new ArrayList<>(courses);
    }


    // ======= Helper methods =======
    public void enrollInCourse(Course course) {
        courses.add(course);
        course.getEnrolledStudents().add(this);
    }

    public void withdrawCourse(Course course) {
        courses.remove(course);
        course.getEnrolledStudents().remove(this);
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", rollNo=" + rollNo +
               ", enrolledCourses=" + courses.size() + ", department=" + department + "]";
    }
}

package org.hibernate.controller;

import org.hibernate.entity.Course;
import org.hibernate.entity.Faculty;
import org.hibernate.entity.Student;
import org.hibernate.service.CourseService;
import org.hibernate.service.FacultyService;
import org.hibernate.service.StudentService;
import org.hibernate.util.InputUtil;

import java.util.List;

public class CourseController {

    private final CourseService courseService = new CourseService();
    private final StudentService studentService = new StudentService();
    private final FacultyService facultyService = new FacultyService();

    // ----- Add or update course (Faculty/Admin) -----
    public void addOrUpdateCourse() {
        System.out.println("\n--- Add / Update Course ---");
        int id = InputUtil.nextInt("Enter Course ID (0 for new): ");
        Course course = id == 0 ? new Course() : courseService.getCourseById(id);

        if (course == null) {
            System.out.println("❌ Course not found, creating new...");
            course = new Course();
        }

        course.setCourseName(InputUtil.nextLine("Course Name: "));
        course.setCourseCode(InputUtil.nextLine("Course Code: "));
        course.setDurationInMonths(InputUtil.nextInt("Duration in months: "));
        course.setSemesterStructure(InputUtil.nextLine("Semester Structure (e.g., Sem 1,2,3): "));
        course.setCredits(InputUtil.nextInt("Credits: "));
        course.setGradeSystem(InputUtil.nextLine("Grade System (A-F): "));

        // Optional Faculty linking
        int facultyId = InputUtil.nextInt("Faculty ID (0 to skip): ");
        if (facultyId != 0) {
            Faculty f = facultyService.findById(facultyId);
            if (f != null) {
                course.setFaculty(f);
                f.getCourses().add(course); // add course to faculty’s course list
            } else {
                System.out.println("❌ Faculty not found, skipping linking.");
            }
        }

        // Save or update course
        boolean success = (id == 0) ? courseService.addCourse(course) : courseService.updateCourse(course);
        System.out.println(success ? "✅ Course saved successfully!" : "❌ Failed to save course.");
    }


    // ----- List courses -----
    public void listCourses() {
        System.out.println("\n--- Courses List ---");
        List<Course> courses = courseService.listAllCourses();
        if (courses.isEmpty()) System.out.println("No courses found.");
        else courses.forEach(System.out::println);
    }

 // ----- Student enroll courses -----
    public void enrollStudent() {
        System.out.println("\n--- Enroll Student in a Course ---");

        int studentId = InputUtil.nextInt("Enter Student ID: ");
        Student s = studentService.findById(studentId);
        if (s == null) {
            System.out.println("❌ Student not found.");
            return;
        }

        // List available courses
        List<Course> courses = courseService.listAllCourses();
        if (courses.isEmpty()) {
            System.out.println("❌ No courses available.");
            return;
        }

        System.out.println("\nAvailable Courses:");
        for (Course c : courses) {
            System.out.println(c.getId() + ") " + c.getCourseName() + " (Credits: " + c.getCredits() + ")");
        }

        int courseId = InputUtil.nextInt("Enter Course ID to enroll: ");
        Course c = courseService.getCourseById(courseId);
        if (c == null) {
            System.out.println("❌ Course not found.");
            return;
        }

        // Check if already enrolled
        if (s.getCourses().contains(c)) {
            System.out.println("❌ Student already enrolled in this course.");
            return;
        }

        // Enroll student
        s.enrollInCourse(c);

        // Save changes
        boolean studentUpdated = studentService.updateStudent(s); // updateStudent() must exist in StudentService
        boolean courseUpdated = courseService.updateCourse(c);

        if (studentUpdated && courseUpdated) {
            System.out.println("✅ Student " + s.getName() + " enrolled in " + c.getCourseName());
        } else {
            System.out.println("❌ Enrollment failed.");
        }
    }

    // ----- Delete course -----
    public boolean deleteCourse(int courseId) {
        return courseService.deleteCourse(courseId);
    }
}

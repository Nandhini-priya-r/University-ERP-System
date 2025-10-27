# University-ERP-System
This project demonstrates an end-to-end university ERP solution with modular, role-based design and database integration via Hibernate. It focuses on automation, scalability, and maintainability, serving as a foundation for enterprise-level university management systems.
University ERP System (Hibernate + Maven + MySQL)

Overview
The University ERP System is a comprehensive management solution developed using Java (JDK 17), Hibernate ORM, and MySQL. It automates key university workflows such as student administration, faculty management, library operations, and administrative reporting, all within a Command-Line Interface (CLI) application.

This system streamlines academic and administrative tasks through role-based access for Admins, Faculty, Librarians, and Students, enabling efficient CRUD operations and data-driven reports across modules.

Core Features
Student Module

View personal records using student ID

Track attendance, marks, and fee status (paid/pending)

View total dues and academic performance

Faculty Module

Add and manage student details

Update attendance and marks for assigned courses

View academic reports and performance analytics

Library & Member Module

Issue and return books for students and faculty

Auto-generate library membership IDs

Track due dates and calculate fines for late returns

Admin Module

Access dashboards summarizing student, faculty, course, and fee reports

Identify students with low attendance or pending fees

Manage and remove system entities (students, faculty, courses, etc.)

Oversee all university data operations

Authentication & Roles

The system supports multiple roles — Admin, Faculty, Student, and Librarian — each with restricted permissions and dedicated role-based menus to ensure data privacy and operational control.

Technology Stack
Category	Technology Used
Language	Java 17
ORM Framework	Hibernate 6.2.7.Final
Database	MySQL
Configuration File	hibernate.cfg.xml
Architecture	Layered MVC (Controller → Service → DAO → Entity)
IDE	Eclipse / IntelliJ IDEA
System Workflow

Admin: Manages users, oversees university operations, and monitors data integrity through reports.

Faculty: Updates student attendance and marks; reviews academic results.

Student: Views attendance, grades, and library records.

Librarian: Handles book inventory, issuing, and returns.

Library Members (Students/Faculty): Borrow and return books as per due dates.

Backend System: All operations are handled via Hibernate’s DAO and Service layers to ensure consistency and scalability.

Database Design Overview
Table Name	Description	Key Fields
students	Stores student details	student_id (PK)
faculty	Contains faculty and department info	faculty_id (PK)
courses	Lists university courses and subjects	course_id (PK)
library	Maintains book details	book_id (PK)
members	Tracks library memberships	member_id (PK)
book_issues	Records issued books, due dates, and fines	issue_id (PK), member_id (FK)
admin	Stores admin credentials	admin_id (PK)
Future Enhancements

Web-based Upgrade: Migrate from CLI to Spring Boot + React full-stack application.

Automated Notifications: Email alerts for attendance shortages or overdue books.

Secure Authentication: Implement JWT-based role-specific login sessions.

Analytics Dashboard: Visualize attendance, department strength, and fee statistics.

Cloud Deployment: Deploy on AWS/Azure with CI/CD integration for scalability.

Conclusion

The University Management System (ERP) delivers a unified platform for handling academic and administrative tasks efficiently. Its modular architecture ensures seamless coordination among stakeholders while maintaining data consistency and security. Built with Hibernate and Maven, the project exemplifies best practices in scalability, maintainability, and real-world application design for modern educational systems.

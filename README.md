

# 🎓 University ERP System

> **End-to-End Enterprise Resource Planning Solution** for universities — built with **Java (JDK 17)**, **Hibernate**, and **MySQL**.
> Designed for **automation, scalability, and modularity**, enabling efficient management of students, faculty, courses, and library operations.

---

## 🧭 Overview

The **University ERP System** is a comprehensive management solution developed using **Java (JDK 17)**, **Hibernate ORM**, and **MySQL**.
It automates key university workflows such as **student administration**, **faculty management**, **library operations**, and **administrative reporting** — all within a **Command-Line Interface (CLI)** application.

🔹 **Role-Based Access:** Admins, Faculty, Librarians, and Students
🔹 **Features:** CRUD operations, analytics, fine tracking, and performance reports
🔹 **Goal:** Streamlined data management with real-time insights and automation

---

## ⚙️ Core Features

### 👩‍🎓 Student Module

* 📄 View personal records using student ID
* 🕒 Track attendance, marks, and fee status (paid/pending)
* 💰 View total dues and academic performance

### 👨‍🏫 Faculty Module

* ➕ Add and manage student details
* 🧾 Update attendance and marks for assigned courses
* 📊 View academic reports and performance analytics

### 📚 Library & Member Module

* 📘 Issue and return books for students and faculty
* 🆔 Auto-generate library membership IDs
* ⏰ Track due dates and calculate fines for late returns

### 🧑‍💼 Admin Module

* 📋 Access dashboards summarizing student, faculty, course, and fee reports
* ⚠️ Identify students with low attendance or pending fees
* 🗑️ Manage and remove entities (students, faculty, courses, etc.)
* 🔐 Oversee all university data operations

---

## 🔑 Authentication & Roles

The system supports multiple roles:

* 🧑‍💼 **Admin** – Full access to all modules
* 👨‍🏫 **Faculty** – Manage students and course data
* 👩‍🎓 **Student** – View personal and academic details
* 📚 **Librarian** – Handle library inventory and book issuance

Each role has restricted permissions and dedicated menus to ensure **data privacy** and **operational control**.

---

## 🧰 Technology Stack

| 🏷️ Category     | ⚙️ Technology Used                                |
| ---------------- | ------------------------------------------------- |
| 💻 Language      | Java 17                                           |
| 🧩 ORM Framework | Hibernate 6.2.7.Final                             |
| 🗄️ Database     | MySQL                                             |
| ⚙️ Config File   | `hibernate.cfg.xml`                               |
| 🏗️ Architecture | Layered MVC (Controller → Service → DAO → Entity) |
| 🧠 IDE           | Eclipse / IntelliJ IDEA                           |

---

## 🔁 System Workflow

| 👤 Role                           | 🧭 Responsibilities                                         |
| --------------------------------- | ----------------------------------------------------------- |
| 🧑‍💼 **Admin**                   | Manages users, oversees operations, and monitors reports    |
| 👨‍🏫 **Faculty**                 | Updates attendance, marks, and evaluates student results    |
| 👩‍🎓 **Student**                 | Views attendance, grades, and library records               |
| 📚 **Librarian**                  | Handles inventory, issuing, and returning books             |
| 👥 **Members (Students/Faculty)** | Borrow and return books as per due dates                    |
| ⚙️ **Backend**                    | All operations handled via Hibernate’s DAO & Service layers |

---

## 🗃️ Database Schema

| Table Name                 | Description                                | Key Fields                        |
| -------------------------- | ------------------------------------------ | --------------------------------- |
| 🎓 **students**            | Stores student details                     | `student_id` (PK)                 |
| 👨‍🏫 **faculty**          | Contains faculty and department info       | `faculty_id` (PK)                 |
| 📚 **courses**             | Lists university courses and subjects      | `course_id` (PK)                  |
| 📚 **library**             | Maintains book details                     | `book_id` (PK)                    |
| 👥 **members**             | Tracks library memberships                 | `member_id` (PK)                  |
| 🧾 **book_issues**         | Records issued books, due dates, and fines | `issue_id` (PK), `member_id` (FK) |
| 🧾 **admin**               | Stores admin credentials                   | `admin_id` (PK)                   |
| 🔮 **Future Enhancements** | Placeholder for upcoming modules           | —                                 |

**Legend:**
🗝️ **PK** → Primary Key 🔗 **FK** → Foreign Key

---

## 🚀 Future Enhancements

| Feature                        | Description                                            |
| ------------------------------ | ------------------------------------------------------ |
| 🌐 **Web-based Upgrade**       | Migrate from CLI → Full-stack (Spring Boot + React)    |
| 📧 **Automated Notifications** | Email alerts for attendance shortages or overdue books |
| 🔒 **Secure Authentication**   | Implement JWT-based role login sessions                |
| 📊 **Analytics Dashboard**     | Visualize attendance, strength, and fee statistics     |
| ☁️ **Cloud Deployment**        | Host on AWS/Azure with CI/CD pipelines                 |

---

## 🏁 Conclusion

The **University Management ERP System** delivers a unified platform for **academic and administrative automation**.
With its modular, scalable design, it ensures **data integrity, operational efficiency, and security** across departments.

💡 Built using **Hibernate + Maven**, it exemplifies enterprise-grade practices and serves as a solid foundation for future **web-based** or **cloud-integrated** university systems.



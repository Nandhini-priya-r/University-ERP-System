package org.hibernate.service;

import org.hibernate.dao.MemberDAO;
import org.hibernate.entity.Faculty;
import org.hibernate.entity.Member;
import org.hibernate.entity.Student;
import org.hibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MemberService {
    private final MemberDAO dao = new MemberDAO();

    public Member create(Member m) { return dao.save(m); }
    public Member update(Member m) { return dao.update(m); }
    public void delete(int id) { dao.delete(id); }
    public Member get(int id) { return dao.findById(id); }
    public List<Member> list() { return dao.findAll(); }

    // 🔍 Find Member by linked student ID
    public Member findByStudentId(int studentId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery(
                "FROM Member WHERE linkedStudentId = :id", Member.class)
                .setParameter("id", studentId)
                .uniqueResult();
        } catch (Exception e) {
            System.err.println("❌ Error finding member by studentId: " + e.getMessage());
            return null;
        } finally {
            session.close();
        }
    }

    // 🧩 Auto-create Member for a student if not already linked
    public Member createLibraryAccessForStudent(int studentId, String studentName) {
        Member existing = findByStudentId(studentId);
        if (existing != null) return existing;

        Member newMember = new Member(studentName, "student");
        newMember.setLinkedStudentId(studentId);
        return dao.save(newMember);
    }
    
    public Member getOrCreateMemberForStudent(Student student) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Member member = null;

        try {
            member = session.createQuery("FROM Member m WHERE m.linkedStudentId = :sid", Member.class)
                            .setParameter("sid", student.getId())
                            .uniqueResult();

            if(member == null) {
                Transaction tx = session.beginTransaction();
                member = new Member();
                member.setName(student.getName());
                member.setRole("student");
                member.setLinkedStudentId(student.getId());
                session.save(member);
                tx.commit();
            }

        } finally {
            session.close();
        }
        return member;
    }

    public Member getOrCreateMemberForFaculty(Faculty faculty) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Member member = null;

        try {
            member = session.createQuery("FROM Member m WHERE m.linkedFacultyId = :fid", Member.class)
                            .setParameter("fid", faculty.getId())
                            .uniqueResult();

            if(member == null) {
                Transaction tx = session.beginTransaction();
                member = new Member();
                member.setName(faculty.getName());
                member.setRole("faculty");
                member.setLinkedFacultyId(faculty.getId());
                session.save(member);
                tx.commit();
            }

        } finally {
            session.close();
        }
        return member;
    }
    
    public Member findByFacultyId(int facultyId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            return session.createQuery("FROM Member WHERE linkedFacultyId = :id", Member.class)
                          .setParameter("id", facultyId)
                          .uniqueResult();
        } finally {
            session.close();
        }
    }

}

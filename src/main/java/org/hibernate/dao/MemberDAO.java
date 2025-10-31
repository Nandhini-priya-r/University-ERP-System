package org.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.entity.Member;
import org.hibernate.query.Query;
import org.hibernate.util.HibernateUtil;

public class MemberDAO extends GenericDAO<Member> {
    public MemberDAO() {
        super(Member.class);
    }
    
    public Member findByStudentId(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Member m WHERE m.student.id = :sid";
            Query<Member> query = session.createQuery(hql, Member.class);
            query.setParameter("sid", studentId);
            return query.uniqueResult();  // returns null if not found
        }
    }
}

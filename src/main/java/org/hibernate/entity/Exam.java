package org.hibernate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "exams")
public class Exam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    private String subject; private String term;
    public Exam(){} public Exam(String subject,String term){this.subject=subject;this.term=term;}
    public Integer getId(){return id;} public String getSubject(){return subject;} public void setSubject(String s){this.subject=s;}
    public String getTerm(){return term;} public void setTerm(String t){this.term=t;}
    @Override public String toString(){return "Exam{id="+id+", subject='"+subject+"', term='"+term+"'}";}


}

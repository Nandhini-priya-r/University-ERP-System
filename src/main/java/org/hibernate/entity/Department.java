package org.hibernate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "departments")
public class Department {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    @Column(nullable = false, unique = true) private String name;
    public Department(){} public Department(String name){this.name=name;}
    public Integer getId(){return id;} public String getName(){return name;} public void setName(String n){this.name=n;}
    @Override public String toString(){return "Department{id="+id+", name='"+name+"'}";}
}


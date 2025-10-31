package org.hibernate.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity @Table(name = "fees")
public class Fees {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Integer id;
    private BigDecimal amount; private String description;
    public Fees(){} public Fees(BigDecimal amount,String description){this.amount=amount;this.description=description;}
    public Integer getId(){return id;} public BigDecimal getAmount(){return amount;} public void setAmount(BigDecimal a){this.amount=a;}
    public String getDescription(){return description;} public void setDescription(String d){this.description=d;}
    @Override public String toString(){return "Fees{id="+id+", amount="+amount+", desc='"+description+"'}";}
}


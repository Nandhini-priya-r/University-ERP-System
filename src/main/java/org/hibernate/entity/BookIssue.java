package org.hibernate.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "book_issues")
public class BookIssue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Library book;

    @ManyToOne
    private Member member;

    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private float fine;
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "librarian_id")
    private Librarian librarian;


    public BookIssue() {}

    public BookIssue(Library book, Member member, LocalDate issueDate, LocalDate dueDate) {
        this.book = book;
        this.member = member;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
    }

    public int getId() { return id; }
    public Library getBook() { return book; }
    public void setBook(Library book) { this.book = book; }
    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public float getFine() { return fine; }
    public void setFine(float fine) { this.fine = fine; }
    
    public Librarian getLibrarian() { return librarian; }
    public void setLibrarian(Librarian librarian) { this.librarian = librarian; }

    @Override
    public String toString() {
        return "BookIssue{id=" + id + ", book=" + book.getTitle() + ", member=" + member.getName() +
                ", issueDate=" + issueDate + ", dueDate=" + dueDate +
                ", returnDate=" + returnDate + ", fine=" + fine + '}';
    }
}

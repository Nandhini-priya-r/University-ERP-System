package org.hibernate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "library_items")
public class Library {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    private String author;
    private String isbn;
    private String category;
    private int totalCopies;
    private int availableCopies;

    public Library() {}

    public Library(String title, String author, String isbn, String category, int totalCopies) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }
    
    public Library(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    // ===== Getters & Setters =====
    public Integer getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    @Override
    public String toString() {
        return "Library{id=" + id + ", title='" + title + '\'' + ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' + ", category='" + category + '\'' +
                ", totalCopies=" + totalCopies + ", availableCopies=" + availableCopies + '}';
    }
}

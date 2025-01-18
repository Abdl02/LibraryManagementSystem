package com.library.management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false)
    private boolean available;

    public Book(String title, String author, boolean available) {
        this.title = title;
        this.author = author;
        this.available = available;
    }
}
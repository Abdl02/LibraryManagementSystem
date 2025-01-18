package com.library.management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false, length = 10)
    private String status;

    public Transaction(Book book, User user, Date date, String status) {
        this.book = book;
        this.user = user;
        this.date = date;
        this.status = status;
    }
}
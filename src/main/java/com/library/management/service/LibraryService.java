package com.library.management.service;

import com.library.management.model.Book;
import com.library.management.model.User;

import java.util.List;


public interface LibraryService {

    void addBook(Book book);
    void removeBook(int bookId);
    void borrowBook(int userId, int bookId);
    void returnBook(int userId, int bookId);

    List<Book> searchBooks(String keyword);
    List<Book> listAllBooks();
}

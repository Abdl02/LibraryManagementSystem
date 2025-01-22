package com.library.management.dao;

import com.library.management.model.Book;

import java.util.List;

public interface BookDAO extends GenericDAO<Book> {
    List<Book> search(String keyword);
    List<Book> listAvailableBooks();
}
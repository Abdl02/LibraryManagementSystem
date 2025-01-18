package com.library.management.service;

import com.library.management.dao.BookDAO;
import com.library.management.dao.GenericDAO;
import com.library.management.dao.TransactionDAO;
import com.library.management.dao.UserDAO;
import com.library.management.exceptions.BookNotFoundException;
import com.library.management.exceptions.BookUnavailableException;
import com.library.management.model.Book;
import com.library.management.model.Transaction;
import com.library.management.model.User;
import com.library.management.util.InputValidator;
import com.library.management.util.LoggerUtil;
import com.library.management.util.ThreadManager;
import com.library.management.util.DataBackupTask;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LibraryServiceImpl implements LibraryService {

    private final GenericDAO<User> userDAO;
    private final GenericDAO<Book> bookDAO;
    private final GenericDAO<Transaction> transactionDAO;

    public LibraryServiceImpl(GenericDAO<User> userDAO, GenericDAO<Book> bookDAO, GenericDAO<Transaction> transactionDAO) {
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
        this.transactionDAO = transactionDAO;

        DataBackupTask.startScheduledBackup(userDAO, bookDAO, transactionDAO);
    }

    @Override
    public void addBook(Book book) {
        if (!InputValidator.isValidName(book.getTitle()) || !InputValidator.isValidName(book.getAuthor())) {
            throw new IllegalArgumentException("Invalid book title or author format.");
        }
        bookDAO.add(book);
        LoggerUtil.logAction("Added book: " + book.getTitle() + " (ID: " + book.getId() + ")");
    }

    @Override
    public void removeBook(int bookId) {
        Book book = bookDAO.getById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found.");
        }
        bookDAO.remove(bookId);
        LoggerUtil.logAction("Removed book with ID: " + bookId);
    }

    @Override
    public void borrowBook(int userId, int bookId) {
        Book book = bookDAO.getById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found.");
        }
        if (!book.isAvailable()) {
            throw new BookUnavailableException("Book with ID " + bookId + " is currently unavailable.");
        }

        book.setAvailable(false);
        bookDAO.update(book);

        User user = userDAO.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }

        Transaction transaction = new Transaction(book, user, new java.util.Date(), "BORROW");
        transactionDAO.add(transaction);

        LoggerUtil.logAction("User ID: " + userId + " borrowed book ID: " + bookId);
    }

    @Override
    public void returnBook(int userId, int bookId) {
        Book book = bookDAO.getById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found.");
        }
        if (book.isAvailable()) {
            throw new IllegalStateException("Book with ID " + bookId + " is already returned.");
        }

        book.setAvailable(true);
        bookDAO.update(book);

        User user = userDAO.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " not found.");
        }

        Transaction transaction = new Transaction(book, user, new java.util.Date(), "RETURN");
        transactionDAO.add(transaction);

        LoggerUtil.logAction("User ID: " + userId + " returned book ID: " + bookId);
    }

    @Override
    public List<Book> searchBooks(String keyword) {
        return bookDAO.listAll();
    }

    @Override
    public List<Book> listAllBooks() {
        return bookDAO.listAll();
    }

    public void sortBooksByTitle(List<Book> books) {
        books.sort(Comparator.comparing(Book::getTitle));
    }

    public void sortBooksByAuthor(List<Book> books) {
        books.sort(Comparator.comparing(Book::getAuthor));
    }

    public List<Book> sortBooksById() {
        List<Book> books = bookDAO.listAll();
        books.sort(Comparator.comparingInt(Book::getId));
        return books;
    }

    public Book binarySearchByTitle(List<Book> books, String title) {
        books.sort(Comparator.comparing(Book::getTitle));
        return books.stream()
                .filter(book -> book.getTitle().equalsIgnoreCase(title))
                .findFirst()
                .orElse(null);
    }

    public Book binarySearchByAuthor(List<Book> books, String author) {
        books.sort(Comparator.comparing(Book::getAuthor));
        return books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .findFirst()
                .orElse(null);
    }
}
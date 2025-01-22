package com.library.management.service;

import com.library.management.dao.GenericDAO;
import com.library.management.exceptions.BookNotFoundException;
import com.library.management.exceptions.BookUnavailableException;
import com.library.management.model.Book;
import com.library.management.model.Transaction;
import com.library.management.model.User;
import com.library.management.util.DataBackupTask;
import com.library.management.util.InputValidator;
import com.library.management.util.LoggerUtil;

import java.util.Comparator;
import java.util.List;

public class LibraryServiceImpl implements LibraryService {

    private final GenericDAO<User> userDAO;
    private final GenericDAO<Book> bookDAO;
    private final GenericDAO<Transaction> transactionDAO;

    /**
     * Constructor for initialize the library services.
     *
     * @param userDAO DAO implementation for user operations.
     * @param bookDAO DAO implementation for book operations.
     * @param transactionDAO DAO implementation for transaction operations.
     */
    public LibraryServiceImpl(GenericDAO<User> userDAO, GenericDAO<Book> bookDAO, GenericDAO<Transaction> transactionDAO) {
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
        this.transactionDAO = transactionDAO;

        DataBackupTask.startScheduledBackup(userDAO, bookDAO, transactionDAO);
    }

    /**
     * * Add a new book with the name of its author.
     *
     * @param book The book to be added.
     * @throws IllegalArgumentException if the book title name or book author name are invalid.
     */
    @Override
    public void addBook(Book book) {
        if (!InputValidator.isValidName(book.getTitle()) || !InputValidator.isValidName(book.getAuthor())) {
            throw new IllegalArgumentException("Invalid book title or author format.");
        }
        bookDAO.add(book);
        LoggerUtil.logAction("Added book: " + book.getTitle() + " (ID: " + book.getId() + ")");
    }

    /**
     * Remove a book by its id.
     *
     * @param bookId The id of book to be deleted.
     * @throws BookNotFoundException if the book that will deleted are not found.
     */
    @Override
    public void removeBook(int bookId) {
        Book book = bookDAO.getById(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with ID " + bookId + " not found.");
        }
        bookDAO.remove(bookId);
        LoggerUtil.logAction("Removed book with ID: " + bookId);
    }

    /**
     * Allows a user to borrow a book.
     *
     * @param userId the id of the user borrowing the book.
     * @param bookId the id of the book to be borrowed.
     */
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

    /**
     * Allows a user to return a book.
     *
     * @param userId the id of the user returning the book.
     * @param bookId the id of the book to be returned.
     */
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

    /**
     * Searching about books that have the keywords in its name.
     *
     * @param keyword The keywords that in book name that make it easier to find the book.
     * @return a list of books matching the keyword.
     */
    @Override
    public List<Book> searchBooks(String keyword) {
        return bookDAO.listAll();
    }

    /**
     * Retrieves all books from the system.
     *
     * @return a list of all books.
     */
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
package com.library.management.test;

import com.library.management.dao.implementation.Book;
import com.library.management.dao.GenericDAO;
import com.library.management.util.DatabaseManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookDAOTest {

    private GenericDAO<com.library.management.model.Book> bookDAO;

    @BeforeEach
    public void setup() {
        bookDAO = new Book();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS books (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "title VARCHAR(100), " +
                    "author VARCHAR(100), " +
                    "available BOOLEAN)";
            stmt.execute(createTableQuery);
            stmt.execute("DELETE FROM books");
        } catch (Exception e) {
            fail("Failed to set up test database: " + e.getMessage());
        }
    }

    @Test
    public void testAddBook() {
        com.library.management.model.Book book = new com.library.management.model.Book("1984", "George Orwell", true);
        bookDAO.add(book);

        List<com.library.management.model.Book> books = bookDAO.listAll();
        assertFalse(books.isEmpty(), "Book list should not be empty after adding a book.");
        assertEquals(1, books.size());

        com.library.management.model.Book retrievedBook = books.get(0);
        assertNotNull(retrievedBook, "Retrieved book should not be null.");
        assertEquals("1984", retrievedBook.getTitle());
        assertEquals("George Orwell", retrievedBook.getAuthor());
        assertTrue(retrievedBook.isAvailable());
    }

    @Test
    public void testGetById() {
        com.library.management.model.Book book = new com.library.management.model.Book("Brave New World", "Aldous Huxley", false);
        bookDAO.add(book);

        List<com.library.management.model.Book> books = bookDAO.listAll();
        assertFalse(books.isEmpty(), "Book list should not be empty after adding a book.");

        com.library.management.model.Book retrievedBook = bookDAO.getById(books.get(0).getId());
        assertNotNull(retrievedBook, "Retrieved book by ID should not be null.");
        assertEquals("Brave New World", retrievedBook.getTitle());
        assertEquals("Aldous Huxley", retrievedBook.getAuthor());
        assertFalse(retrievedBook.isAvailable());
    }

    @Test
    public void testListAllBooks() {
        bookDAO.add(new com.library.management.model.Book("The Catcher in the Rye", "J.D. Salinger", true));
        bookDAO.add(new com.library.management.model.Book("To Kill a Mockingbird", "Harper Lee", false));

        List<com.library.management.model.Book> books = bookDAO.listAll();
        assertEquals(2, books.size(), "Book list size should match the number of books added.");
    }

    @Test
    public void testEmptyBookList() {
        List<com.library.management.model.Book> books = bookDAO.listAll();
        assertTrue(books.isEmpty(), "Book list should be empty if no books are added.");
    }

    @AfterEach
    public void cleanup() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE books");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


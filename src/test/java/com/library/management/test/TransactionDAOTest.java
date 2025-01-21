package com.library.management.test;

import com.library.management.dao.implementation.Book;
import com.library.management.dao.GenericDAO;
import com.library.management.dao.implementation.Transaction;
import com.library.management.dao.implementation.User;
import com.library.management.util.DatabaseManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionDAOTest {

    private GenericDAO<com.library.management.model.Transaction> transactionDAO;
    private GenericDAO<com.library.management.model.User> userDAO;
    private GenericDAO<com.library.management.model.Book> bookDAO;

    @BeforeEach
    public void setup() {
        transactionDAO = new Transaction();
        userDAO = new User();
        bookDAO = new Book();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "user_id INT, " +
                    "book_id INT, " +
                    "date DATE, " +
                    "status VARCHAR(10))";
            stmt.execute(createTableQuery);
            stmt.execute("DELETE FROM transactions");
        } catch (Exception e) {
            fail("Failed to set up test database: " + e.getMessage());
        }
    }

    @Test
    public void testAddTransaction() {
        com.library.management.model.User user = new com.library.management.model.User("John Doe", "johndoe@example.com");
        com.library.management.model.Book book = new com.library.management.model.Book("Effective Java", "Joshua Bloch", true);

        userDAO.add(user);
        bookDAO.add(book);

        com.library.management.model.Transaction transaction = new com.library.management.model.Transaction(book, user, Date.valueOf("2025-01-10"), "RETURN");
        transactionDAO.add(transaction);

        List<com.library.management.model.Transaction> transactions = transactionDAO.listAll();
        assertFalse(transactions.isEmpty(), "Transaction list should not be empty after adding a transaction.");
        assertEquals(1, transactions.size());
        assertEquals(user.getId(), transactions.get(0).getUser().getId());
        assertEquals(book.getId(), transactions.get(0).getBook().getId());
    }

    @Test
    public void testListAllTransactions_Empty() {
        List<com.library.management.model.Transaction> transactions = transactionDAO.listAll();
        assertTrue(transactions.isEmpty(), "Transaction list should be empty if no transactions are added.");
    }

    @AfterEach
    public void cleanup() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS transactions");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


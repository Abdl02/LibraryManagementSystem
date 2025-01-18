package com.library.management.test;

import com.library.management.dao.GenericDAO;
import com.library.management.dao.implementation.User;
import com.library.management.util.DatabaseManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOTest {

    private GenericDAO<com.library.management.model.User> userDAO;

    @BeforeEach
    public void setup() {
        userDAO = new User();
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "email VARCHAR(100))";
            stmt.execute(createTableQuery);

            stmt.execute("DELETE FROM users");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to set up test database");
        }
    }

    @Test
    public void testAddUser() {
        com.library.management.model.User user = new com.library.management.model.User("John Doe", "john.doe@example.com");
        userDAO.add(user);

        List<com.library.management.model.User> users = userDAO.listAll();
        assertEquals(1, users.size());

        com.library.management.model.User retrievedUser = users.get(0);
        assertNotNull(retrievedUser);
        assertEquals("John Doe", retrievedUser.getName());
        assertEquals("john.doe@example.com", retrievedUser.getEmail());
    }

    @Test
    public void testGetById() {
        com.library.management.model.User user = new com.library.management.model.User("Jane Smith", "jane.smith@example.com");
        userDAO.add(user);

        List<com.library.management.model.User> users = userDAO.listAll();
        assertEquals(1, users.size());

        com.library.management.model.User retrievedUser = userDAO.getById(users.get(0).getId());
        assertNotNull(retrievedUser);
        assertEquals("Jane Smith", retrievedUser.getName());
        assertEquals("jane.smith@example.com", retrievedUser.getEmail());
    }

    @Test
    public void testListAllUsers() {
        userDAO.add(new com.library.management.model.User("Alice", "alice@example.com"));
        userDAO.add(new com.library.management.model.User("Bob", "bob@example.com"));

        List<com.library.management.model.User> users = userDAO.listAll();
        assertEquals(2, users.size());
    }

    @AfterAll
    public void cleanup() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE users");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

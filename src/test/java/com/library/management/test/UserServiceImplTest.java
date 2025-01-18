package com.library.management.test;

import com.library.management.dao.implementation.User;
import com.library.management.service.UserService;
import com.library.management.service.UserServiceImpl;
import com.library.management.util.DatabaseManager;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceImplTest {

    private UserService userService;
    private User userDAO;

    @BeforeEach
    public void setup() {
        userDAO = new User();
        userService = new UserServiceImpl(userDAO);

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(100), " +
                    "email VARCHAR(100))";
            stmt.execute(createTableQuery);
            stmt.execute("DELETE FROM users");
        } catch (SQLException e) {
            e.printStackTrace();
            fail("Failed to set up test database");
        }
    }

    @Test
    public void testRegisterUser_Success() {
        com.library.management.model.User user = new com.library.management.model.User("John Doe", "john.doe@example.com");
        assertDoesNotThrow(() -> userService.registerUser(user));

        List<com.library.management.model.User> users = userDAO.listAll();
        assertEquals(1, users.size());

        com.library.management.model.User retrievedUser = users.get(0);
        assertNotNull(retrievedUser);
        assertEquals("John Doe", retrievedUser.getName());
        assertEquals("john.doe@example.com", retrievedUser.getEmail());
    }

    @Test
    public void testRegisterUser_InvalidEmail() {
        com.library.management.model.User user = new com.library.management.model.User("John Doe", "invalid-email");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
        assertEquals("Invalid user input: Name or Email is incorrect.", exception.getMessage());
    }

    @Test
    public void testRegisterUser_InvalidName() {
        com.library.management.model.User user = new com.library.management.model.User("J", "john.doe@example.com");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.registerUser(user));
        assertEquals("Invalid user input: Name or Email is incorrect.", exception.getMessage());
    }

    @Test
    public void testLoginUser_Success() {
        com.library.management.model.User user = new com.library.management.model.User("Jane Smith", "jane.smith@example.com");
        userService.registerUser(user);

        com.library.management.model.User loggedInUser = userService.loginUser("jane.smith@example.com");
        assertNotNull(loggedInUser);
        assertEquals("Jane Smith", loggedInUser.getName());
    }

    @Test
    public void testLoginUser_InvalidEmail() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.loginUser("invalid-email"));
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    public void testLoginUser_UserNotFound() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userService.loginUser("notfound@example.com"));
        assertEquals("User with the provided email does not exist.", exception.getMessage());
    }

    @Test
    public void testListAllUsers() {
        userService.registerUser(new com.library.management.model.User("Alice", "alice@example.com"));
        userService.registerUser(new com.library.management.model.User("Bob", "bob@example.com"));

        List<com.library.management.model.User> users = userService.listAllUsers();
        assertEquals(2, users.size());
    }

    @AfterAll
    public void cleanup() {
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
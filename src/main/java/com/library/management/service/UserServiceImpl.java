package com.library.management.service;

import com.library.management.dao.GenericDAO;
import com.library.management.model.User;
import com.library.management.util.InputValidator;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final GenericDAO<User> userDAO;

    /**
     * Constructor for initialize the user services.
     *
     * @param userDAO DAO implementation for user operations.
     */
    public UserServiceImpl(GenericDAO<User> userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user in the system according to our input validator.
     *
     * @param user The user to be registered.
     * @throws IllegalArgumentException if the user's name or email is invalid.
     */
    @Override
    public void registerUser(User user) {
        if (InputValidator.isValidEmail(user.getEmail()) && InputValidator.isValidName(user.getName())) {
            userDAO.add(user);
        } else {
            throw new IllegalArgumentException("Invalid user input: Name or Email is incorrect.");
        }
    }

    /**
     * Logs in a user by verifying their email.
     *
     * @param email The email of the user attempting to log in.
     * @return The user object if the email matches an existing user.
     * @throws IllegalArgumentException if the email is invalid or the user does not exist.
     */
    @Override
    public User loginUser(String email) {
        if (InputValidator.isValidEmail(email)) {
            List<User> users = userDAO.listAll();
            for (User user : users) {
                if (user.getEmail().equalsIgnoreCase(email)) {
                    return user;
                }
            }
            throw new IllegalArgumentException("User with the provided email does not exist.");
        } else {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }

    /**
     * Retrieves a list of all users in the system.
     *
     * @return A list of all registered users.
     */
    @Override
    public List<User> listAllUsers() {
        return userDAO.listAll();
    }
}

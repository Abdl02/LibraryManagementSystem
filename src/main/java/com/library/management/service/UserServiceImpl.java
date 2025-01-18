package com.library.management.service;

import com.library.management.dao.GenericDAO;
import com.library.management.model.User;
import com.library.management.util.InputValidator;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final GenericDAO<User> userDAO;

    public UserServiceImpl(GenericDAO<User> userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void registerUser(User user) {
        if (InputValidator.isValidEmail(user.getEmail()) && InputValidator.isValidName(user.getName())) {
            userDAO.add(user);
        } else {
            throw new IllegalArgumentException("Invalid user input: Name or Email is incorrect.");
        }
    }

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

    @Override
    public List<User> listAllUsers() {
        return userDAO.listAll();
    }
}

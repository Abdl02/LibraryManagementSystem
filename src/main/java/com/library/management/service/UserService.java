package com.library.management.service;

import com.library.management.model.User;

import java.util.List;

public interface UserService {
    void registerUser(User user);

    User loginUser(String email);
    List<User> listAllUsers();
}

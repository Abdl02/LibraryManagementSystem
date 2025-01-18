package com.library.management.dao;

import com.library.management.model.User;

import java.util.List;

public interface UserDAO extends GenericDAO<User> {
    User findByEmail(String email);
    User loginUser(String email);
}
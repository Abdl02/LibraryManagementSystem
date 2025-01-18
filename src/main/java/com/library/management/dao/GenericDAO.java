package com.library.management.dao;

import java.util.List;

public interface GenericDAO<T> {
    void add(T entity);
    void update(T entity);
    void remove(int id);
    T getById(int id);
    List<T> listAll();
}

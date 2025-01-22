package com.library.management.dao;

import java.util.List;

/**
 * A generic Data Access Object (DAO) interface to provide common CRUD operations.
 *
 * <p>
 * This interface is designed to be reusable for different entity types
 * (e.g., User, Book, Transaction) by utilizing Java Generics. It defines
 * the core database operations that are common across all types of entities,
 * ensuring consistency and reducing code duplication.
 * </p>
 *
 * @param <T> the type of the entity that this DAO manages (e.g., User, Book, Transaction).
 */
public interface GenericDAO<T> {
    void add(T entity);
    void update(T entity);
    void remove(int id);
    T getById(int id);
    List<T> listAll();
}

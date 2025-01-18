package com.library.management.dao.implementation;

import com.library.management.dao.BookDAO;
import com.library.management.dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class Book implements BookDAO, GenericDAO<com.library.management.model.Book> {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("libraryPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(com.library.management.model.Book book) {
        em.getTransaction().begin();
        if (book.getId() == 0) {
            em.persist(book);
        } else {
            em.merge(book);
        }
        em.getTransaction().commit();
    }

    @Override
    public void update(com.library.management.model.Book book) {
        em.getTransaction().begin();
        em.merge(book);
        em.getTransaction().commit();
    }

    @Override
    public void remove(int id) {
        em.getTransaction().begin();
        com.library.management.model.Book book = em.find(com.library.management.model.Book.class, id);
        if (book != null) {
            em.remove(book);
        }
        em.getTransaction().commit();
    }

    @Override
    public com.library.management.model.Book getById(int id) {
        return em.find(com.library.management.model.Book.class, id);
    }

    @Override
    public List<com.library.management.model.Book> listAll() {
        return em.createQuery("SELECT b FROM Book b", com.library.management.model.Book.class).getResultList();
    }

    public List<com.library.management.model.Book> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty.");
        }
        String query = "SELECT b FROM Book b WHERE LOWER(b.title) LIKE :keyword OR LOWER(b.author) LIKE :keyword";
        return em.createQuery(query, com.library.management.model.Book.class)
                .setParameter("keyword", "%" + keyword.toLowerCase() + "%")
                .getResultList();
    }

    @Override
    public List<com.library.management.model.Book> listAvailableBooks() {
        return em.createQuery("SELECT b FROM Book b WHERE b.available = true", com.library.management.model.Book.class).getResultList();
    }
}
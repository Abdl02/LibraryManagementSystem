package com.library.management.dao.implementation;

import com.library.management.dao.GenericDAO;
import com.library.management.dao.UserDAO;
import com.library.management.util.InputValidator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class User implements UserDAO, GenericDAO<com.library.management.model.User> {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("libraryPU");
    private final EntityManager em = emf.createEntityManager();
    private User userDAO;

    @Override
    public void add(com.library.management.model.User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }

    @Override
    public void update(com.library.management.model.User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();
    }

    @Override
    public void remove(int id) {
        em.getTransaction().begin();
        com.library.management.model.User user = em.find(com.library.management.model.User.class, id);
        if (user != null) {
            em.remove(user);
        }
        em.getTransaction().commit();
    }

    @Override
    public com.library.management.model.User getById(int id) {
        return em.find(com.library.management.model.User.class, id);
    }

    @Override
    public List<com.library.management.model.User> listAll() {
        try {
            return em.createQuery("SELECT u FROM User u", com.library.management.model.User.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage(), e);
        }
    }

    @Override
    public com.library.management.model.User findByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.email = :email", com.library.management.model.User.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error finding user by email: " + e.getMessage(), e);
        }
    }

    @Override
    public com.library.management.model.User loginUser(String email) {
        if (!InputValidator.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format.");
        }
        com.library.management.model.User user = userDAO.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("User with the provided email does not exist.");
        }
        return user;
    }

}
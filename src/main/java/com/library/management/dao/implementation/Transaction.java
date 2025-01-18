package com.library.management.dao.implementation;

import com.library.management.dao.GenericDAO;
import com.library.management.dao.TransactionDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class Transaction implements TransactionDAO, GenericDAO<com.library.management.model.Transaction> {

    private static final Logger logger = LoggerFactory.getLogger(Transaction.class);
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("libraryPU");
    private final EntityManager em = emf.createEntityManager();

    @Override
    public void add(com.library.management.model.Transaction transaction) {
        if (transaction == null || transaction.getUser() == null || transaction.getBook() == null) {
            throw new IllegalArgumentException("Transaction, user, or book cannot be null.");
        }
        em.getTransaction().begin();
        em.persist(transaction);
        em.getTransaction().commit();
        logger.info("Transaction added successfully: {}", transaction);
    }

    @Override
    public void update(com.library.management.model.Transaction transaction) {
        em.getTransaction().begin();
        em.merge(transaction);
        em.getTransaction().commit();
    }

    @Override
    public void remove(int id) {
        em.getTransaction().begin();
        com.library.management.model.Transaction transaction = em.find(com.library.management.model.Transaction.class, id);
        if (transaction != null) {
            em.remove(transaction);
        }
        em.getTransaction().commit();
    }

    @Override
    public com.library.management.model.Transaction getById(int id) {
        return em.find(com.library.management.model.Transaction.class, id);
    }

    @Override
    public List<com.library.management.model.Transaction> listAll() {
        return em.createQuery("SELECT t FROM Transaction t", com.library.management.model.Transaction.class).getResultList();
    }

    @Override
    public List<com.library.management.model.Transaction> listTransactionsByUserId(int userId) {
        return em.createQuery("SELECT t FROM Transaction t WHERE t.user.id = :userId", com.library.management.model.Transaction.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<com.library.management.model.Transaction> listOverdueTransactions() {
        return List.of();
    }
}
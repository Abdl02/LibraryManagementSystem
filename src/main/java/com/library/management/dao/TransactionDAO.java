package com.library.management.dao;

import com.library.management.model.Transaction;
import jakarta.persistence.EntityManager;

import java.util.List;

public interface TransactionDAO extends GenericDAO<Transaction> {
    List<Transaction> listTransactionsByUserId(int userId);
    List<Transaction> listOverdueTransactions();
}

package com.library.management.util;

import com.library.management.dao.BookDAO;
import com.library.management.dao.GenericDAO;
import com.library.management.dao.TransactionDAO;
import com.library.management.dao.UserDAO;
import com.library.management.model.Book;
import com.library.management.model.Transaction;
import com.library.management.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DataBackupTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DataBackupTask.class);

    private final GenericDAO<User> userDAO;
    private final GenericDAO<Book> bookDAO;
    private final GenericDAO<Transaction> transactionDAO;

    public DataBackupTask(GenericDAO<User> userDAO, GenericDAO<Book> bookDAO, GenericDAO<Transaction> transactionDAO) {
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
        this.transactionDAO = transactionDAO;
    }

    @Override
    public void run() {
        try {
            logger.info("Starting data backup...");

            List<User> users = userDAO.listAll();
            writeToFile("backup_users.txt", users);

            List<Book> books = bookDAO.listAll();
            writeToFile("backup_books.txt", books);

            List<Transaction> transactions = transactionDAO.listAll();
            writeToFile("backup_transactions.txt", transactions);

            logger.info("Data backup completed successfully.");
        } catch (IOException e) {
            logger.error("Error occurred during data backup", e);
        }
    }

    private static final String BACKUP_DIR = "src/main/resources/";

    private <T> void writeToFile(String fileName, List<T> data) throws IOException {
        String filePath = BACKUP_DIR + fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }

    public static void startScheduledBackup(GenericDAO<User> userDAO, GenericDAO<Book> bookDAO, GenericDAO<Transaction> transactionDAO) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new DataBackupTask(userDAO, bookDAO, transactionDAO), 0, 10, TimeUnit.MINUTES);
    }
}
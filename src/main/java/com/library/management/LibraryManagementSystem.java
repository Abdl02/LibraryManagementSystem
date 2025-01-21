package com.library.management;

import com.library.management.dao.implementation.Book;
import com.library.management.dao.implementation.Transaction;
import com.library.management.dao.implementation.User;
import com.library.management.service.LibraryService;
import com.library.management.service.LibraryServiceImpl;
import com.library.management.service.UserService;
import com.library.management.service.UserServiceImpl;
import com.library.management.util.DatabaseManager;
import com.library.management.dao.GenericDAO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        System.setProperty("h2.console.enabled", "true");
        DatabaseManager.initializeDatabase();

        GenericDAO<com.library.management.model.User> userDAO = new User();
        GenericDAO<com.library.management.model.Book> bookDAO = new Book();
        GenericDAO<com.library.management.model.Transaction> transactionDAO = new Transaction();
        LibraryService libraryService = new LibraryServiceImpl(userDAO, bookDAO, transactionDAO);
        UserService userService = new UserServiceImpl(userDAO);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             PrintWriter writer = new PrintWriter(System.out, true)) {

            writer.println("Welcome to the Library Management System");

            while (true) {
                writer.println("\nSelect an option:");
                writer.println("1. Register User");
                writer.println("2. Login User");
                writer.println("3. Add Book");
                writer.println("4. Remove Book");
                writer.println("5. Borrow Book");
                writer.println("6. Return Book");
                writer.println("7. List All Books");
                writer.println("8. Search Books");
                writer.println("9. List All Users");
                writer.println("0. Exit");

                writer.print("Enter your choice: ");
                writer.flush();
                int choice;

                try {
                    choice = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    writer.println("Invalid input. Please enter a valid number.");
                    continue;
                }

                switch (choice) {
                    case 1:
                        writer.print("Enter user name: ");
                        writer.flush();
                        String userName = reader.readLine();
                        writer.print("Enter user email: ");
                        writer.flush();
                        String userEmail = reader.readLine();
                        try {
                            userService.registerUser(new com.library.management.model.User(userName, userEmail));
                            writer.println("User registered successfully.");
                        } catch (IllegalArgumentException e) {
                            writer.println("Error: " + e.getMessage());
                        }
                        break;

                    case 2:
                        writer.print("Enter user email to login: ");
                        writer.flush();
                        String loginEmail = reader.readLine();
                        try {
                            com.library.management.model.User loggedInUser = userService.loginUser(loginEmail);
                            writer.println("Welcome, " + loggedInUser.getName() + "!");
                        } catch (IllegalArgumentException e) {
                            writer.println("Error: " + e.getMessage());
                        }
                        break;

                    case 3:
                        writer.print("Enter book title: ");
                        writer.flush();
                        String title = reader.readLine();
                        writer.print("Enter book author: ");
                        writer.flush();
                        String author = reader.readLine();
                        libraryService.addBook(new com.library.management.model.Book(title, author, true));
                        writer.println("Book added successfully.");
                        break;

                    case 4:
                        writer.print("Enter book ID to remove: ");
                        writer.flush();
                        int removeId;
                        try {
                            removeId = Integer.parseInt(reader.readLine());
                        } catch (NumberFormatException e) {
                            writer.println("Invalid book ID. Please try again.");
                            break;
                        }
                        try {
                            libraryService.removeBook(removeId);
                            writer.println("Book removed successfully.");
                        } catch (RuntimeException e) {
                            writer.println("Error: " + e.getMessage());
                        }
                        break;

                    case 5:
                        writer.print("Enter user ID: ");
                        writer.flush();
                        int borrowUserId;
                        int borrowBookId;
                        try {
                            borrowUserId = Integer.parseInt(reader.readLine());
                            writer.print("Enter book ID: ");
                            writer.flush();
                            borrowBookId = Integer.parseInt(reader.readLine());
                        } catch (NumberFormatException e) {
                            writer.println("Invalid input. Please enter numeric IDs.");
                            break;
                        }
                        try {
                            libraryService.borrowBook(borrowUserId, borrowBookId);
                            writer.println("Book borrowed successfully.");
                        } catch (IllegalStateException e) {
                            writer.println("Error: " + e.getMessage());
                        }
                        break;

                    case 6:
                        writer.print("Enter user ID: ");
                        writer.flush();
                        int returnUserId;
                        int returnBookId;
                        try {
                            returnUserId = Integer.parseInt(reader.readLine());
                            writer.print("Enter book ID: ");
                            writer.flush();
                            returnBookId = Integer.parseInt(reader.readLine());
                        } catch (NumberFormatException e) {
                            writer.println("Invalid input. Please enter numeric IDs.");
                            break;
                        }
                        try {
                            libraryService.returnBook(returnUserId, returnBookId);
                            writer.println("Book returned successfully.");
                        } catch (IllegalStateException e) {
                            writer.println("Error: " + e.getMessage());
                        }
                        break;

                    case 7:
                        List<com.library.management.model.Book> books = libraryService.listAllBooks();
                        if (books.isEmpty()) {
                            writer.println("No books available in the system.");
                        } else {
                            writer.println("Listing all books:");
                            books.forEach(book ->
                                    writer.println("ID: " + book.getId() + ", Title: " + book.getTitle() +
                                            ", Author: " + book.getAuthor() + ", Available: " + book.isAvailable()));
                        }
                        break;

                    case 8:
                        writer.print("Enter keyword to search: ");
                        writer.flush();
                        String keyword = reader.readLine().trim();
                        if (keyword.isEmpty()) {
                            writer.println("Error: Search keyword cannot be empty.");
                        } else {
                            libraryService.searchBooks(keyword).forEach(book ->
                                    writer.println("ID: " + book.getId() + ", Title: " + book.getTitle() +
                                            ", Author: " + book.getAuthor() + ", Available: " + book.isAvailable()));
                        }
                        break;

                    case 9:
                        List<com.library.management.model.User> users = userService.listAllUsers();
                        if (users.isEmpty()) {
                            writer.println("No users found in the system.");
                        } else {
                            writer.println("Listing all users:");
                            users.forEach(user ->
                                    writer.println("ID: " + user.getId() + ", Name: " + user.getName() +
                                            ", Email: " + user.getEmail()));
                        }
                        break;

                    case 0:
                        writer.println("Exiting the system. Goodbye!");
                        System.exit(0);
                        break;

                    default:
                        writer.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

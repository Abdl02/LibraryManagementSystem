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

import java.util.Scanner;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        System.setProperty("h2.console.enabled", "true");
        DatabaseManager.initializeDatabase();

        GenericDAO<com.library.management.model.User> userDAO = new User();
        GenericDAO<com.library.management.model.Book> bookDAO = new Book();
        GenericDAO<com.library.management.model.Transaction> transactionDAO = new Transaction();
        LibraryService libraryService = new LibraryServiceImpl(userDAO, bookDAO, transactionDAO);
        UserService userService = new UserServiceImpl(userDAO);

        System.out.println("Welcome to the Library Management System");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Register User");
            System.out.println("2. Login User");
            System.out.println("3. Add Book");
            System.out.println("4. Remove Book");
            System.out.println("5. Borrow Book");
            System.out.println("6. Return Book");
            System.out.println("7. List All Books");
            System.out.println("8. Search Books");
            System.out.println("9. List All Users");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter user name: ");
                    String userName = scanner.nextLine();
                    System.out.print("Enter user email: ");
                    String userEmail = scanner.nextLine();
                    try {
                        userService.registerUser(new com.library.management.model.User(userName, userEmail));
                        System.out.println("User registered successfully.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Enter user email to login: ");
                    String loginEmail = scanner.nextLine();
                    try {
                        com.library.management.model.User loggedInUser = userService.loginUser(loginEmail);
                        System.out.println("Welcome, " + loggedInUser.getName() + "!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 3:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book author: ");
                    String author = scanner.nextLine();
                    libraryService.addBook(new com.library.management.model.Book(title, author, true));
                    System.out.println("Book added successfully.");
                    break;

                case 4:
                    System.out.print("Enter book ID to remove: ");
                    int removeId = scanner.nextInt();
                    try {
                        libraryService.removeBook(removeId);
                        System.out.println("Book removed successfully.");
                    } catch (RuntimeException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.print("Enter user ID: ");
                    int borrowUserId = scanner.nextInt();
                    System.out.print("Enter book ID: ");
                    int borrowBookId = scanner.nextInt();
                    try {
                        libraryService.borrowBook(borrowUserId, borrowBookId);
                        System.out.println("Book borrowed successfully.");
                    } catch (IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 6:
                    System.out.print("Enter user ID: ");
                    int returnUserId = scanner.nextInt();
                    System.out.print("Enter book ID: ");
                    int returnBookId = scanner.nextInt();
                    try {
                        libraryService.returnBook(returnUserId, returnBookId);
                        System.out.println("Book returned successfully.");
                    } catch (IllegalStateException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 7:
                    System.out.println("Listing all books:");
                    libraryService.listAllBooks().forEach(book ->
                            System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() +
                                    ", Author: " + book.getAuthor() + ", Available: " + book.isAvailable()));
                    break;

                case 8:
                    System.out.print("Enter keyword to search: ");
                    String keyword = scanner.nextLine().trim();
                    if (keyword.isEmpty()) {
                        System.out.println("Error: Search keyword cannot be empty.");
                    } else {
                        libraryService.searchBooks(keyword).forEach(book ->
                                System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() +
                                        ", Author: " + book.getAuthor() + ", Available: " + book.isAvailable()));
                    }
                    break;

                case 9:
                    System.out.println("Listing all users:");
                    userService.listAllUsers().forEach(user ->
                            System.out.println("ID: " + user.getId() + ", Name: " + user.getName() +
                                    ", Email: " + user.getEmail()));
                    break;

                case 0:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

# Library Management System - README

## Project Overview

The **Library Management System** is a comprehensive, modular application developed to demonstrate advanced knowledge and application of Java programming concepts. The system allows users to efficiently manage a library, including operations such as searching for books, borrowing and returning books, user management, and more. 

This project adheres to clean code principles, design patterns, and Java best practices, ensuring maintainability, scalability, and readability.

---

## Key Features

- **Book Management:**
  - Add, update, delete, and list books.
  - Search books using advanced search techniques.
  - Check availability of books.
  
- **User Management:**
  - Register users and manage their information.
  - Allow users to log in using their email.
  
- **Transaction Management:**
  - Borrow and return books.
  - Log and track all borrowing and returning transactions.

- **Advanced Programming Concepts:**
  - Sorting and searching algorithms (e.g., binary search).
  - Exception handling for graceful error management.
  - Multi-threaded operations for background tasks like data backups.
  - Input validation using regular expressions (regex).

- **Persistence:**
  - CRUD operations for users, books, and transactions using **JDBC** and **JPA/ORM**.
  - Integration with an H2 in-memory database for testing and local storage.

- **Logging:**
  - Comprehensive logging for system actions and exceptions.

---

## Project Objectives

1. Demonstrate proficiency in **Java programming concepts**:
   - Collections and generics.
   - Lambda expressions and functional programming.
   - String manipulation techniques.
   - Multi-threading.
   
2. Adhere to **Object-Oriented Programming Principles**:
   - Encapsulation, Inheritance, Polymorphism, and Abstraction.
   
3. Showcase **clean code practices**:
   - SOLID principles.
   - Modular and reusable codebase.
   
4. Implement advanced **database operations** using JDBC and JPA:
   - CRUD operations for books, users, and transactions.

5. Ensure robust input validation and error handling.

6. Enable **file I/O operations**:
   - Export backups of library data to files.

---

## Installation and Setup

### Prerequisites
- **Java Development Kit (JDK)** (Version 8 or higher)
- **Apache Maven** for building the project
- **H2 Database Console** (included in the project)
- **Integrated Development Environment (IDE)** (e.g., IntelliJ IDEA or Eclipse)

### Steps to Run
1. Clone the repository to your local machine:
   ```bash
   git clone <repository-url>
   cd LibraryManagementSystem
   ```
   
2. Build the project using Maven:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   java -jar target/LibraryManagementSystem.jar
   ```

4. Access the H2 Database Console (optional for database inspection):
   - Open your browser and go to `http://localhost:8082`.
   - Use the following credentials:
     - **URL:** `jdbc:h2:~/librarydb`
     - **User:** `sa`
     - **Password:** *(leave empty)*

---

## Usage Instructions

1. Launch the application, and you will see the main menu with the following options:
   - Register User
   - Login User
   - Add Book
   - Remove Book
   - Borrow Book
   - Return Book
   - List All Books
   - Search Books
   - List All Users
   - Exit
   
2. Follow the on-screen instructions to perform operations.

---

## Technical Overview

### Design Patterns
- **DAO (Data Access Object):** Separates database access logic from business logic.
- **Singleton:** Used for database connection management.
- **Strategy:** Sorting and searching logic is modularized for extensibility.

### Tools and Technologies
- **Java 8+**
- **JPA/ORM (Hibernate)**
- **H2 Database**
- **SLF4J and Logback** for logging
- **JUnit** for unit testing
- **Maven** for dependency management and build automation

---

## Testing

The project includes unit tests for critical functionalities using **JUnit**:
- DAO operations (CRUD for books, users, and transactions).
- Library services (e.g., borrow, return, search).

Run the tests using Maven:
```bash
mvn test
```

---

## Known Limitations

1. **Scalability:** The system is designed for small-scale library management and might require architectural changes for larger deployments.
2. **Search Limitations:** The current search functionality uses basic keyword matching. Advanced filters (e.g., genre, year) can be added.
3. **UI/UX:** The project is console-based. A GUI or web-based interface could enhance usability.

---

## Future Enhancements

1. Integrate a graphical user interface (GUI) or web-based interface.
2. Extend search functionality with advanced filtering options.
3. Implement user roles and permissions (e.g., admin vs. regular user).
4. Optimize multi-threaded operations for high-performance environments.

---

## Authors and Contributors

This project was developed as part of a training program to demonstrate Java programming proficiency and adherence to clean code principles. Contributions and suggestions are welcome!

---

## License

This project is open-source and available under the **MIT License**. See the `LICENSE` file for more details.

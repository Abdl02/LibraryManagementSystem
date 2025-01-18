// Sorting and Searching Utility for Books
package com.library.management.util;

import com.library.management.model.Book;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BookUtils {

    // Method to sort books by title using Timsort (Collections.sort) //n * log(n)
    public static void sortBooksByTitle(List<Book> books) {
        Collections.sort(books, Comparator.comparing(Book::getTitle));
    }

    public static void sortBooksByAuthor(List<Book> books) {
        Collections.sort(books, Comparator.comparing(book -> book.getAuthor()));
    }

    public static void sortBooksById(List<Book> books) {
        Collections.sort(books, Comparator.comparingInt(book -> book.getId()));
    }

    public static Book binarySearchByTitle(List<Book> books, String title) {
        int left = 0, right = books.size() - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            Book midBook = books.get(mid);
            int comparison = midBook.getTitle().compareToIgnoreCase(title);

            if (comparison == 0) {
                return midBook;
            } else if (comparison < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return null;
    }

    public static void displayBooks(List<Book> books) {
        books.forEach(book -> System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Available: " + book.isAvailable()));
    }
}

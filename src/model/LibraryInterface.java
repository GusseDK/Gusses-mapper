package model;

import java.util.List;

public interface LibraryInterface {
    List<Book> getBooks();
    List<Patron> getPatrons();

    boolean reserveBook(String bookIsbn, int patronRegNo);
    boolean borrowBook(String bookIsbn, int patronRegNo);
    boolean returnBook(String bookIsbn, int patronRegNo);
}


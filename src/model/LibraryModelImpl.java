package model;

import java.util.ArrayList;
import java.util.List;

public class LibraryModelImpl implements LibraryInterface {

    private final List<Book> books = new ArrayList<>();
    private final List<Patron> patrons = new ArrayList<>();

    public LibraryModelImpl() {
        seedPatrons();
        seedBooks();
    }



    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public List<Patron> getPatrons() {
        return patrons;
    }

    @Override
    public boolean reserveBook(String bookIsbn, int patronRegNo) {
        Book book = findBookByIsbn(bookIsbn);
        Patron patron = findPatronByRegNo(patronRegNo);
        if (book == null || patron == null) return false;


        synchronized (book) {
            if (book.getReserver() != null) return false;
                return book.reserve(patron);
        }
    }

    @Override
    public boolean borrowBook(String bookIsbn, int patronRegNo) {
        Book book = findBookByIsbn(bookIsbn);
        Patron patron = findPatronByRegNo(patronRegNo);
        if (book == null || patron == null) return false;

        synchronized (book) {
            if (book.getBorrower() != null) return false;

            Patron reserver = book.getReserver();
            if (reserver != null && reserver != patron) return false;
                return book.borrow(patron);
        }
    }

    @Override
    public boolean returnBook(String bookIsbn, int patronRegNo) {
        Book book = findBookByIsbn(bookIsbn);
        if (book == null) return false;

        synchronized (book) {
            if (book.getBorrower() == null) return false;

            if (patronRegNo != -1) {
                Patron patron = findPatronByRegNo(patronRegNo);
                if (patron == null) return false;

                if (book.getBorrower() != patron) return false;
            }

            book.returnBook();
            return true;
        }
    }

    private void seedPatrons() {
        patrons.add(new Patron("Bob", 1001));
        patrons.add(new Patron("Wendy", 1002));
        patrons.add(new Patron("James", 1003));
    }

    private void seedBooks() {
        books.add(new Book("Java Programming", "John Doe", 2020, "978-0000000001"));
        books.add(new Book("Design Patterns", "GoF", 1994, "978-0000000002"));
        books.add(new Book("Clean Code", "Robert C. Martin", 2008, "978-0000000003"));
        books.add(new Book("The Hobbit", "J.R.R. Tolkien", 1937, "978-0000000004"));
    }


    private Book findBookByIsbn(String isbn) {
        if (isbn == null) return null;
        for (Book b : books) {
            if (isbn.equals(b.getIsbn())) return b;
        }
        return null;
    }

    private Patron findPatronByRegNo(int regNo) {
        for (Patron p : patrons) {
            if (p.getRegistrationNumber() == regNo) return p;
        }
        return null;
    }
}


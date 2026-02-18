package model;



public class Book {

    private String title;
    private String author;
    private int year;
    private String isbn;
    private Patron borrower;
    private Patron reserver;

    public Book(String title, String author, int year, String isbn) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.isbn = isbn;
    }

    public boolean borrow(Patron p) {
        if (borrower == null && (reserver == null || reserver == p)) {
            borrower = p;
            if (reserver == p) {
                reserver = null;
            }
            return true;
        }
        return false;
    }

    public boolean reserve(Patron p) {
        if (reserver == null) {
            reserver = p;
            return true;
        }
        return false;
    }

    public void returnBook() {
        borrower = null;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getIsbn() { return isbn; }
    public Patron getBorrower() { return borrower; }
    public Patron getReserver() { return reserver; }

    public boolean isAvailable() {
        return borrower == null && reserver == null;
    }

    public boolean isBorrowed() {
        return borrower != null;
    }

    public boolean isReserved() {
        return reserver != null;
    }
}

package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.LibraryInterface;
import model.Patron;

public class BibloController {

    // ---- FXML fields (skal matche fx:id i Biblo.fxml) ----
    @FXML private TableView<Book> booksTableView;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> stateColumn;

    @FXML private ComboBox<Patron> patronComboBox;

    @FXML private Button borrowBookButton;
    @FXML private Button reserveBookButton;
    @FXML private Button returnBookButton;
    @FXML private Button refreshBooksButton;

    // ---- Model reference ----
    private LibraryInterface model;

    // Kaldt automatisk af FXMLLoader
    @FXML
    private void initialize() {
        System.out.println("Controller loaded!");

        // Binder kolonner til Book.getTitle() og Book.getAuthor()
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        // State-kolonne er "beregnet" tekst ud fra borrower/reserver
        stateColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(buildStateText(cell.getValue()))
        );
    }

    // ---- Kaldes fra MainApp efter loader.load() ----
    public void setModel(LibraryInterface model) {
        this.model = model;

        // Fyld GUI med data
        patronComboBox.setItems(FXCollections.observableArrayList(model.getPatrons()));
        booksTableView.setItems(FXCollections.observableArrayList(model.getBooks()));
        booksTableView.refresh();
    }

    // ---- Button handlers (matcher onAction i FXML) ----
    @FXML
    private void onBorrowClicked() {
        if (model == null) return;

        Book book = booksTableView.getSelectionModel().getSelectedItem();
        Patron patron = patronComboBox.getSelectionModel().getSelectedItem();
        if (book == null || patron == null) {
            showInfo("Missing selection", "Vælg en bog og en patron først.");
            return;
        }

        boolean ok = model.borrowBook(book.getIsbn(), patron.getRegistrationNumber());
        if (!ok) {
            showInfo("Borrow failed", "Kunne ikke låne bogen.\n(Tjek om den allerede er lånt eller reserveret af en anden)");
        }
        refresh();
    }

    @FXML
    private void onReserveClicked() {
        if (model == null) return;

        Book book = booksTableView.getSelectionModel().getSelectedItem();
        Patron patron = patronComboBox.getSelectionModel().getSelectedItem();
        if (book == null || patron == null) {
            showInfo("Missing selection", "Vælg en bog og en patron først.");
            return;
        }

        boolean ok = model.reserveBook(book.getIsbn(), patron.getRegistrationNumber());
        if (!ok) {
            showInfo("Reserve failed", "Kunne ikke reservere bogen.\n(der er måske allerede en reservation)");
        }
        refresh();
    }

    @FXML
    private void onReturnClicked() {
        if (model == null) return;

        Book book = booksTableView.getSelectionModel().getSelectedItem();
        if (book == null) {
            showInfo("Missing selection", "Vælg en bog først.");
            return;
        }

        Patron patron = patronComboBox.getSelectionModel().getSelectedItem();
        int regNo = (patron == null) ? -1 : patron.getRegistrationNumber();

        boolean ok = model.returnBook(book.getIsbn(), regNo);
        if (!ok) {
            showInfo("Return failed", "Kunne ikke aflevere bogen.\n(Tjek om den er lånt, og evt. om patron matcher)");
        }
        refresh();
    }

    @FXML
    private void onRefreshClicked() {
        refresh();
    }

    // ---- Helpers ----
    private void refresh() {
        if (model == null) return;
        booksTableView.setItems(FXCollections.observableArrayList(model.getBooks()));
        booksTableView.refresh();
    }

    private String buildStateText(Book b) {
        if (b == null) return "";

        boolean borrowed = b.isBorrowed();
        boolean reserved = b.isReserved();

        String borrowerName = (b.getBorrower() == null) ? "" : b.getBorrower().getName();
        String reserverName = (b.getReserver() == null) ? "" : b.getReserver().getName();

        if (borrowed && reserved) return "Borrowed (" + borrowerName + ") + Reserved (" + reserverName + ")";
        if (borrowed) return "Borrowed (" + borrowerName + ")";
        if (reserved) return "Reserved (" + reserverName + ")";
        return "Available";
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}

package app;

import controller.BibloController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.LibraryInterface;
import model.LibraryModelImpl;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/view/Biblo.fxml"));

        Scene scene = new Scene(loader.load());

        // ⭐ HER kommer step 3 ⭐
        LibraryInterface model = new LibraryModelImpl();
        BibloController controller = loader.getController();
        controller.setModel(model);

        stage.setTitle("Library System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

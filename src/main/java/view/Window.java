package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Window extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(loadFXMLFile());
        Scene scene = new Scene(fxmlLoader.load(), 800, 500);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    public URL loadFXMLFile() {
        return getClass().getClassLoader()
                .getResource("window.fxml");
    }
}

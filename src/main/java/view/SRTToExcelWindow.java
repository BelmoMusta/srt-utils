package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SRTToExcelWindow extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		final FXMLLoader fxmlLoader = new FXMLLoader(loadFXMLFile());
		final Scene scene = new Scene(fxmlLoader.load(), 500, 400);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public URL loadFXMLFile() {
		return getClass().getClassLoader()
				.getResource("srt-to-excel.fxml");
	}
}

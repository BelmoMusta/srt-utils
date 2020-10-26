package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class SrtItemController implements Initializable {
	
	private TextArea text;
	private ObjectProperty<Font> observableFont = new SimpleObjectProperty<>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		observableFont.setValue(Font.font("Tahoma"));
		text.fontProperty().bindBidirectional(this.observableFont);
	}
}

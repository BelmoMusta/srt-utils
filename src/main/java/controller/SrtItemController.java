package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import model.SrtItem;

import java.net.URL;
import java.util.ResourceBundle;

public class SrtItemController implements Initializable {

    @FXML
    private Label order;
    @FXML
    private TextField startsAt;
    @FXML
    private TextField endsAt;
    @FXML
    private TextArea text;
    private ObjectProperty<Font> observableFont = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        observableFont.setValue(Font.font("Tahoma"));
        text.fontProperty().bindBidirectional(this.observableFont);
    }

    public void populateWith(SrtItem srtItem) {
        order.setText(String.valueOf(srtItem.getOrder()));
        startsAt.setText(srtItem.getStartsAt());
        endsAt.setText(srtItem.getEndsAt());
        text.setText(srtItem.getText());
    }

    public void lock() {
        startsAt.setEditable(false);
        endsAt.setEditable(false);
        text.setEditable(false);
    }

    public ObjectProperty<Font> getObservableFont() {
        return observableFont;
    }

}

package controller;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.SrtItem;
import org.apache.commons.io.FileUtils;
import view.SrtItemView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WindowController implements Initializable {
    @FXML
    private Button load;
    @FXML
    private Button changeFont;
    @FXML
    BorderPane container;

    @FXML
    ProgressIndicator progress;

    private ObjectProperty<Font> observableFont = new SimpleObjectProperty<>(Font.font("Tahoma"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initView();
        initChangeFontButton();
    }

    private void initChangeFontButton() {
        changeFont.setOnAction(event -> { // SPAGHETTI :D
            final Stage dialog = new Stage();
            dialog.setTitle("Select Font");
            final Button apply = new Button("Apply");
            final Button cancel = new Button("Cancel");
            final ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getSelectionModel().select(observableFont.get().getName());
            final ComboBox<Integer> fontSizeChooser = new ComboBox<>();
            fontSizeChooser.getSelectionModel().select(Integer.valueOf((int) observableFont.get().getSize()));
            final List<Integer> listOfSize = IntStream.range(0, 72).boxed().collect(Collectors.toList());
            fontSizeChooser.getItems().addAll(listOfSize);
            comboBox.getItems().addAll(Font.getFontNames());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initOwner(container.getScene().getWindow());

            HBox upperGroup = new HBox(20);
            upperGroup.setAlignment(Pos.CENTER);
            upperGroup.getChildren().add(comboBox);
            upperGroup.getChildren().add(fontSizeChooser);
            HBox dialogHbox = new HBox(20);
            dialogHbox.setAlignment(Pos.CENTER);

            VBox dialogVbox1 = new VBox(20);
            dialogVbox1.setAlignment(Pos.CENTER_LEFT);

            VBox dialogVbox2 = new VBox(20);
            dialogVbox2.setAlignment(Pos.CENTER_RIGHT);

            dialogVbox1.getChildren().add(apply);
            dialogVbox2.getChildren().add(cancel);

            apply.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    e -> {
                        Font font = Font.font(comboBox.getSelectionModel().getSelectedItem(), fontSizeChooser.getSelectionModel().getSelectedItem());
                        observableFont.setValue(font);
                        dialog.close();
                    });
            cancel.addEventHandler(MouseEvent.MOUSE_CLICKED,
                    e -> dialog.close());

            dialogHbox.getChildren().addAll(dialogVbox1, dialogVbox2);
            VBox container = new VBox(upperGroup, dialogHbox);
            Scene dialogScene = new Scene(container, 500, 100);
            dialog.setScene(dialogScene);
            dialog.show();
        });
    }

    private void initView() {
        progress.setVisible(false);
        VBox vBox = new VBox();
        VBox unmodifiableRightSide = new VBox();
        HBox hBox = new HBox(vBox, unmodifiableRightSide);
        container.setCenter(hBox);

        load.setOnAction(event -> {

            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {

                    progress.setVisible(true);
                    progress.setProgress(0.0);
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(null);
                    String fileRead = loadFile(file);


                    Set<SrtItem> srtItems = SrtItem.fromString(fileRead);
                    int indicator = 0;
                    double sizeOfElements = srtItems.size();
                    for (SrtItem srtItem : srtItems) {
                        SrtItemView srtItemView = new SrtItemView();
                        SrtItemView unmodifiable = new SrtItemView();
                        SrtItemController itemController = srtItemView.getController();
                        SrtItemController unmodifiableController = unmodifiable.getController();
                        WindowController.this.observableFont.bindBidirectional(itemController.getObservableFont());
                        WindowController.this.observableFont.bindBidirectional(unmodifiableController.getObservableFont());

                        itemController.populateWith(srtItem);
                        unmodifiableController.populateWith(srtItem);
                        unmodifiableController.lock();
                        vBox.getChildren().add(srtItemView);
                        unmodifiableRightSide.getChildren().add(unmodifiable);
                        progress.progressProperty().setValue(indicator / sizeOfElements);
                        //  progress.setProgress(indicator / sizeOfElements);
                        indicator++;

                    }
                    return null;
                }
            };
            new Thread(task)
                    .start();
        });
    }


    private String loadFile(File file) {
        String fileRead = "";
        if (file != null) {
            try {
                fileRead = FileUtils.readFileToString(file, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileRead;
    }
}

package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.SrtItem;
import org.apache.commons.io.FileUtils;
import view.SrtItemView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class WindowController implements Initializable {
    @FXML
    private Button load;
    @FXML
    BorderPane container;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VBox vBox = new VBox();
        VBox unmodifiableRightSide = new VBox();
        HBox hBox = new HBox(vBox, unmodifiableRightSide);
        container.setCenter(hBox);

        load.setOnAction(event -> {
            //testing
            // TODO make a file chooser
            String fileRead = "";
            try {
                fileRead = FileUtils.readFileToString(new File("C:\\Users\\mbelmokhtar\\Desktop\\King.Of.California.2007.1080p.BluRay.x264-Japhson.srt"), "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

            Set<SrtItem> srtItems = SrtItem.fromString(fileRead);
            int limit = 0;
            for (SrtItem srtItem : srtItems) {
                SrtItemView srtItemView = new SrtItemView();
                SrtItemView unmodifiable = new SrtItemView();
                SrtItemController itemController = srtItemView.getController();
                SrtItemController unmodifiableController = unmodifiable.getController();
                itemController.populateWith(srtItem);
                unmodifiableController.populateWith(srtItem);
                unmodifiableController.lock();
                vBox.getChildren().add(srtItemView);
                unmodifiableRightSide.getChildren().add(unmodifiable);
                if (limit++ > 50) { // takes a lot of time to load // progressbar?
                    break;
                }
            }


        });
    }
}

package view;

import bridge.BridgeInterface;
import controller.SrtItemController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;


public class SrtItemView extends AnchorPane implements BridgeInterface<SrtItemController> {
    private SrtItemController controller;
    public SrtItemView() {
        super();

        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getClassLoader()
                    .getResource("srt-item.fxml"));

            controller = new SrtItemController();
            loader.setController(controller);
            Node n = loader.load();
            this.getChildren().add(n);


        } catch (IOException iex) {

        }
    }

    @Override
    public SrtItemController getController() {
        return controller;
    }
}

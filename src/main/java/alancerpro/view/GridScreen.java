package alancerpro.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class GridScreen extends GridPane {
    private String resource;
    public FXMLLoader fxmlLoader;

    public GridScreen(String resource) {
        this.resource = resource;
        fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

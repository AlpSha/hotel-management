package alancerpro;

import alancerpro.controller.Controller;
import alancerpro.controller.LoginController;
import alancerpro.model.User;
import alancerpro.model.data.DBConnector;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        if(!DBConnector.getInstance().open()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Initialization failed");
            alert.setContentText("Can't receive connection from database");
            alert.showAndWait();
            Platform.exit();
            return;
        }


        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("view/LoginScreen.fxml"));
        Parent loginRoot = loginLoader.load();
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(loginRoot));
        stage.showAndWait();

        LoginController loginController = loginLoader.getController();
        if(loginController.isExitSignal()) {
            Platform.exit();
            return;
        }

        Controller.setLoggedInUser(loginController.getLoggedInUser());

        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("view/MainLook.fxml"));

        Parent mainRoot = mainLoader.load();
        primaryStage.setTitle("Hotel Management");
        primaryStage.setMinWidth(1050);
        primaryStage.setMinHeight(700);
        primaryStage.setScene(new Scene(mainRoot, 1350, 800));
        primaryStage.show();


    }

    public void stop() throws Exception {
        DBConnector.getInstance().close();
    }


    public static void main(String[] args) {
        launch(args);
    }

}

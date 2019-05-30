package alancerpro.controller;

import alancerpro.model.User;
import alancerpro.model.data.UserData;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML
    private JFXTextField usernameTextField;

    @FXML
    private JFXPasswordField passwordField;


    private User loggedInUser = null;
    private boolean exitSignal = true;

    @FXML
    void handleLoginButton() {
        if(usernameTextField.getText() == null || passwordField == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter your username and password");
            alert.showAndWait();
            passwordField.setText("");
            return;
        }

        User loggedInUser = UserData.getInstance().checkLogin(usernameTextField.getText(), passwordField.getText());
        if(loggedInUser == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wrong information");
            alert.setContentText("Your username or password is wrong");
            alert.showAndWait();
            passwordField.setText("");
            return;
        }
        this.loggedInUser = loggedInUser;
        this.exitSignal = false;
        this.usernameTextField.getScene().getWindow().hide();
    }

    @FXML
    void onKeyPressed(KeyEvent e) {
        if(e.getCode() == KeyCode.ENTER) {
            handleLoginButton();
        } else if(e.getCode() == KeyCode.ESCAPE) {
            handleExitButton();
        }
    }

    @FXML
    void handleExitButton() {
        this.exitSignal = true;
        this.usernameTextField.getScene().getWindow().hide();
    }

    public boolean isExitSignal() {
        return this.exitSignal;
    }

    public User getLoggedInUser() {
        return this.loggedInUser;
    }

}

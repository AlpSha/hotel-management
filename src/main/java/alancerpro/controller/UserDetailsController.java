package alancerpro.controller;

import alancerpro.model.User;
import alancerpro.model.UserType;
import alancerpro.model.data.UserData;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

import static alancerpro.model.data.UserData.users;

public class UserDetailsController implements Initializable {

    @FXML
    private JFXTextField idTextField;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXTextField usernameTextField;

    @FXML
    private JFXTextField emailTextBox;

    @FXML
    private JFXTextField addressTextBox;

    @FXML
    private JFXTextField phoneNumberTextBox;

    @FXML
    private JFXPasswordField passwordField;

    @FXML
    private ChoiceBox<UserType> userTypeChoiceBox;


    private User selectedUser;

    private boolean creatingNewUser = false;
    private boolean editable = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userTypeChoiceBox.setItems(FXCollections.observableArrayList(UserType.values()));
    }

    @FXML
    void handleCloseButton() {
        this.idTextField.getScene().getWindow().hide();
    }

    @FXML
    void handleSaveButton() {

        if(idTextField.getText().length()<6 || nameTextField.getText().length()<7 || usernameTextField.getText().length()<6 || emailTextBox.getText().length()<6 || passwordField.getText().length() < 7) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid input");
            alert.setContentText("Be sure you filled the important fields");
            alert.showAndWait();
            return;
        }



        User newUser = new User(idTextField.getText(), nameTextField.getText(), usernameTextField.getText(), userTypeChoiceBox.getValue(), emailTextBox.getText(), addressTextBox.getText(), phoneNumberTextBox.getText());


        if(!creatingNewUser) {
            // Editing current user
            boolean success;
            if(!passwordField.getText().equals("defaultPassword")) {
                success = UserData.getInstance().updateUserAndPassword(selectedUser.getUsername(), newUser, passwordField.getText());
            } else  {
                success = UserData.getInstance().updateUser(selectedUser.getUsername(), newUser);
            }

            if(!success) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error updating password");
                alert.setContentText("Your information couldn't have been upgraded.");
                alert.showAndWait();
                return;
            }
            selectedUser.setAll(newUser);
        } else {
            // Creating new user
            if(!UserData.getInstance().insertIntoUsers(newUser, passwordField.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Error creating user");
                alert.setContentText("We couldn't create user on database");
                alert.showAndWait();
                return;
            }
            this.creatingNewUser = false;
            users.add(newUser);
            handleCloseButton();
        }
    }


    public void setSelectedUser(User user) {
        this.selectedUser = user;

        boolean isAdmin = Controller.getLoggedInUser().getUserType() == UserType.admin;

        if(user == null) {
            creatingNewUser = true;
            editable = true;
            disableFields(false);
            userTypeChoiceBox.setValue(UserType.employee);
            if(isAdmin) {
                userTypeChoiceBox.setDisable(false);
            } else {
                userTypeChoiceBox.setDisable(true);
            }
            return;
        }


        idTextField.setText(user.getId());
        nameTextField.setText(user.getName());
        usernameTextField.setText(user.getUsername());
        emailTextBox.setText(user.getEmail());
        addressTextBox.setText(user.getAddress());
        phoneNumberTextBox.setText(user.getPhoneNumber());
        passwordField.setText("defaultPassword");
        userTypeChoiceBox.setValue(user.getUserType());
        creatingNewUser = false;
        if((isAdmin) ||  Controller.getLoggedInUser().getUsername().equals(user.getUsername())) {
            editable = true;
            disableFields(false);
            if(!isAdmin) {
                userTypeChoiceBox.setDisable(true);
            }
        } else {
            editable = false;
            disableFields(true);
        }
    }


    private void disableFields(boolean bool) {
        nameTextField.setDisable(bool);
        usernameTextField.setDisable(bool);
        userTypeChoiceBox.setDisable(bool);
        emailTextBox.setDisable(bool);
        addressTextBox.setDisable(bool);
        phoneNumberTextBox.setDisable(bool);
        passwordField.setDisable(bool);
    }

    public User getSelectedUser() {
        return selectedUser;
    }
}

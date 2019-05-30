package alancerpro.controller;

import alancerpro.model.User;
import alancerpro.model.data.UserData;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static alancerpro.model.data.UserData.users;

public class UsersController implements Initializable, Searchable {

    @FXML
    private JFXTreeTableView<User> usersTableView;

    @FXML
    private TreeTableColumn<User, String> columnName;

    @FXML
    private TreeTableColumn<User, String> columnUsername;

    @FXML
    private TreeTableColumn<User, String> columnUserType;

    @FXML
    private TreeTableColumn<User, String> columnEmail;

    @FXML
    private TreeTableColumn<User, String> columnPhoneNumber;

    @FXML
    private TreeTableColumn<User, String> columnAddress;

    @FXML
    private JFXTextField searchUserTextField;

    @FXML
    private Text usernameText;

    @FXML
    private Text userTypeText;


    private FilteredList<User> filteredList = new FilteredList<>(users);

    private final TreeItem<User> root = new RecursiveTreeItem<>(filteredList, RecursiveTreeObject::getChildren);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.usernameText.setText(Controller.getLoggedInUser().getUsername());
        this.userTypeText.setText(Controller.getLoggedInUser().getUserType().toString());

        filteredList.setPredicate(item -> true);

        columnName.setCellValueFactory(cell -> cell.getValue().getValue().nameProperty());
        columnUsername.setCellValueFactory(cell -> cell.getValue().getValue().usernameProperty());
        columnUserType.setCellValueFactory(cell -> cell.getValue().getValue().userTypeProperty());
        columnEmail.setCellValueFactory(cell -> cell.getValue().getValue().emailProperty());
        columnAddress.setCellValueFactory(cell -> cell.getValue().getValue().addressProperty());
        columnPhoneNumber.setCellValueFactory(cell -> cell.getValue().getValue().phoneNumberProperty());

        usersTableView.setRowFactory(tv -> {
            TreeTableRow<User> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    handleShowButton();
                }
            });
            return row;
        });


        usersTableView.setRoot(root);
        usersTableView.setShowRoot(false);
    }

    @FXML
    private void handleAddButton() {
        showUserDetailsStage(null);
        usersTableView.getSelectionModel().selectLast();
    }

    @FXML
    private void handleShowButton() {
        showUserDetailsStage(usersTableView.getSelectionModel().getSelectedItem().getValue());
    }

    private void showUserDetailsStage(User user) {
        FXMLLoader detailsLoader = new FXMLLoader(getClass().getResource("/alancerpro/view/UserDetailScreen.fxml"));
        Parent root;
        try {
            root = detailsLoader.load();
        } catch (IOException e) {
            System.out.println("Error loading user details: " + e.getMessage());
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Can't load screen");
            alert.setContentText("There has been a problem with loading user details screen");
            alert.showAndWait();
            return;
        }
        ((UserDetailsController) detailsLoader.getController()).setSelectedUser(user);
        Stage stage = new Stage();
        if (user == null) {
            stage.setTitle("New User");
        } else {
            stage.setTitle("User details");
        }
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setScene(new Scene(root, 522, 667));
        stage.showAndWait();
    }


    @FXML
    private void handleDeleteButton() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("Are you sure?");
        confirmation.setContentText("Selected user will be deleted");
        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isEmpty() || result.get() == ButtonType.CANCEL) {
            return;
        }

        TreeItem<User> selectedItem = usersTableView.getSelectionModel().getSelectedItem();

        if (!UserData.getInstance().deleteFromUsers(selectedItem.getValue().getUsername())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Error deleting user");
            alert.setContentText("Couldn't delete user from database.");
            alert.showAndWait();
            return;
        }

        users.remove(selectedItem.getValue());
    }


    @FXML
    public void handleSearchKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSearchButton();
        }
    }

    @FXML
    public void handleSearchButton() {
        String searchParameter = searchUserTextField.getText();
        if (searchParameter.isBlank()) {
            filteredList.setPredicate(reservation -> true);
            return;
        }

        if (usersTableView.getSortOrder().size() <= 0) {
            searchByName(searchParameter);
            return;
        }

        switch (usersTableView.getSortOrder().get(0).getText()) {
            case "Name":
                searchByName(searchParameter);
                break;
            case "Username":
                searchByUsername(searchParameter);
                break;
            case "User Type":
                searchByUserType(searchParameter);
                break;
            case "Email":
                searchByEmail(searchParameter);
                break;
            case "Phone Number":
                searchByPhoneNumber(searchParameter);
                break;
            case "Address":
                searchByAddress(searchParameter);
                break;
        }
    }

    private void searchByName(String param) {
        filteredList.setPredicate(item -> item.getName().toLowerCase().contains(param.toLowerCase()));
    }

    private void searchByUsername(String param) {
        filteredList.setPredicate(item -> item.getUsername().toLowerCase().contains(param.toLowerCase()));
    }

    private void searchByUserType(String param) {
        filteredList.setPredicate(item -> item.getUserType().toString().toLowerCase().contains(param.toLowerCase()));
    }

    private void searchByEmail(String param) {
        filteredList.setPredicate(item -> item.getEmail().toLowerCase().contains(param.toLowerCase()));
    }

    private void searchByPhoneNumber(String param) {
        filteredList.setPredicate(item -> item.getPhoneNumber().toLowerCase().contains(param.toLowerCase()));
    }

    private void searchByAddress(String param) {
        filteredList.setPredicate(item -> item.getAddress().toLowerCase().contains(param.toLowerCase()));
    }
}

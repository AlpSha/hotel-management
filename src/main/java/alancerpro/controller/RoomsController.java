package alancerpro.controller;

import alancerpro.model.Reservation;
import alancerpro.model.ReservationStatus;
import alancerpro.model.Room;
import alancerpro.model.data.ReservationData;
import alancerpro.model.data.RoomData;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RoomsController implements Initializable, Searchable {
    @FXML
    TreeTableView<Room> roomTreeTableView;
    @FXML
    TreeTableColumn<Room, String> columnRoomNumber;
    @FXML
    TreeTableColumn<Room, String> columnRoomType;
    @FXML
    TreeTableColumn<Room, String> columnRoomPrice;
    @FXML
    TreeTableColumn<Room, String> columnRoomStatus;
    @FXML
    TextField searchRoomTextField;
    @FXML
    Text usernameText;
    @FXML
    Text userTypeText;

    private FilteredList<Room> filteredList = new FilteredList<>(RoomData.rooms);

    private final TreeItem<Room> root = new RecursiveTreeItem<>(filteredList, RecursiveTreeObject::getChildren);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.usernameText.setText(Controller.getLoggedInUser().getUsername());
        this.userTypeText.setText(Controller.getLoggedInUser().getUserType().toString());

        filteredList.setPredicate(room -> true);

        columnRoomNumber.setCellValueFactory(cell -> cell.getValue().getValue().roomNumberProperty());
        columnRoomType.setCellValueFactory(cell -> cell.getValue().getValue().roomTypeProperty());

        columnRoomPrice.setCellValueFactory(cell -> cell.getValue().getValue().roomPriceProperty());
        columnRoomStatus.setCellValueFactory(cell -> {
            SimpleStringProperty currentStatus = new SimpleStringProperty();
            Room room = cell.getValue().getValue();
            Reservation res = room.getReservationOnDate(LocalDate.now());
            if(res != null) {
                if(res.getReservationStatus().equals(ReservationStatus.checkedIn)) {
                    currentStatus.set("staying");
                } else if(res.getReservationStatus().equals(ReservationStatus.active)) {
                    currentStatus.set("reserved");
                }
            } else {
                currentStatus.set("empty");
            }
            return currentStatus;
        });

        roomTreeTableView.setRoot(root);
        roomTreeTableView.setShowRoot(false);
    }


    private boolean edit = false;

    @FXML
    void handleEditButton() {
        edit=true;
        handleAddButton();
    }

    @FXML
    void handleAddButton() {
        TreeItem<Room> selectedItem = roomTreeTableView.getSelectionModel().getSelectedItem();
        if(edit && selectedItem == null) {
            edit = false;
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/alancerpro/view/AddRoomDialogue.fxml"));
        try {
            Stage stage = new Stage();
            stage.setTitle("Room Information");
            stage.initModality(Modality.APPLICATION_MODAL);
            Parent root = loader.load();
            stage.setScene(new Scene(root, 540, 400));
            stage.setResizable(false);
            if(edit && (selectedItem != null)) {
                ((RoomDialogueController)loader.getController()).setEditingRoom(selectedItem.getValue());
            }
            edit = false;
            stage.showAndWait();
        } catch (IOException e){
            System.out.println("Error loading room dialogue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleDeleteButton() {
        TreeItem<Room> selectedItem;
        if((selectedItem = roomTreeTableView.getSelectionModel().getSelectedItem()) == null) {
            return;
        }

        if(columnRoomStatus.getCellData(selectedItem).equals("staying")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Room is not empty");
            alert.setContentText("There is someone staying at that room!");
            alert.showAndWait();
            return;
        }

        if(ReservationData.getInstance().queryReservationsIdsWithRoomNumber(selectedItem.getValue().getRoomNumber()).size() > 0) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setHeaderText("Are you sure?");
            confirmation.setContentText("There is reservations with this room. All will be deleted.");
            Optional<ButtonType> result = confirmation.showAndWait();
            if(result.isEmpty() || result.get() == ButtonType.CANCEL) {
                return;
            }
        }


        if(!RoomData.getInstance().deleteRoomWithReservations(selectedItem.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error deleting room");
            alert.setContentText("We couldn't delete room from database");
            alert.showAndWait();
            return;
        }

        RoomData.rooms.remove(selectedItem.getValue());
        for (List<Reservation> list: ReservationData.reservationsMap.values()) {
            List<Reservation> toDelete = new ArrayList<>();
            for (Reservation r: list) {
                if(r.getRoom().getRoomNumber() == selectedItem.getValue().getRoomNumber()) {
                    toDelete.add(r);
                }
            }
            list.removeAll(toDelete);
        }

    }


    @FXML
    public void handleSearchKey(KeyEvent k) {
        if(k.getCode() == KeyCode.ENTER) {
            handleSearchButton();
        }
    }


    @FXML
    public void handleSearchButton() {
        String searchParameter = searchRoomTextField.getText();
        if (searchParameter.isBlank()) {
            filteredList.setPredicate(room -> true);
            return;
        }

        if (roomTreeTableView.getSortOrder().size() <= 0) {
            searchByRoomNumber(searchParameter);
            return;
        }

        switch(roomTreeTableView.getSortOrder().get(0).getText()) {
            case "Room Number":
                searchByRoomNumber(searchParameter);
                break;
            case "Room Type":
                searchByRoomType(searchParameter);
                break;
            case "Price":
                searchByRoomPrice(searchParameter);
                break;
            case "Status":
                searchByRoomStatus(searchParameter);
                break;
        }
    }

    private void searchByRoomNumber(String searchParameter) {
        filteredList.setPredicate(room -> String.valueOf(room.getRoomNumber()).contains(searchParameter));
    }

    private void searchByRoomType(String searchParameter) {
        filteredList.setPredicate(room -> room.getRoomType().toString().toLowerCase().contains(searchParameter.toLowerCase()));
    }

    private void searchByRoomPrice(String searchParameter) {
        filteredList.setPredicate(room -> String.valueOf(room.getRoomPrice()).contains(searchParameter));
    }

    private void searchByRoomStatus(String searchParameter) {

        filteredList.setPredicate(room -> true);
        filteredList.setPredicate(room -> {
            TreeItem<Room> roomTreeItem = null;
            for (TreeItem<Room> item:root.getChildren()) {
                if(item.getValue().equals(room)) {
                    roomTreeItem = item;
                    break;
                }
            }
            boolean returnValue = false;
            if(roomTreeItem != null) {
                returnValue = columnRoomStatus.getCellData(roomTreeItem).toLowerCase().contains(searchParameter.toLowerCase());
            }
            return returnValue;
        });
    }

    void removeSearchFilter() {
        filteredList.setPredicate(room -> true);
        this.searchRoomTextField.setText("");
    }





}

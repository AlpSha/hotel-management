package alancerpro.controller;

import alancerpro.model.Room;
import alancerpro.model.RoomType;
import alancerpro.model.data.RoomData;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomDialogueController implements Initializable {
    @FXML
    JFXTextField roomNumberField;
    @FXML
    ChoiceBox<RoomType> roomTypeChoiceBox;


    private Room editingRoom;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roomTypeChoiceBox.setItems(FXCollections.observableArrayList(RoomType.values()));

    }

    public void setEditingRoom(Room room) {
        this.roomNumberField.setDisable(true);
        editingRoom = room;
        roomNumberField.setText(String.valueOf(room.getRoomNumber()));
        roomTypeChoiceBox.setValue(room.getRoomType());
    }

    @FXML
    void handleSaveButton() {
        boolean result;
        if(editingRoom == null) {
            result = addRoom();
        } else {
            result = editRoom();
        }
        if(result) {
            handleCancelButton();
        }

    }

    @FXML
    void handleCancelButton() {
        roomNumberField.getScene().getWindow().hide();
    }

    @FXML
    void handleKeyPressed(KeyEvent k) {
        if(k.getCode() == KeyCode.ESCAPE) {
            handleCancelButton();
        }
    }


    private boolean addRoom() {
        Room room;
        try {
            room = new Room(Integer.parseInt(roomNumberField.getText()), roomTypeChoiceBox.getValue());
        } catch (NumberFormatException|NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wrong type of input");
            alert.setContentText("Please be sure you entered correct formation.");
            alert.showAndWait();
            return false;
        }

        if(roomIsExist(room)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Room already exists");
            alert.setContentText("There is a room with this number");
            alert.showAndWait();
            return false;
        }

        if(!RoomData.getInstance().insertIntoRooms(room)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error with database");
            alert.setContentText("Couldn't insert room to database.");
            alert.showAndWait();
            return false;
        }
        RoomData.rooms.add(room);
        return true;
    }

    private boolean editRoom() {
        if(!RoomData.getInstance().updateRoom(editingRoom, roomTypeChoiceBox.getValue())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error with database");
            alert.setContentText("Couldn't update room information");
            alert.showAndWait();
            return false;
        }
        this.editingRoom.setRoomType(roomTypeChoiceBox.getValue());
        return true;
    }

    private boolean roomIsExist(Room room) {
        for(Room r: RoomData.rooms) {
            if(r.getRoomNumber() == room.getRoomNumber()) {
                return true;
            }
        }
        return false;
    }
}

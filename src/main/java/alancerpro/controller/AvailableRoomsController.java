package alancerpro.controller;

import alancerpro.model.Room;
import alancerpro.model.data.RoomData;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;

import java.time.LocalDate;


public class AvailableRoomsController {

    @FXML
    JFXTreeTableView<Room> availableRoomsTableView;
    @FXML
    TreeTableColumn<Room, String> columnRoom;
    @FXML
    TreeTableColumn<Room, String> columnType;

    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationDetailController sourceController;


    private ObservableList<Room> availableRooms = FXCollections.observableArrayList();

    private final TreeItem<Room> root = new RecursiveTreeItem<>(availableRooms, RecursiveTreeObject::getChildren);




    public void setDates(LocalDate startDate, LocalDate endDate, ReservationDetailController sourceController){

        this.startDate = startDate;
        this.endDate = endDate;
        this.sourceController = sourceController;
        for (Room r : RoomData.rooms) {
            if(r.isAvailableBetween(startDate, endDate)) {
                availableRooms.add(r);
            }
        }
        columnRoom.setCellValueFactory(param -> param.getValue().getValue().roomNumberProperty());
        columnType.setCellValueFactory(param -> param.getValue().getValue().roomTypeProperty());

        availableRoomsTableView.setRowFactory(tv -> {
            TreeTableRow<Room> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if(event.getClickCount() == 2 && (!row.isEmpty())) {
                    handleAddButton();
                }
            });
            return row;
        });

        availableRoomsTableView.setRoot(root);
        availableRoomsTableView.setShowRoot(false);
    }







    @FXML
    public void handleAddButton() {
        sourceController.addReservationRow(startDate, endDate, availableRoomsTableView.getSelectionModel().getSelectedItem().getValue());
        handleCancelButton();
    }

    @FXML
    public void handleCancelButton() {
        availableRoomsTableView.getScene().getWindow().hide();
    }
}

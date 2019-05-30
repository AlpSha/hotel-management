package alancerpro.controller;

import alancerpro.model.Reservation;
import alancerpro.model.Room;
import alancerpro.model.data.RoomData;
import alancerpro.util.StringFormatter;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    @FXML
    Text textMonthAndYear;
    @FXML
    TreeTableColumn<Room, String> columnRoomNumber;
    @FXML
    TreeTableView<Room> roomsTableView;
    @FXML
    Text usernameText;
    @FXML
    Text userTypeText;


    private final TreeItem<Room> root = new RecursiveTreeItem<>(RoomData.rooms, RecursiveTreeObject::getChildren);

    private Month currentMonth;
    private Year currentYear;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.usernameText.setText(Controller.getLoggedInUser().getUsername());
        this.userTypeText.setText(Controller.getLoggedInUser().getUserType().toString());

        currentMonth = LocalDate.now().getMonth();
        currentYear = Year.of(LocalDate.now().getYear());
        setColumns();
    }

    @FXML
    void handleButtonNextMonth() {
        if(currentMonth == Month.DECEMBER) {
           currentYear = currentYear.plusYears(1);
        }
        currentMonth = currentMonth.plus(1);
        setColumns();
    }
    @FXML
    void handleButtonPreviousMonth() {
        if(currentMonth == Month.JANUARY) {
            currentYear = currentYear.minusYears(1);
        }
        currentMonth = currentMonth.minus(1);
        setColumns();
    }
    @FXML
    void handleButtonToday() {
        currentMonth = LocalDate.now().getMonth();
        currentYear = Year.of(LocalDate.now().getYear());
        setColumns();
    }








    /********* Setting columns *********/
    public void setColumns() {
        roomsTableView.getColumns().setAll(FXCollections.observableArrayList());
        roomsTableView.getColumns().add(columnRoomNumber);
        for(int day = 1; day<=currentMonth.length(currentYear.isLeap()); ++day) {
            TreeTableColumn<Room, String> tmpColumn = new TreeTableColumn<>();
            tmpColumn.setText(String.valueOf(day));
            tmpColumn.setPrefWidth(38);

            tmpColumn.setCellValueFactory(cell -> {
                LocalDate date = LocalDate.of(currentYear.getValue(), currentMonth, Integer.parseInt(tmpColumn.getText()));

                Reservation res = cell.getValue().getValue().getReservationOnDate(date);
                SimpleStringProperty cellValue = new SimpleStringProperty();
                if(res == null) {
                    cellValue.set("empty");
                } else if(res.getStartDateAsDate().equals(date) || tmpColumn.getText().equals("1")) {
                    cellValue.set(res.getCustomer().getName());
                    tmpColumn.setPrefWidth(70);
                } else {
                    cellValue.set("busy");
                }
                return cellValue;
            });

            tmpColumn.setCellFactory(column -> new TreeTableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if(item == null || empty) {
                        setStyle("");
                    } else {
                        if(item.equals("empty")) {
                            setStyle("");
                            setText("");
                        } else if(item.equals("busy")) {
                            setStyle("-fx-background-color:  #00B4FF");
                            setText("");
                        } else {
                            setStyle("-fx-background-color:  #00B4FF");
                            setText(item);
                        }
                        setWrapText(true);
                    }
                }
            });

            roomsTableView.getColumns().add(tmpColumn);
        }

        columnRoomNumber.setCellValueFactory(cell -> cell.getValue().getValue().roomNumberProperty());

        roomsTableView.setRoot(root);
        roomsTableView.setShowRoot(false);

        String monthName = StringFormatter.capitalizedThreeLetters(this.currentMonth.toString());


        textMonthAndYear.setText(monthName + " " + currentYear.toString());

    }








}

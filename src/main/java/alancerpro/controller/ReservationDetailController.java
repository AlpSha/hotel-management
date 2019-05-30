package alancerpro.controller;

import alancerpro.model.*;
import alancerpro.model.data.AccountData;
import alancerpro.model.data.ReservationData;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class ReservationDetailController implements Initializable {

    @FXML
    Text customerNameText;
    @FXML
    Text reservationIdText;
    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;
    @FXML
    Text totalNights;
    @FXML
    Text status;
    @FXML
    JFXButton buttonCustomer;
    @FXML
    JFXButton buttonCheckIn;
    @FXML
    JFXButton buttonCheckOut;
    @FXML
    JFXButton buttonAdd;
    @FXML
    JFXButton buttonSaveSide;
    @FXML
    JFXButton buttonSaveBottom;
    @FXML
    JFXButton buttonDelete;
    @FXML
    JFXButton buttonDeposit;

    @FXML
    Text balanceText;
    @FXML
    Label balanceLabel;
    @FXML
    TreeTableColumn<Reservation, String> columnRoom;
    @FXML
    TreeTableColumn<Reservation, String> columnAdults;
    @FXML
    TreeTableColumn<Reservation, String> columnChilds;
    @FXML
    TreeTableColumn<Reservation, String> columnStartDate;
    @FXML
    TreeTableColumn<Reservation, String> columnEndDate;
    @FXML
    TreeTableColumn<Reservation, String> columnNumberOfDays;
    @FXML
    TreeTableView<Reservation> reservationsTableView;


    private ObservableList<Reservation> reservationsList = FXCollections.observableArrayList();

    private final TreeItem<Reservation> root = new RecursiveTreeItem<>(reservationsList, RecursiveTreeObject::getChildren);


    private Customer customer;
    private boolean newReservation = false;


    /********** Initialization **********/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        StringConverter<LocalDate> converter = new StringConverter<>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(TimeFormat.format);
            @Override
            public String toString(LocalDate date) {
                if(date!=null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String s) {
                if(s != null && !s.isEmpty()) {
                    return LocalDate.parse(s, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        startDate.setConverter(converter);
        startDate.setPromptText(TimeFormat.format);
        endDate.setConverter(converter);
        endDate.setPromptText(TimeFormat.format);

        columnRoom.setCellValueFactory(param -> param.getValue().getValue().getRoom().roomNumberProperty());
        columnAdults.setCellValueFactory(param -> param.getValue().getValue().adultsProperty());
        columnAdults.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

        columnChilds.setCellValueFactory(param -> param.getValue().getValue().childsProperty());
        columnChilds.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

        columnStartDate.setCellValueFactory(param -> param.getValue().getValue().startDateProperty());
        columnStartDate.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        columnEndDate.setCellValueFactory(param -> param.getValue().getValue().endDateProperty());
        columnEndDate.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

        columnNumberOfDays.setCellValueFactory(param -> param.getValue().getValue().numberOfDaysProperty());
        startDate.valueProperty().addListener((observable, oldValue, newValue) ->  {
            if(newValue == null || endDate.getValue() == null) {
                totalNights.setText(String.valueOf(-1));
                return;
            }
            try {
                totalNights.setText(String.valueOf(newValue.datesUntil(endDate.getValue()).count()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date range: " + e.getMessage());
            }

        });
        endDate.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue == null || startDate.getValue() == null) {
                totalNights.setText(String.valueOf(-1));
                return;
            }
            try {
                totalNights.setText(String.valueOf(startDate.getValue().datesUntil(newValue).count()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date range: " + e.getMessage());
            }
        }));

        //Set editing cells, commit edit if chosen date is available
        columnStartDate.setOnEditCommit(cell -> {
                    try {
                        if(LocalDate.parse(cell.getNewValue(), TimeFormat.getFormatter()).compareTo(cell.getRowValue().getValue().getEndDateAsDate())  >= 0) {
                            reservationsTableView.refresh();
                            return;
                        }
                    } catch (DateTimeParseException e) {
                        reservationsTableView.refresh();
                        return;
                    }
                    Reservation res = reservationsTableView.getSelectionModel().getSelectedItem().getValue();
                    removeThisReservationAndAddOthersToMainList(res);
                    if(res.getRoom().isAvailableBetween(LocalDate.parse(cell.getNewValue(), TimeFormat.getFormatter()), res.getEndDateAsDate())) {
                        res.setStartDate(cell.getNewValue());
                    }
                    res.setStartDate(cell.getOldValue());
                    ReservationData.reservationsAsList.add(res);
                    reservationsTableView.refresh();
                }
        );
        columnEndDate.setOnEditCommit(cell -> {
            try {
                if(LocalDate.parse(cell.getNewValue(), TimeFormat.getFormatter()).compareTo(cell.getRowValue().getValue().getStartDateAsDate())  <= 0) {
                    reservationsTableView.refresh();
                    return;
                }
            } catch (DateTimeParseException e) {
                reservationsTableView.refresh();
                return;
            }
            Reservation res = reservationsTableView.getSelectionModel().getSelectedItem().getValue();
            removeThisReservationAndAddOthersToMainList(res);
            if(res.getRoom().isAvailableBetween(res.getStartDateAsDate(), LocalDate.parse(cell.getNewValue(), TimeFormat.getFormatter()))) {
                res.setEndDate(cell.getNewValue());
            }
            res.setEndDate(cell.getOldValue());

            //Adding back to list because we finished checking
            ReservationData.reservationsAsList.add(res);
            reservationsTableView.refresh();
        });
        reservationsTableView.setEditable(true);
        reservationsTableView.setRoot(root);
        reservationsTableView.setShowRoot(false);

    }


    public void createNewReservation() {
        newReservation = true;
        setSelectedReservation(null);
        long resId;
        if((resId = ReservationData.getInstance().generateNewReservationId()) == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error generating reservation id");
            alert.setContentText("Turning back to reservations menu");
            alert.showAndWait();
            Controller.getInstance().showReservationsScreen();
        }
        this.reservationIdText.setText(String.valueOf(resId));
        this.status.setText(ReservationStatus.active.toString());
    }


    public void setSelectedReservation(List<Reservation> reservations) {
        if(reservations == null) {
            buttonCheckIn.setVisible(false);
            buttonCheckOut.setVisible(false);
            buttonDeposit.setVisible(false);
            return;
        }

        Reservation reservation = reservations.get(0);
        this.customer = reservation.getCustomer();
        if(customer != null) {
            this.customerNameText.setText(customer.getName());
        }
        this.reservationIdText.setText(String.valueOf(reservation.getReservationID()));
        this.startDate.setValue(reservation.getStartDateAsDate());
        this.endDate.setValue(reservation.getEndDateAsDate());
        this.totalNights.setText(String.valueOf(reservation.getNumberOfDays()));
        this.status.setText(reservation.getReservationStatus().toString());

        switch (reservation.getReservationStatus()) {
            case active:
                buttonCheckIn.setVisible(true);
                buttonCheckOut.setVisible(false);
                buttonDeposit.setVisible(false);
                break;
            case checkedIn:
                buttonCheckIn.setVisible(false);
                buttonCheckOut.setVisible(true);
                buttonDeposit.setVisible(true);
                balanceLabel.setVisible(true);
                balanceText.setVisible(true);
                refreshBalanceText();
                break;
            case checkedOut:
                buttonCheckIn.setVisible(false);
                buttonCheckOut.setVisible(false);
                buttonDeposit.setVisible(false);
        }

        if(reservation.getReservationStatus() == ReservationStatus.checkedOut) {
            buttonAdd.setVisible(false);
            buttonCheckOut.setVisible(false);
            buttonCheckIn.setVisible(false);
            buttonCustomer.setVisible(false);
            buttonDelete.setVisible(false);
            buttonSaveBottom.setVisible(false);
            buttonSaveSide.setVisible(false);
        }

        reservationsList.setAll(reservations);
    }






    /******** Handle Methods ********/
    @FXML
    public void handleAddButton() {
        if(endDate.getValue() == null || startDate.getValue() == null || startDate.getValue().compareTo(endDate.getValue()) >= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid date pickup");
            alert.setContentText("Be sure you selected it correctly");
            alert.showAndWait();
            return;
        }

        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/alancerpro/view/AvailableRoomsDialogue.fxml"));
            root = loader.load();

            //Adding this list because i want these rooms to be not available to choose
            ReservationData.reservationsAsList.addAll(reservationsList);

            ((AvailableRoomsController)loader.getController()).setDates(this.startDate.getValue(), this.endDate.getValue(), this);

            Stage stage = new Stage();
            stage.setTitle("Available Rooms");
            stage.setScene(new Scene(root, 550, 700));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error opening room dialogue: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteButton() {
        Reservation itemToDelete = reservationsTableView.getSelectionModel().getSelectedItem().getValue();
        reservationsList.remove(itemToDelete);
        ReservationData.reservationsAsList.remove(itemToDelete);
    }

    @FXML
    public void handleCheckInButton() {
        for (Reservation r: reservationsList) {
            r.setReservationStatus(ReservationStatus.checkedIn);
        }
        status.setText(ReservationStatus.checkedIn.toString());
        setSelectedReservation(new ArrayList<>(reservationsList));
        handleSaveButton();
    }

    @FXML
    public void handleDepositButton() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/alancerpro/view/DepositScreen.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Hotel Management");
            stage.setScene(new Scene(root, 610, 428));
            stage.setResizable(false);

            ((DepositScreenController)loader.getController()).setCurrentAccount(AccountData.accounts.get(Long.parseLong(this.reservationIdText.getText())));

            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error loading deposit screen: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        refreshBalanceText();
    }


    @FXML
    public void handleSaveButton(){
        if(customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No customer selected");
            alert.setContentText("You should select a customer.");
            alert.showAndWait();
            return;
        }

        if(reservationsList.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No reservations");
            alert.setContentText("There should be at least one reservation.");
            alert.showAndWait();
            return;
        }

        long reservationID = Long.parseLong(reservationIdText.getText());


        if (!ReservationData.getInstance().insertReservationList(reservationsList, reservationID)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error Saving Data");
            alert.setContentText("Reservations couldn't be saved. Be sure that everything is valid.");
            alert.showAndWait();
            return;
        }

        ObservableList<Reservation> newList = FXCollections.observableArrayList();
        for(Reservation r: reservationsList) {
            newList.add(new Reservation(r));
        }



        ReservationData.reservationsMap.put(reservationID, newList);
        ReservationData.setLists();

        double newDebt = 0;
        for (Reservation r: ReservationData.reservationsMap.get(reservationID)) {
            newDebt += r.getRoom().getRoomType().getRoomPrice();
        }

        if(newReservation) {
            Account account = new Account(reservationID, 0);
            AccountData.accounts.put(reservationID, account);
            AccountData.getInstance().insertIntoAccounts(account);
        }

        Account account = AccountData.accounts.get(reservationID);
        double oldDebt = account.getDebt();
        account.setDebt(newDebt);
        if (!AccountData.getInstance().updateAccountDebt(account)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Save Failed");
            alert.setContentText("We failed to update account information. Please try again some other time.");
            alert.showAndWait();
            account.setDebt(oldDebt);
            return;
        }
        balanceText.setText(String.format("%.2f", account.getBalance()));
        if(newReservation) {
            buttonCheckIn.setVisible(true);
            newReservation = false;
        }
    }


    @FXML
    void handleCheckOutButton() {
        if(Double.valueOf(this.balanceText.getText()) > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Customer needs to make payment");
            alert.setContentText("Be sure you saved the last changes");
            alert.setHeight(200);
            alert.showAndWait();
            return;
        }

        long currentReservationId = Long.parseLong(this.reservationIdText.getText());

        if(!ReservationData.getInstance().updateStatusOfReservation(currentReservationId, ReservationStatus.checkedOut)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Couldn't check out.");
            alert.setContentText("We have a problem with database. Can't save the changes.");
            alert.showAndWait();
            return;
        }

        for (Reservation r: ReservationData.reservationsMap.get(currentReservationId)) {
            r.setReservationStatus(ReservationStatus.checkedOut);
        }
        ReservationData.setLists();

        setSelectedReservation(ReservationData.reservationsMap.get(currentReservationId));
    }



    @FXML
    public void handleReservationsButton() {
        Controller.getInstance().showReservationsScreen();
    }


    @FXML
    void handleCustomerSelection() {
        GridPane currentScreen = (GridPane) (Controller.getCurrentMainPane().getCenter());

        CustomersController customersController = Controller.getInstance().showCustomersScreenGetController();
        customersController.setAsSelectForReservation(currentScreen, this);
        customersController.setSelection(customer);
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        for (Reservation r: reservationsList) {
            r.setCustomer(customer);
        }
        this.customerNameText.setText(customer.getName());
    }



    /********* Other methods **********/
    public void addReservationRow(LocalDate startDate, LocalDate endDate, Room room) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeFormat.format);

        Reservation reservation = new Reservation(
                Long.parseLong(reservationIdText.getText()),
                room,
                customer,
                0,
                0,
                startDate.format(formatter),
                endDate.format(formatter),
                ReservationStatus.valueOf(status.getText()),
                LocalDate.now().format(formatter),
                Controller.getLoggedInUser().getUsername());
        reservationsList.add(reservation);

        //Removing list here because we added it when handling with the add button
        ReservationData.reservationsAsList.removeAll(reservationsList);
    }




//    This method removes the given reservation from reservationsAsList which is being observed by table view.
//    This is removing all the reservations with same Id first, than adds the clone items instead except the one being edited currently.
//    So we will be able to check the reservation dates amongst all reservations including the ones in table, except the one we are editing.
    private void removeThisReservationAndAddOthersToMainList(Reservation res) {
        List<Reservation> toRemove = new ArrayList<>();
        for (Reservation r: ReservationData.reservationsAsList) {
            if(r.getReservationID() == Long.parseLong(reservationIdText.getText())) {
                toRemove.add(r);
            }
        }
        ReservationData.reservationsAsList.removeAll(toRemove);
        ReservationData.reservationsAsList.addAll(reservationsList);
        ReservationData.reservationsAsList.remove(res);
    }


    private void refreshBalanceText() {
        balanceText.setText(String.format("%.2f", AccountData.accounts.get(Long.parseLong(this.reservationIdText.getText())).getBalance()));
    }
}

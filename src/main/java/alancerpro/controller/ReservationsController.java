package alancerpro.controller;

import alancerpro.model.Reservation;
import alancerpro.model.data.AccountData;
import alancerpro.model.data.ReservationData;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class ReservationsController implements Initializable, Searchable {
    @FXML
    TreeTableColumn<Reservation, String> columnReservationID;
    @FXML
    TreeTableColumn<Reservation, String> columnStatus;
    @FXML
    TreeTableColumn<Reservation, String> columnCustomer;
    @FXML
    TreeTableColumn<Reservation, String> columnStartDate;
    @FXML
    TreeTableColumn<Reservation, String> columnEndDate;
    @FXML
    TreeTableColumn<Reservation, String> columnTotalDays;
    @FXML
    JFXTreeTableView<Reservation> reservationsTableView;
    @FXML
    JFXTextField searchReservationTextField;
    @FXML
    JFXButton searchReservationButton;
    @FXML
    Text usernameText;
    @FXML
    Text userTypeText;



    protected FilteredList<Reservation> filteredList = new FilteredList<>(ReservationData.reservationsAsDistinctList, reservation -> true);

    private final TreeItem<Reservation> root = new RecursiveTreeItem<>(filteredList, RecursiveTreeObject::getChildren);






    /************ Initialize ************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.usernameText.setText(Controller.getLoggedInUser().getUsername());
        this.userTypeText.setText(Controller.getLoggedInUser().getUserType().toString());


        columnReservationID.setCellValueFactory(param -> param.getValue().getValue().reservationIDProperty());
        columnCustomer.setCellValueFactory(param -> param.getValue().getValue().getCustomer().nameProperty());
        columnStartDate.setCellValueFactory(param -> param.getValue().getValue().startDateProperty());
        columnEndDate.setCellValueFactory(param -> param.getValue().getValue().endDateProperty());
        columnTotalDays.setCellValueFactory(param -> param.getValue().getValue().numberOfDaysProperty());



        //This part has been taken from James_D on stackoverflow.com
        reservationsTableView.setRowFactory( tv -> {
            TreeTableRow<Reservation> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    handleShowButton();
                }
            });
            return row ;
        });



        reservationsTableView.setRoot(root);
        reservationsTableView.setShowRoot(false);
    }




    @FXML
    public void handleShowButton() {
        if(reservationsTableView.getSelectionModel().getSelectedItem() == null) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/alancerpro/view/ReservationDetailScreen.fxml"));
        try {

            Parent root = loader.load();
            Controller.getCurrentMainPane().setCenter(root);
        } catch(IOException e) {
            e.printStackTrace();
        }

        List<Reservation> reservations = new ArrayList<>();


        //Creating copy items to be able to edit easily
        for (Reservation r: ReservationData.reservationsMap.get(reservationsTableView.getSelectionModel().getSelectedItem().getValue().getReservationID())){
            reservations.add(new Reservation(r));
        }


        //Removing actual items from reservationsAsList and adding the copies instead
        //This will help handling the edit reservation dates through cells
        //Since reservationsAsList will be refreshed when user goes to another tab, there is no problem to change it here
        List<Reservation> toRemove = new ArrayList<>();
        for(Reservation r: ReservationData.reservationsAsList) {
            if(r.getReservationID() == reservationsTableView.getSelectionModel().getSelectedItem().getValue().getReservationID()) {
                toRemove.add(r);
            }
        }
        ReservationData.reservationsAsList.removeAll(toRemove);
        ReservationData.reservationsAsList.addAll(reservations);

        ((ReservationDetailController)loader.getController()).setSelectedReservation(reservations);
    }


    @FXML
    private void handleAddReservation() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/alancerpro/view/ReservationDetailScreen.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Can't load reservation page");
            alert.showAndWait();
            return;
        }
        Controller.getCurrentMainPane().setCenter(root);
        ((ReservationDetailController)loader.getController()).createNewReservation();
    }

    @FXML
    private void handleDeleteReservation() {
        TreeItem<Reservation> selectedItem = reservationsTableView.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            return;
        }
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("Are you sure?");
        confirmation.setContentText("Payment information with this reservation will be lost");
        Optional<ButtonType> result = confirmation.showAndWait();
        if(result.isEmpty() || result.get().equals(ButtonType.CANCEL)) {
            return;
        }

        if(!ReservationData.getInstance().deleteReservation(selectedItem.getValue().getReservationID())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error deleting reservation.");
            alert.setContentText("Some problem happened with the database");
            alert.showAndWait();
            return;
        }

        ReservationData.reservationsMap.remove(selectedItem.getValue().getReservationID());
        AccountData.accounts.remove(selectedItem.getValue().getReservationID());
        ReservationData.setLists();

    }




    /********** Search Methods **********/
    @FXML
    public void handleSearchButton() {
        String searchParameter = searchReservationTextField.getText();
        if (searchParameter.isBlank()) {
            filteredList.setPredicate(reservation -> true);
            return;
        }

        if (reservationsTableView.getSortOrder().size() <= 0) {
            searchByID(searchParameter);
            return;
        }

        switch(reservationsTableView.getSortOrder().get(0).getText()) {
            case "Reservation ID":
            case "Stay ID":
                searchByID(searchParameter);
                break;
            case "Status":
                searchByStatus(searchParameter);
                break;
            case "Client":
                searchByClient(searchParameter);
                break;
            case "Start Date":
            case "Check-In Date":
                searchByStartDate(searchParameter);
                break;
            case "Check-Out Date":
            case "End Date":
                searchByEndDate(searchParameter);
                break;
            case "Total Days":
                searchByTotalDays(searchParameter);
                break;
        }
    }


    private void searchByID(String searchParameter) {
        int searchValue;
        try {
            searchValue = Integer.parseInt(searchParameter);
        } catch (Exception e) {
            searchValue = 0;
        }
        int searchValueFinal = searchValue;

        filteredList.setPredicate(reservation -> reservation.getReservationID() == searchValueFinal);
    }

    private void searchByStatus(String searchParameter) {
        filteredList.setPredicate(reservation -> reservation.getReservationStatus().toString().toLowerCase().contains(searchParameter.toLowerCase()));
    }

    private void searchByClient(String searchParameter) {
        filteredList.setPredicate(reservation -> reservation.getCustomer().getName().toLowerCase().contains(searchParameter.toLowerCase()));
    }

    private void searchByStartDate(String searchParameter) {
        filteredList.setPredicate(reservation -> reservation.getStartDate().contains(searchParameter));
    }

    private void searchByEndDate(String searchParameter) {
        filteredList.setPredicate(reservation -> reservation.getEndDate().contains(searchParameter));
    }

    private void searchByTotalDays(String searchParameter) {
        int searchValue;
        try {
            searchValue = Integer.parseInt(searchParameter);
        } catch (Exception e) {
            searchValue = 0;
        }
        int searchValueFinal = searchValue;
        filteredList.setPredicate(reservation -> reservation.getNumberOfDays() == searchValueFinal);
    }

    @FXML
    public void handleSearchKey(KeyEvent k) {
        if (k.getCode() == KeyCode.ENTER) {
            handleSearchButton();
        }

    }

}

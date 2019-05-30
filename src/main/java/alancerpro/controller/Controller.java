package alancerpro.controller;

import alancerpro.model.data.*;
import alancerpro.view.*;
import alancerpro.model.User;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;




public class Controller implements Initializable {
    private static Controller instance;
    private static BorderPane currentMainPane;

    private static User loggedInUser;

    public static void setLoggedInUser(User loggedInUser) {
        Controller.loggedInUser = loggedInUser;
    }

    @FXML
    BorderPane mainPane;

    private ReservationScreen reservationScreen;
    private StaysScreen staysScreen;
    private RoomsScreen roomsScreen;
    private CalendarScreen calendarScreen;
    private CustomersScreen customersScreen;
    private UsersScreen usersScreen;




    public Controller() {
        instance = this;
    }

    public static Controller getInstance() {
        return instance;
    }

    public static BorderPane getCurrentMainPane() {
        return currentMainPane;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CustomerData.customers.setAll(CustomerData.getInstance().queryCustomers());
        UserData.users.setAll(UserData.getInstance().queryUsers());
        RoomData.rooms.setAll(RoomData.getInstance().queryRooms());


        ReservationData.reservationsMap.putAll(ReservationData.getInstance().queryViewReservations());
        ReservationData.setLists();
        AccountData.getInstance().queryAccountsAndSetList();


        currentMainPane = mainPane;
        reservationScreen = new ReservationScreen();
        staysScreen = new StaysScreen();
        roomsScreen = new RoomsScreen();
        calendarScreen = new CalendarScreen();
        customersScreen = new CustomersScreen();
        usersScreen = new UsersScreen();


        mainPane.setCenter(reservationScreen);
    }



    @FXML
    void mainMenuTabClicked(ActionEvent event) {
        ReservationData.setLists();
        GridScreen screen;
        switch (((Control)event.getSource()).getId()) {
            case "stays":
                clearReservationsSelection(staysScreen);
                screen = staysScreen;
                break;
            case "rooms":
                ((RoomsController) roomsScreen.fxmlLoader.getController()).removeSearchFilter();
                screen = roomsScreen;
                break;
            case "calendar":
                ((CalendarController)calendarScreen.fxmlLoader.getController()).setColumns();
                screen = calendarScreen;
                break;
            case "customers":
                clearCustomersSelection();
                screen = customersScreen;
                break;
            case "users":
                screen = usersScreen;
                break;
            case "logOut":
                handleLogOut();
                return;
            case "reservations":
            default:
                clearReservationsSelection(reservationScreen);
                screen = reservationScreen;
        }
        mainPane.setCenter(screen);
    }

    public void showReservationsScreen() {
        ReservationData.setLists();
        clearReservationsSelection(reservationScreen);
        mainPane.setCenter(reservationScreen);
    }


    private void clearReservationsSelection(GridScreen screen) {
        ((ReservationsController)screen.fxmlLoader.getController()).reservationsTableView.getSelectionModel().clearSelection();
    }

    public void showCustomersScreen() {
        clearCustomersSelection();
        mainPane.setCenter(customersScreen);
    }

    private void clearCustomersSelection() {
        ((CustomersController)customersScreen.fxmlLoader.getController()).setSelection(null);
    }

    public CustomersController showCustomersScreenGetController() {
        showCustomersScreen();
        return customersScreen.fxmlLoader.getController();
    }

    private void handleLogOut() {
        mainPane.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/alancerpro/view/LoginScreen.fxml"));
        try {
            Parent loginRoot = loader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene(new Scene(loginRoot));
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("Error loading login page." + e.getMessage());
            e.printStackTrace();
        }
        LoginController loginController = loader.getController();
        if(loginController.isExitSignal()) {
            Platform.exit();
        }

        Controller.setLoggedInUser(loginController.getLoggedInUser());
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/alancerpro/view/MainLook.fxml"));
            Stage primaryStage = new Stage();
            primaryStage.setMinWidth(1050);
            primaryStage.setMinHeight(700);
            primaryStage.setScene(new Scene(root, 1350, 800));
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Error loading main screen" + e.getMessage());
            e.printStackTrace();
        }
    }

}

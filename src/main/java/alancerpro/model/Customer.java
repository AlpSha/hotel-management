package alancerpro.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customer extends Person<Customer> {

    private SimpleStringProperty name;
    private SimpleStringProperty id;
    private ObservableList<Reservation> reservations;

    public Customer(String id, String name, String email, String address, String phoneNumber) {
        super(id, name, email, address, phoneNumber);
        reservations = FXCollections.observableArrayList();
    }

    public void addReservation(Reservation res) {
        reservations.add(res);
    }
}

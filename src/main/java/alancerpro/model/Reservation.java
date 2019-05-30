package alancerpro.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Reservation extends RecursiveTreeObject<Reservation> {
    private SimpleStringProperty reservationID;
    private Room room;
    private Customer customer;
    private SimpleStringProperty adults;
    private SimpleStringProperty childs;
    private SimpleStringProperty startDate;
    private SimpleStringProperty endDate;
    private SimpleStringProperty numberOfDays;
    private SimpleStringProperty reservationStatusProperty;
    private String createdBy;
    private SimpleStringProperty createDate;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeFormat.format);

    public Reservation(long ID, Room room, Customer customer, int adults, int childs, String startDate, String endDate, ReservationStatus reservationStatus, String createDate, String createdBy) {
        this.reservationID = new SimpleStringProperty(String.valueOf(ID));
        this.room = room;
        this.customer = customer;
        this.adults = new SimpleStringProperty(String.valueOf(adults));
        this.childs = new SimpleStringProperty(String.valueOf(childs));
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
        this.reservationStatusProperty = new SimpleStringProperty(reservationStatus.toString());
        this.createdBy = createdBy;
        this.createDate = new SimpleStringProperty(createDate);

        numberOfDays = new SimpleStringProperty(String.valueOf(calculateNumberOfDays()));
    }

    public Reservation(Reservation r) {
        this(r.getReservationID(), r.getRoom(), r.getCustomer(), r.getAdults(), r.getChilds(), r.getStartDate(), r.getEndDate(), r.getReservationStatus(), r.getCreateDate(), r.createdBy);
    }




    public SimpleStringProperty reservationStatusPropertyProperty() {
        return reservationStatusProperty;
    }

    public long getReservationID() {
        return Long.parseLong(reservationID.get());
    }

    public SimpleStringProperty reservationIDProperty() {
        return reservationID;
    }

    public int getAdults() {
        return Integer.parseInt(adults.get());
    }

    public SimpleStringProperty adultsProperty() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults.set(String.valueOf(adults));
    }

    public int getChilds() {
        return Integer.parseInt(childs.get());
    }

    public SimpleStringProperty childsProperty() {
        return childs;
    }

    public void setChilds(int childs) {
        this.childs.set(String.valueOf(childs));
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getStartDate() {
        return startDate.get();
    }

    public SimpleStringProperty startDateProperty() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate.set(startDate);
        this.numberOfDays.set(String.valueOf(calculateNumberOfDays()));
    }

    public String getEndDate() {
        return endDate.get();
    }

    public SimpleStringProperty endDateProperty() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate.set(endDate);
        this.numberOfDays.set(String.valueOf(calculateNumberOfDays()));
    }

    public void setNumberOfDays(long numberOfDays) {
        this.numberOfDays.set(String.valueOf(numberOfDays));
    }

    public int getNumberOfDays() {
        return Integer.parseInt(numberOfDays.get());
    }

    public long calculateNumberOfDays() throws IllegalArgumentException {
        LocalDate startDate = LocalDate.parse(this.startDate.get(), formatter);
        LocalDate endDate = LocalDate.parse(this.endDate.get(), formatter);
        return startDate.datesUntil(endDate).count();

    }

    public LocalDate getStartDateAsDate () {
        return LocalDate.parse(getStartDate(), formatter);
    }

    public LocalDate getEndDateAsDate () {
        return LocalDate.parse(getEndDate(), formatter);
    }

    public SimpleStringProperty numberOfDaysProperty() {
        return numberOfDays;
    }


    public ReservationStatus getReservationStatus() {
        return ReservationStatus.valueOf(reservationStatusProperty.getValue());
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        reservationStatusProperty.setValue(reservationStatus.toString());
    }

    public String getCreateDate() {
        return createDate.get();
    }

    public SimpleStringProperty createDateProperty() {
        return createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public boolean equals(Object obj) {
        if(!obj.getClass().equals(Reservation.class)) {
            return false;
        }
        Reservation r = (Reservation) obj;
        if(r.getReservationID() == this.getReservationID())
        {
            if (r.getRoom().getRoomNumber() == this.getRoom().getRoomNumber() && r.getStartDate().equals(this.getStartDate()) && r.getEndDate().equals(this.getEndDate())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationID=" + reservationID.getValue() +
                ", room=" + room.getRoomNumber() +
                ", customer=" + customer.getName() +
                ", adults=" + adults.getValue() +
                ", childs=" + childs.getValue() +
                ", startDate=" + startDate.getValue() +
                ", endDate=" + endDate.getValue() +
                ", numberOfDays=" + numberOfDays.getValue() +
                ", reservationStatusProperty=" + reservationStatusProperty.getValue() +
                ", createdBy='" + createdBy +
                ", createDate=" + createDate.getValue() +
                '}';
    }
}


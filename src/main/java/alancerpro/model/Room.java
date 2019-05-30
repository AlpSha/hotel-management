package alancerpro.model;

import alancerpro.model.data.ReservationData;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Room extends RecursiveTreeObject<Room> {

    private SimpleStringProperty roomNumber;
    private ObservableMap<LocalDate, Reservation> reservations = FXCollections.emptyObservableMap();
    private SimpleStringProperty roomType;
    private SimpleStringProperty roomPrice;


    public Room(int roomNumber, RoomType roomType) {
        this.roomNumber = new SimpleStringProperty(String.valueOf(roomNumber));
        this.roomType = new SimpleStringProperty(roomType.toString());
        this.roomPrice = new SimpleStringProperty(String.valueOf(roomType.getRoomPrice()));
    }



    public boolean isAvailableBetween(LocalDate startDate, LocalDate endDate) {
        for (Reservation res: ReservationData.reservationsAsList) {
            if(res.getRoom().getRoomNumber() != this.getRoomNumber()) {
                continue;
            }
            LocalDate sd = res.getStartDateAsDate();
            LocalDate ed = res.getEndDateAsDate();
            if((sd.compareTo(startDate) >= 0 && sd.compareTo(endDate) < 0) ||
            (sd.compareTo(startDate) < 0) && (ed.compareTo(startDate) > 0)) {
                return false;
            }
        }
        return true;
    }

    public int isAvailableAt(LocalDate date) {
        for(Reservation res: ReservationData.reservationsAsList) {
            if(res.getRoom().getRoomNumber() != this.getRoomNumber()) {
                continue;
            }
            LocalDate sd = res.getStartDateAsDate();
            LocalDate ed = res.getEndDateAsDate();

            if(date.compareTo(sd) == 0) {
                return 0;
            }
            if(date.compareTo(sd) > 0 && date.compareTo(ed) < 0) {
                return -1;
            }
        }
        return 1;
    }

    public SimpleStringProperty isAvailableAtProperty(LocalDate date) {
        int result = isAvailableAt(date);
        switch (result) {
            case 1:
                return new SimpleStringProperty("available");
            case 0:
                return new SimpleStringProperty("starts");
            case -1:
                return new SimpleStringProperty("unavailable");
        }
        return null;
    }

    public Reservation getReservationOnDate(LocalDate date) {
        for(Reservation res: ReservationData.reservationsAsList) {
            if(res.getRoom().getRoomNumber() != this.getRoomNumber()) {
                continue;
            }
            LocalDate sd = res.getStartDateAsDate();
            LocalDate ed = res.getEndDateAsDate();

            if(date.compareTo(sd) >= 0 && date.compareTo(ed) <= 0) {
                return res;
            }
        }
        return null;
    }
    public void addReservation(Reservation reservation) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-mm-yyyy");

        LocalDate startDate = LocalDate.parse(reservation.getStartDate(), dtf);
        LocalDate endDate = LocalDate.parse(reservation.getEndDate(), dtf);
        for (;!startDate.equals(endDate); startDate = startDate.plusDays(1)) {
            reservations.put(startDate, reservation);
        }
    }

    public void removeReservation(Reservation reservation) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-mm-yyyy");

        LocalDate startDate = LocalDate.parse(reservation.getStartDate(), dtf);
        LocalDate endDate = LocalDate.parse(reservation.getEndDate(), dtf);
        for (;!startDate.equals(endDate); startDate = startDate.plusDays(1)) {
                reservations.remove(startDate);
        }
    }




    public double getRoomPrice() {
        return Double.parseDouble(roomPrice.get());
    }

    public SimpleStringProperty roomPriceProperty() {
        return roomPrice;
    }

    public void setRoomPrice(double roomPrice) {
        this.roomPrice.set(String.valueOf(roomPrice));
    }

    public Map getReservations() {
        return FXCollections.observableMap(reservations);
    }

    public int getRoomNumber() {
        return Integer.parseInt(roomNumber.get());
    }

    public SimpleStringProperty roomNumberProperty() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber.set(String.valueOf(roomNumber));
    }

    public RoomType getRoomType() {
        return RoomType.valueOf(roomType.get());
    }

    public void setRoomType(RoomType roomType) {
        this.roomType.setValue(roomType.toString());
    }

    public SimpleStringProperty roomTypeProperty() {
        return roomType;
    }
}

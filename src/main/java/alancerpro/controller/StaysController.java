package alancerpro.controller;

import alancerpro.model.ReservationStatus;
import java.net.URL;
import java.util.ResourceBundle;

public class StaysController extends ReservationsController {




    /************* Initialize **************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        filteredList.setPredicate(res -> res.getReservationStatus().equals(ReservationStatus.checkedIn));
    }
}

package alancerpro.controller;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class AllReservationsController extends ReservationsController implements Initializable {




    /************* Initialize *************/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        columnStatus.setCellValueFactory(param -> param.getValue().getValue().reservationStatusPropertyProperty());
    }
}
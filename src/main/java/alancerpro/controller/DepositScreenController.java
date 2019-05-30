package alancerpro.controller;

import alancerpro.model.Account;
import alancerpro.model.Payment;
import alancerpro.model.PaymentType;
import alancerpro.model.data.AccountData;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DepositScreenController implements Initializable {
    @FXML
    JFXButton moreButton;
    @FXML
    JFXButton btnCash;
    @FXML
    JFXButton btnVisa;
    @FXML
    JFXButton btnMasterCard;
    @FXML
    JFXButton btnAmericanExpress;
    @FXML
    JFXButton btnCheck;
    @FXML
    AnchorPane detailsPane;
    @FXML
    TreeTableView<Payment> paymentsTableView;
    @FXML
    TreeTableColumn<Payment, String> columnAmount;
    @FXML
    TreeTableColumn<Payment, String> columnType;
    @FXML
    TextField paidAmount;




    private ObservableList<Payment> paymentsList = FXCollections.observableArrayList();

    private final TreeItem<Payment> root = new RecursiveTreeItem<>(paymentsList, RecursiveTreeObject::getChildren);


    private JFXButton lastSelectedPaymentButton;
    private boolean detailsIsVisible = false;
    private long currentReservationId = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnAmericanExpress.setAccessibleText(PaymentType.americanExpress.toString());
        btnCash.setAccessibleText(PaymentType.cash.toString());
        btnCheck.setAccessibleText(PaymentType.check.toString());
        btnMasterCard.setAccessibleText(PaymentType.masterCard.toString());
        btnVisa.setAccessibleText(PaymentType.visa.toString());

        lastSelectedPaymentButton = null;
    }

    void setCurrentAccount(Account account) {
        //Creating copy payments list
        List<Payment> tmpPayments = new ArrayList<>();
        for (Payment p: account.getPayments()) {
            tmpPayments.add(new Payment(p));
        }

        paymentsList.setAll(tmpPayments);
        columnAmount.setCellValueFactory(param -> param.getValue().getValue().paymentAmountProperty());
        columnType.setCellValueFactory(param -> param.getValue().getValue().paymentTypeProperty());

        this.currentReservationId = account.getReservationID();

        paymentsTableView.setRoot(root);
        paymentsTableView.setShowRoot(false);
    }


    @FXML
    void handleMoreButton() {
        if(!detailsIsVisible) {
            moreButton.getScene().getWindow().setHeight(800);
        } else {
            moreButton.getScene().getWindow().setHeight(460);
        }
        detailsPane.setVisible(!detailsIsVisible);
        detailsIsVisible = !detailsIsVisible;
    }

    @FXML
    void handlePaymentTypeButtons(ActionEvent e) {
        JFXButton selectedButton = ((JFXButton)e.getSource());
        if(lastSelectedPaymentButton != null && lastSelectedPaymentButton.equals(selectedButton)) {
            return;
        }

        if(lastSelectedPaymentButton != null) {
            lastSelectedPaymentButton.setStyle("-fx-background-color:  #00B4FF");
            lastSelectedPaymentButton.setTextFill(Paint.valueOf("#133067"));
        }
        selectedButton.setStyle("-fx-background-color:   #133067");
        selectedButton.setTextFill(Paint.valueOf("#fff"));
        lastSelectedPaymentButton = selectedButton;
    }

    @FXML
    void handleAddButton() {
        double amount = 0;
        try {
            amount = Double.parseDouble(paidAmount.getText());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
            return;
        }
        paidAmount.setText("");


        PaymentType type;
        if(lastSelectedPaymentButton != null) {
            type = PaymentType.valueOf(lastSelectedPaymentButton.getAccessibleText());
        } else {
            type = PaymentType.cash;
        }
        paymentsList.add(new Payment(type, amount));

    }


    @FXML
    void handleEnterKey(KeyEvent k) {
        if(k.getCode().equals(KeyCode.ENTER)) {
            handleAddButton();
        }
    }

    @FXML
    void handleOkButton() {
        if(paidAmount.getText() != null) {
            handleAddButton();
        }

        if(!AccountData.getInstance().updatePaymentsWithCurrentList(this.currentReservationId, paymentsList)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error updating information.");
            alert.setContentText("We couldn't update payment information. Leaving as it is.");
            alert.showAndWait();
            return;
        }

        ObservableList<Payment> newList = FXCollections.observableArrayList();

        for (Payment p: paymentsList) {
            newList.add(new Payment(p));
        }

        AccountData.accounts.get(this.currentReservationId).setPaymentsList(newList);
        handleCancelButton();
    }

    @FXML
    void handleCancelButton() {
        paymentsTableView.getScene().getWindow().hide();
    }

    @FXML
    void handleDeleteButton() {
        paymentsList.remove(paymentsTableView.getSelectionModel().getSelectedItem().getValue());
    }

}
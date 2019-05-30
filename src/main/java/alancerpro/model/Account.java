package alancerpro.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Account extends RecursiveTreeObject<Account> {
    private SimpleStringProperty reservationID;
    private SimpleStringProperty debt;
    private SimpleStringProperty balance;
    private ObservableList<Payment> payments;

    public Account(long reservationID, double debt) {
        this.reservationID = new SimpleStringProperty(String.valueOf(reservationID));
        this.debt = new SimpleStringProperty(String.format("%.2f",debt));
        this.payments = FXCollections.observableArrayList();
        this.balance = new SimpleStringProperty(this.debt.get());
    }

    public Account(Account account) {
        this(account.getReservationID(), account.getDebt());
        this.payments.setAll(account.getPayments());
        this.balance.setValue(this.debt.get());
    }

    public boolean addPayment(Payment payment) {
        if(payment.getPaymentAmount() > 0) {
            balance.set(String.valueOf(getBalance()-payment.getPaymentAmount()));
            payments.add(payment);
            return true;
        }
        return false;
    }


    public ObservableList<Payment> getPayments() {
        return payments;
    }

    public void setPaymentsList(ObservableList<Payment> list) {
        this.payments = list;
    }

    public double getDebt() {
        return Double.parseDouble(debt.get());
    }

    public SimpleStringProperty debtProperty() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt.set(String.valueOf(debt));
    }

    public long getReservationID() {
        return Long.parseLong(reservationID.get());
    }

    public SimpleStringProperty reservationIDProperty() {
        return reservationID;
    }

    public void setReservationID(long reservationID) {
        this.reservationID.set(String.valueOf(reservationID));
    }

    public double getBalance() {
        double balance = getDebt();
        for (Payment p: payments) {
            balance -= p.getPaymentAmount();
        }
        return balance;
    }

    public SimpleStringProperty balanceProperty() {
        balance.set(String.valueOf(getBalance()));
        return balance;

    }

}

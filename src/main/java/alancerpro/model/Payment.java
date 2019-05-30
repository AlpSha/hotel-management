package alancerpro.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class Payment extends RecursiveTreeObject<Payment> {
    private SimpleStringProperty paymentType;
    private SimpleStringProperty paymentAmount;

    public Payment(PaymentType paymentType, double paymentAmount) {
        this.paymentType = new SimpleStringProperty(paymentType.toString());
        this.paymentAmount = new SimpleStringProperty(String.valueOf(paymentAmount));
    }

    public Payment(Payment p) {
        this(p.getPaymentType(), p.getPaymentAmount());
    }


    public PaymentType getPaymentType() {
        return PaymentType.valueOf(paymentType.get());
    }

    public SimpleStringProperty paymentTypeProperty() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType.set(paymentType.toString());
    }

    public double getPaymentAmount() {
        return Double.parseDouble(paymentAmount.get());
    }

    public SimpleStringProperty paymentAmountProperty() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount.set(paymentAmount);
    }
}

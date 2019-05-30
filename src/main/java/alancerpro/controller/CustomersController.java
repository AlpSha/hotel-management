package alancerpro.controller;

import alancerpro.model.*;
import alancerpro.model.data.AccountData;
import alancerpro.model.data.CustomerData;
import alancerpro.model.data.ReservationData;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomersController implements Initializable, Searchable {

    @FXML
    private JFXTextField searchReservationTextField;

    @FXML
    private JFXButton searchReservationButton;

    @FXML
    private TreeTableColumn<Customer, String> columnCustomerName;

    @FXML
    private TreeTableColumn<Customer, String> columnPhoneNumber;

    @FXML
    private TreeTableColumn<Customer, String> columnEmail;

    @FXML
    private TreeTableColumn<Customer, String> columnAddress;

    @FXML
    private TreeTableView<Customer> customersTableView;

    @FXML
    private TextField searchCustomerTextField;

    @FXML
    private JFXTextField idTextField;

    @FXML
    private JFXTextField nameTextField;

    @FXML
    private JFXTextField phoneTextField;

    @FXML
    private JFXTextField emailTextField;

    @FXML
    private JFXTextField addressTextField;

    @FXML
    private JFXTextField address2TextField;

    @FXML
    private JFXTreeTableView<Account> accountsTableView;

    @FXML
    private TreeTableColumn<Account, String> columnReservationId;

    @FXML
    private TreeTableColumn<Account, String> columnBalance;

    @FXML
    private TreeTableColumn<Account, String> columnAccountDebt;

    @FXML
    private JFXTreeTableView<Payment> paymentsTableView;

    @FXML
    private TreeTableColumn<Payment, String> columnPaymentAmount;

    @FXML
    private TreeTableColumn<Payment, String> columnPaymentType;

    @FXML
    private JFXButton selectForReservationButton;

    @FXML
    private Text totalBalanceText;


    private FilteredList<Customer> customerFilteredList = new FilteredList<>(CustomerData.customers);

    private final TreeItem<Customer> rootCustomers = new RecursiveTreeItem<>(customerFilteredList, RecursiveTreeObject::getChildren);

    private ObservableList<Account> accountsList = FXCollections.observableArrayList();

    private final TreeItem<Account> rootAccounts = new RecursiveTreeItem<>(accountsList, RecursiveTreeObject::getChildren);

    private ObservableList<Payment> paymentsList = FXCollections.observableArrayList();

    private final TreeItem<Payment> rootPayments = new RecursiveTreeItem<>(paymentsList, RecursiveTreeObject::getChildren);


    private Customer selectedCustomer;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customerFilteredList.setPredicate(customer -> true);

        columnCustomerName.setCellValueFactory(cell -> cell.getValue().getValue().nameProperty());
        columnPhoneNumber.setCellValueFactory(cell -> cell.getValue().getValue().phoneNumberProperty());
        columnEmail.setCellValueFactory(cell -> cell.getValue().getValue().emailProperty());
        columnAddress.setCellValueFactory(cell -> cell.getValue().getValue().addressProperty());

        customersTableView.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldSelection, newSelection) -> {
            if(newSelection != null) {
                fillCustomerDetails(newSelection.getValue());
            } else {
                fillCustomerDetails(null);
            }
        }));

        customersTableView.setRowFactory( tv -> {
            TreeTableRow<Customer> row = new TreeTableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) && !selectForReservationButton.isDisable()) {
                    handleSelectForReservation();
                }
            });
            return row ;
        });



        customersTableView.setRoot(rootCustomers);
        customersTableView.setShowRoot(false);
        selectedCustomer = null;


        columnReservationId.setCellValueFactory(cell -> cell.getValue().getValue().reservationIDProperty());
        columnBalance.setCellValueFactory(cell -> cell.getValue().getValue().balanceProperty());
        columnAccountDebt.setCellValueFactory(cell -> cell.getValue().getValue().debtProperty());

        accountsTableView.setRoot(rootAccounts);
        accountsTableView.setShowRoot(false);

        accountsTableView.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(newValue != null) {
                handlePaymentListDetails(newValue.getValue());
            } else {
                handlePaymentListDetails(null);
            }
        }));


        columnPaymentAmount.setCellValueFactory(cell -> cell.getValue().getValue().paymentAmountProperty());
        columnPaymentType.setCellValueFactory(cell -> cell.getValue().getValue().paymentTypeProperty());

        paymentsTableView.setRoot(rootPayments);
        paymentsTableView.setShowRoot(false);
    }



    @FXML
    void handleAddButton() {
        String id;
        String name;
        String email;
        String phone;
        String address;

        try {
            if((name = nameTextField.getText()).length() < 10 || (email = emailTextField.getText()).length() < 7 || (id = idTextField.getText()).length() < 5){
                throw new NullPointerException();
            }
            address = addressTextField.getText()+ " " + address2TextField.getText();
            phone = phoneTextField.getText();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Invalid input");
            alert.setContentText("Please fill the fields correctly");
            alert.showAndWait();
            return;
        }
        Customer newCustomer = new Customer(id, name, email, address, phone);

        if(!CustomerData.getInstance().insertIntoCustomers(newCustomer)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error saving customer.");
            alert.setContentText("Please check your connection with database.");
            alert.showAndWait();
            return;
        }
        CustomerData.customers.add(newCustomer);
        setSelection(newCustomer);
    }

    public void setSelection(Customer customer) {
        if(customer == null) {
            customersTableView.getSelectionModel().clearSelection();
            return;
        }

        TreeItem<Customer> item = null;
        for (TreeItem<Customer> c: customersTableView.getRoot().getChildren()) {
            if(c.getValue().getId().equals(customer.getId())) {
                item = c;
                break;
            }
        }
        if(item == null) {
            return;
        }
        customersTableView.getSelectionModel().select(item);
        fillCustomerDetails(customer);
    }


    @FXML
    void handleDeleteButton() {
        Customer customer = customersTableView.getSelectionModel().getSelectedItem().getValue();
        if(ReservationData.getInstance().queryReservationsViewWithCustomerAndStatus(customer.getId(), ReservationStatus.checkedIn).size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Can't delete customer");
            alert.setContentText("Customer is still checked-in at hotel.");
            alert.showAndWait();
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("Are you sure?");
        confirmation.setContentText("All reservations associated also will be deleted");
        Optional<ButtonType> resultConf = confirmation.showAndWait();
        if(resultConf.isEmpty() || resultConf.get() == ButtonType.CANCEL) {
           return;
        }

        List<Long> toRemoveReservationIds = ReservationData.getInstance().queryReservationIdsWithCustomer(customer.getId());

        if(!CustomerData.getInstance().deleteCustomerWithReservations(customer.getId())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error deleting customer");
            alert.setContentText("We couldn't delete customer from database.");
            alert.showAndWait();
            return;
        }

        for (Long l: toRemoveReservationIds) {
            System.out.println("removing with id: " + l);
            ReservationData.reservationsMap.remove(l);
        }
        CustomerData.customers.remove(customer);
        ReservationData.setLists();

    }

    @FXML
    void handleClearButton() {
        setSelection(null);
    }

    @FXML
    void handleSaveButton() {
        if(selectedCustomer == null) {
            return;
        }

        if(idTextField.getText().length()<5 || nameTextField.getText().length()<7 || emailTextField.getText().length()<6) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wrong input");
            alert.setContentText("Please fill information correctly");
            alert.showAndWait();
            return;
        }

        Customer newCustomer = new Customer(idTextField.getText(), nameTextField.getText(), emailTextField.getText(), addressTextField.getText()+ " " + address2TextField.getText(), phoneTextField.getText());


        if(!CustomerData.getInstance().updateCustomer(selectedCustomer.getId(), newCustomer)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error updating customer");
            alert.setContentText("We couldn't update customer information on database");
            alert.showAndWait();
            return;
        }

        selectedCustomer.setAddress(newCustomer.getAddress());
        selectedCustomer.setId(newCustomer.getId());
        selectedCustomer.setEmail(newCustomer.getEmail());
        selectedCustomer.setName(newCustomer.getName());
        selectedCustomer.setPhoneNumber(newCustomer.getPhoneNumber());
    }




    private void fillCustomerDetails(Customer customer) {
        selectedCustomer = customer;

        if(customer == null) {
            idTextField.setText("");
            nameTextField.setText("");
            phoneTextField.setText("");
            emailTextField.setText("");
            addressTextField.setText("");
            address2TextField.setText("");
            accountsList.setAll(FXCollections.observableArrayList());
            paymentsList.setAll(FXCollections.observableArrayList());
            return;
        }

        idTextField.setText(customer.getId());
        nameTextField.setText(customer.getName());
        phoneTextField.setText(customer.getPhoneNumber());
        emailTextField.setText(customer.getEmail());
        String[] address = new String[2];
        if(customer.getAddress().length() > 70) {
            address[0] = customer.getAddress().substring(0, 70);
            address[1] = customer.getAddress().substring(70);
            addressTextField.setText(address[0]);
            address2TextField.setText(address[1]);
        } else {
            addressTextField.setText(customer.getAddress());
            address2TextField.setText("");
        }

        List<Long> reservationIds;
        if((reservationIds = ReservationData.getInstance().queryReservationIdsWithCustomer(customer.getId())) == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("An error occurred.");
            alert.setContentText("Couldn't get account information");
            alert.showAndWait();
            return;
        }

        double totalBalance = 0;

        accountsList.setAll(FXCollections.observableArrayList());
        for (Long resId: reservationIds) {
            Account a = AccountData.accounts.get(resId);
            accountsList.add(a);
            totalBalance += a.getBalance();
        }

        this.totalBalanceText.setText(String.format("%.2f" ,totalBalance));

        paymentsList.setAll(FXCollections.observableArrayList());
    }

    private void handlePaymentListDetails(Account account) {
        if(account == null || account.getPayments() == null) {
            paymentsList.setAll(FXCollections.observableArrayList());
        } else {
            paymentsList.setAll(account.getPayments());
        }
    }






    /********** Selection For Reservation **********/
    private GridPane ownerScreen;
    private ReservationDetailController ownerController;

    public void setAsSelectForReservation(GridPane ownerScreen, ReservationDetailController ownerController) {
        this.ownerScreen = ownerScreen;
        this.ownerController = ownerController;
        selectForReservationButton.setVisible(true);
    }

    @FXML
    void handleSelectForReservation() {
        TreeItem<Customer> selectedItem = customersTableView.getSelectionModel().getSelectedItem();
        if(selectedItem == null) {
            return;
        }
        ownerController.setCustomer(selectedItem.getValue());
        Controller.getCurrentMainPane().setCenter(ownerScreen);
        this.ownerScreen = null;
        this.ownerController = null;
        selectForReservationButton.setVisible(false);
    }





    /********** Search Methods **********/
    @FXML
    public void handleSearchKey(KeyEvent k) {
        if(k.getCode().equals(KeyCode.ENTER)) {
            handleSearchButton();
        }
    }

    @FXML
    public void handleSearchButton() {
        String searchParameter = searchCustomerTextField.getText();
        if (searchParameter.isBlank()) {
            customerFilteredList.setPredicate(customer -> true);
            return;
        }

        if (customersTableView.getSortOrder().size() <= 0) {
            searchByCustomerName(searchParameter);
            return;
        }

        switch(customersTableView.getSortOrder().get(0).getText()) {
            case "Name":
                searchByCustomerName(searchParameter);
                break;
            case "Phone Number":
                searchByPhoneNumber(searchParameter);
                break;
            case "Email":
                searchByEmail(searchParameter);
                break;
            case "Address":
                searchByAddress(searchParameter);
                break;
        }
    }

    private void searchByCustomerName(String searchParameter) {
        customerFilteredList.setPredicate(customer -> customer.getName().toLowerCase().contains(searchParameter.toLowerCase()));
    }

    private void searchByPhoneNumber(String searchParameter) {
        customerFilteredList.setPredicate(customer -> customer.getPhoneNumber().contains(searchParameter));
    }

    private void searchByEmail(String searchParameter) {
        customerFilteredList.setPredicate(customer -> customer.getEmail().toLowerCase().contains(searchParameter.toLowerCase()));
    }

    private void searchByAddress(String searchParameter) {
        customerFilteredList.setPredicate(customer -> customer.getAddress().toLowerCase().contains(searchParameter.toLowerCase()));
    }
}

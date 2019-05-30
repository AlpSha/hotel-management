package alancerpro.model.data;

import alancerpro.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerData extends Queries {
    private CustomerData() {}
    private static CustomerData instance = new CustomerData();
    public static CustomerData getInstance() {
        return instance;
    }

    public static ObservableList<Customer> customers = FXCollections.observableArrayList();


    public List<Customer> queryCustomers() {
        try {
            return runCustomerQuery(queryCustomers);
        } catch(SQLException e) {
            System.out.println("Error querying customers: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public Customer queryCustomerWithId(String customerId) {
        try {
            queryCustomerWithId.setString(1, customerId);
            return runCustomerQuery(queryCustomerWithId).get(0);
        }catch (SQLException e) {
            System.out.println("Error querying with customer id: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private List<Customer> runCustomerQuery(PreparedStatement statement) throws SQLException {
        List<Customer> customersList = new ArrayList<>();
            ResultSet results = statement.executeQuery();
            while(results.next()) {
                String cusId = results.getString(COLUMN_CUSTOMER_ID_INDEX);
                String cusName = results.getString(COLUMN_CUSTOMER_NAME_INDEX);
                String phoneNumber = results.getString(COLUMN_CUSTOMER_PHONE_NUMBER_INDEX);
                String email = results.getString(COLUMN_CUSTOMER_EMAIL_INDEX);
                String address = results.getString(COLUMN_CUSTOMER_ADDRESS_INDEX);
                customersList.add(new Customer(cusId, cusName, email, address, phoneNumber));
            }
            return customersList;
    }

    public boolean insertIntoCustomers(Customer customer) {
        try {
            insertIntoCustomers.setString(1, customer.getId());
            insertIntoCustomers.setString(2, customer.getName());
            insertIntoCustomers.setString(3, customer.getPhoneNumber());
            insertIntoCustomers.setString(4, customer.getEmail());
            insertIntoCustomers.setString(5, customer.getAddress());
            insertIntoCustomers.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting into customers: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomerWithReservations(String customerId) {
        try {
            conn.setAutoCommit(false);
            List<Long> reservationIds = new ArrayList<>();
            queryViewReservationsWithCustomerIdAndStatus.setString(1, customerId);
            ResultSet resultSet = queryViewReservationsWithCustomerIdAndStatus.executeQuery();
            while(resultSet.next()) {
                reservationIds.add(resultSet.getLong(COLUMN_VIEW_RESERVATION_CUSTOMER_ID_INDEX));
            }
            for(long r: reservationIds) {
                ReservationData.getInstance().deleteFromReservationsWithReservationId(r);
            }
            ReservationData.getInstance().deleteFromReservationsJointWithCustomer(customerId);
            deleteCustomer(customerId);

            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting customer: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Oh, boy. Things are going crazy. " + e2.getMessage());
                e.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Can't set autocommit. We are in trouble: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean updateCustomer(String customerId, Customer customer) {
        try {
            updateCustomer.setString(6, customerId);
            updateCustomer.setString(1, customer.getId());
            updateCustomer.setString(2, customer.getName());
            updateCustomer.setString(3, customer.getPhoneNumber());
            updateCustomer.setString(4, customer.getEmail());
            updateCustomer.setString(5, customer.getAddress());
            updateCustomer.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void deleteCustomer(String customerId) throws SQLException {
        deleteCustomerWithId.setString(1, customerId);
        deleteCustomerWithId.execute();
    }
}

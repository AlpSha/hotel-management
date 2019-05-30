package alancerpro.model.data;

import alancerpro.model.Account;
import alancerpro.model.Payment;
import alancerpro.model.PaymentType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountData extends Queries {
    private AccountData() {

    }
    private static AccountData instance = new AccountData();
    public static AccountData getInstance() {
        return instance;
    }

    public static ObservableMap<Long, Account> accounts = FXCollections.observableHashMap();

    public void queryAccountsAndSetList() {
        try {
            ResultSet resultSet1 = queryAccounts.executeQuery();
            while(resultSet1.next()) {
                long resId = resultSet1.getLong(COLUMN_ACCOUNTS_RESERVATION_ID_INDEX);
                double debt = resultSet1.getDouble(COLUMN_ACCOUNTS_DEBT_INDEX);
                accounts.put(resId, new Account(resId, debt));
            }
            ResultSet resultSet2 = queryViewPayments.executeQuery();
            while (resultSet2.next()) {
                long resId = resultSet2.getLong(COLUMN_VIEW_PAYMENT_RESERVATION_ID_INDEX);
                double payAmount = resultSet2.getDouble(COLUMN_VIEW_PAYMENT_AMOUNT_INDEX);
                PaymentType payType = PaymentType.valueOf(resultSet2.getString(COLUMN_VIEW_PAYMENT_TYPE_INDEX));
                accounts.get(resId).addPayment(new Payment(payType, payAmount));
            }
        } catch (SQLException e) {
            System.out.println("Error querying payment views: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void insertIntoAccounts(Account account) {
        try {
            insertIntoAccounts.setLong(1, account.getReservationID());
            insertIntoAccounts.setDouble(2, account.getBalance());
            insertIntoAccounts.execute();
        } catch (SQLException e) {
            System.out.println("Error inserting into accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void deleteFromAccounts(long reservationId) throws SQLException {
        deleteAccountWithReservationId.setLong(1, reservationId);
        deleteAccountWithReservationId.execute();
    }





    public boolean updatePaymentsWithCurrentList(long reservationId, List<Payment> payments) {
        try {
            conn.setAutoCommit(false);
            deleteFromPaymentsWithReservationId(reservationId);
            for (Payment p: payments) {
                insertIntoPayments(reservationId, p);
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error changing payments: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Error setting autocommit to true, we got trouble. " + e.getMessage());
                e.printStackTrace();
            }
        }

    }


    private void insertIntoPayments(long reservationId, Payment payment) throws SQLException {
        insertIntoPayments.setLong(1, reservationId);
        insertIntoPayments.setDouble(2, payment.getPaymentAmount());
        insertIntoPayments.setString(3, payment.getPaymentType().toString());
        insertIntoPayments.execute();
    }

    private void deleteFromPaymentsWithReservationId (long reservationId) throws SQLException {
        deletePaymentWithReservationId.setLong(1, reservationId);
        deletePaymentWithReservationId.execute();
    }



    public boolean updateAccountDebt(Account account) {
        try {
            updateAccountDebt.setString(1, String.format("%.2f", account.getDebt()));
            updateAccountDebt.setLong(2, account.getReservationID());
            updateAccountDebt.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating account debt: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSinglePayment(long reservationId, Payment payment) {
        try {
            deleteSinglePayment.setLong(1, reservationId);
            deleteSinglePayment.setDouble(2, payment.getPaymentAmount());
            deleteSinglePayment.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting payment: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}

package alancerpro.model.data;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector extends Queries {
    private DBConnector() {

    }

    private static DBConnector connector = new DBConnector();
    public static DBConnector getInstance() {
        return connector;
    }


    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);


            preparedStatements.add(insertIntoReservations = conn.prepareStatement(INSERT_RESERVATION, Statement.RETURN_GENERATED_KEYS));
            preparedStatements.add(insertIntoReservationsJoint = conn.prepareStatement(INSERT_RESERVATIONS_JOINT));
            preparedStatements.add(updateReservation = conn.prepareStatement(UPDATE_RESERVATION));
            preparedStatements.add(updateReservationCustomer = conn.prepareStatement(UPDATE_RESERVATION_CUSTOMER));
            preparedStatements.add(updateReservationRoom = conn.prepareStatement(UPDATE_RESERVATION_ROOM));
            preparedStatements.add(updateReservationJoint = conn.prepareStatement(UPDATE_RESERVATION_JOINT));
            preparedStatements.add(updateReservationStatus = conn.prepareStatement(UPDATE_RESERVATION_STATUS));
            preparedStatements.add(deleteReservation = conn.prepareStatement(DELETE_RESERVATIONS_WITH_RESERVATION_ID));
            preparedStatements.add(queryRoomWithRoomNumber = conn.prepareStatement(QUERY_ROOM_WITH_ID));
            preparedStatements.add(queryCustomerWithId = conn.prepareStatement(QUERY_CUSTOMER_WITH_ID));
            preparedStatements.add(queryCustomers = conn.prepareStatement(QUERY_CUSTOMERS));
            preparedStatements.add(queryCustomers = conn.prepareStatement(QUERY_CUSTOMERS));
            preparedStatements.add(queryRooms = conn.prepareStatement(QUERY_ROOMS));
            preparedStatements.add(queryViewReservationsPage = conn.prepareStatement(QUERY_VIEW_RESERVATION_PAGE));
            preparedStatements.add(queryViewReservationsWithReservationId = conn.prepareStatement(QUERY_RESERVATIONS_VIEW_WITH_ID));
            preparedStatements.add(queryViewReservationsWithStatus = conn.prepareStatement(QUERY_RESERVATIONS_VIEW_WITH_STATUS));
            preparedStatements.add(deleteReservationJoint = conn.prepareStatement(DELETE_RESERVATIONS_JOINT_WITH_ID));
            preparedStatements.add(queryTableReservationsWithReservationId = conn.prepareStatement(QUERY_RESERVATIONS_TABLE_WITH_RESERVATION_ID));
            preparedStatements.add(queryViewPaymentsWithReservationId = conn.prepareStatement(QUERY_VIEW_PAYMENTS_WITH_RES_ID));
            preparedStatements.add(queryViewPayments = conn.prepareStatement(QUERY_VIEW_PAYMENTS));
            preparedStatements.add(insertIntoPayments = conn.prepareStatement(INSERT_INTO_PAYMENTS));
            preparedStatements.add(updateAccountDebt = conn.prepareStatement(UPDATE_ACCOUNT_DEBT_WITH_RESERVATION_ID));
            preparedStatements.add(queryAccounts = conn.prepareStatement(QUERY_ACCOUNTS));
            preparedStatements.add(deletePaymentWithReservationId = conn.prepareStatement(DELETE_FROM_PAYMENTS_WITH_RESERVATION_ID));
            preparedStatements.add(deleteSinglePayment = conn.prepareStatement(DELETE_SINGLE_PAYMENT));
            preparedStatements.add(insertIntoRooms = conn.prepareStatement(INSERT_INTO_ROOMS));
            preparedStatements.add(updateRoom = conn.prepareStatement(UPDATE_ROOM_WITH_ROOM_NUMBER));
            preparedStatements.add(deleteRoom = conn.prepareStatement(DELETE_ROOM_WITH_ROOM_NUMBER));
            preparedStatements.add(queryReservationsIdsWithRoomNumber = conn.prepareStatement(QUERY_RESERVATIONS_IDS_WITH_ROOM_NUMBER));
            preparedStatements.add(deleteReservationJointsWithRoomNumber = conn.prepareStatement(DELETE_RESERVATIONS_JOINT_WITH_ROOM_NUMBER));
            preparedStatements.add(deleteReservationWithId = conn.prepareStatement(DELETE_FROM_RESERVATIONS_WITH_ID));
            preparedStatements.add(insertIntoCustomers = conn.prepareStatement(INSERT_INTO_CUSTOMERS));
            preparedStatements.add(deleteCustomerWithId = conn.prepareStatement(DELETE_FROM_CUSTOMERS));
            preparedStatements.add(queryViewReservationsWithCustomerIdAndStatus = conn.prepareStatement(QUERY_VIEW_RESERVATIONS_WITH_CUSTOMER_ID_AND_STATUS));
            preparedStatements.add(deleteReservationsWithReservationId = conn.prepareStatement(DELETE_FROM_RESERVATIONS_WITH_RESERVATION_ID));
            preparedStatements.add(deleteReservationJointWithCustomer = conn.prepareStatement(DELETE_FROM_RESERVATIONS_JOINT_WITH_CUSTOMER));
            preparedStatements.add(updateCustomer = conn.prepareStatement(UPDATE_CUSTOMER));
            preparedStatements.add(queryViewReservationIdsWithCustomerId = conn.prepareStatement(QUERY_VIEW_RESERVATION_IDS_WITH_CUSTOMER_ID));
            preparedStatements.add(queryMaxReservationId = conn.prepareStatement(QUERY_MAX_RESERVATION_ID));
            preparedStatements.add(insertIntoAccounts = conn.prepareStatement(INSERT_INTO_ACCOUNTS));
            preparedStatements.add(deleteAccountWithReservationId = conn.prepareStatement(DELETE_ACCOUNT_WITH_RESERVATION_ID));
            preparedStatements.add(queryUsers = conn.prepareStatement(QUERY_USERS));
            preparedStatements.add(updateUser = conn.prepareStatement(UPDATE_USER));
            preparedStatements.add(updateUserPassword = conn.prepareStatement(UPDATE_USER_PASSWORD));
            preparedStatements.add(insertIntoUsers = conn.prepareStatement(INSERT_INTO_USERS));
            preparedStatements.add(deleteFromUsers = conn.prepareStatement(DELETE_FROM_USERS));
            preparedStatements.add(queryUserLoginInfoWithUsername = conn.prepareStatement(QUERY_USER_LOGIN_INFO_WITH_USERNAME));
            preparedStatements.add(queryUsersWithUsername = conn.prepareStatement(QUERY_USERS_WITH_USERNAME));



            return true;
        } catch (SQLException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            for (PreparedStatement ps: preparedStatements) {
                ps.close();
            }

            if(conn != null) {
                conn.close();
            }

        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }
}

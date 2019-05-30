package alancerpro.model.data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Queries {
    static final String DB_NAME = "hotel_man.db";
    static final String CONNECTION_STRING = "jdbc:sqlite:/home/anrchst/Documents/Programming/Java/HotelManagement/" + DB_NAME;

    static final String TABLE_USERS = "users";
    static final String COLUMN_USER_ID = "_id";
    static final String COLUMN_USER_NAME = "name";
    static final String COLUMN_USER_USERNAME = "username";
    static final String COLUMN_USER_PASSWORD = "password";
    static final String COLUMN_USER_USER_TYPE = "user_type";
    static final String COLUMN_USER_EMAIL = "email";
    static final String COLUMN_USER_ADDRESS = "address";
    static final String COLUMN_USER_PHONE_NUMBER = "phone_number";
    static final String COLUMN_USER_SALT = "salt";
    static final int COLUMN_USER_ID_INDEX = 1;
    static final int COLUMN_USER_NAME_INDEX = 2;
    static final int COLUMN_USER_USERNAME_INDEX = 3;
    static final int COLUMN_USER_PASSWORD_INDEX = 4;
    static final int COLUMN_USER_USER_TYPE_INDEX = 5;
    static final int COLUMN_USER_EMAIL_INDEX = 6;
    static final int COLUMN_USER_ADDRESS_INDEX = 7;
    static final int COLUMN_USER_PHONE_NUMBER_INDEX = 8;
    static final int COLUMN_USER_SALT_INDEX = 9;

    static final String TABLE_RESERVATIONS = "reservations";
    static final String COLUMN_RESERVATION_ID = "_id";
    static final String COLUMN_RESERVATION_RESERVATION_ID = "reservation_id";
    static final String COLUMN_RESERVATION_START_DATE = "start_date";
    static final String COLUMN_RESERVATION_END_DATE = "end_date";
    static final String COLUMN_RESERVATION_STATUS = "reservation_status";
    static final String COLUMN_RESERVATION_CREATION_DATE = "creation_date";
    static final String COLUMN_RESERVATION_ADULTS = "adults";
    static final String COLUMN_RESERVATION_CHILDS = "childs";
    static final int COLUMN_RESERVATION_ID_INDEX = 1;
    static final int COLUMN_RESERVATION_RESERVATION_ID_INDEX = 2;


    static final String TABLE_CUSTOMERS = "customers";
    static final String COLUMN_CUSTOMER_ID = "_id";
    static final String COLUMN_CUSTOMER_NAME = "name";
    static final String COLUMN_CUSTOMER_PHONE_NUMBER = "phone_number";
    static final String COLUMN_CUSTOMER_EMAIL = "email";
    static final String COLUMN_CUSTOMER_ADDRESS = "address";
    static final int COLUMN_CUSTOMER_ID_INDEX = 1;
    static final int COLUMN_CUSTOMER_NAME_INDEX = 2;
    static final int COLUMN_CUSTOMER_PHONE_NUMBER_INDEX = 3;
    static final int COLUMN_CUSTOMER_EMAIL_INDEX = 4;
    static final int COLUMN_CUSTOMER_ADDRESS_INDEX = 5;

    static final String TABLE_ROOMS = "rooms";
    static final String COLUMN_ROOM_NUMBER = "number";
    static final String COLUMN_ROOM_TYPE = "type";
    static final int COLUMN_ROOM_NUMBER_INDEX = 1;
    static final int COLUMN_ROOM_TYPE_INDEX = 2;

    static final String TABLE_RESERVATIONS_JOINT = "reservations_joint";
    static final String COLUMN_JOINT_ID = "_id";
    static final String COLUMN_JOINT_USERNAME = "username";
    static final String COLUMN_JOINT_ROOM_NUMBER = "room_number";
    static final String COLUMN_JOINT_CUSTOMER_ID = "customer_id";

    static final String TABLE_ACCOUNTS = "accounts";
    static final String COLUMN_ACCOUNTS_RESERVATION_ID = "reservation_id";
    static final String COLUMN_ACCOUNTS_DEBT = "debt";
    static final int COLUMN_ACCOUNTS_RESERVATION_ID_INDEX = 1;
    static final int COLUMN_ACCOUNTS_DEBT_INDEX = 2;

    static final String TABLE_PAYMENTS = "payments";
    static final String COLUMN_PAYMENTS_ID = "_id";
    static final String COLUMN_PAYMENTS_RESERVATION_ID = "reservation_id";
    static final String COLUMN_PAYMENTS_AMOUNT = "amount";
    static final String COLUMN_PAYMENTS_TYPE = "type";
    static final int COLUMN_PAYMENTS_ID_INDEX = 1;
    static final int COLUMN_PAYMENTS_RESERVATION_ID_INDEX = 2;
    static final int COLUMN_PAYMENTS_AMOUNT_INDEX = 3;
    static final int COLUMN_PAYMENTS_TYPE_INDEX = 4;


    static final String VIEW_RESERVATIONS_PAGE = "view_reservations_page";
    static final String COLUMN_VIEW_RESERVATION_ID = "reservation_id";
    static final String COLUMN_VIEW_RESERVATION_ROOM_NUMBER = "room_number";
    static final String COLUMN_VIEW_RESERVATION_STATUS = "reservation_status";
    static final String COLUMN_VIEW_RESERVATION_CUSTOMER_ID = "customer_id";
    static final String COLUMN_VIEW_RESERVATION_ADULTS = "adults";
    static final String COLUMN_VIEW_RESERVATION_CHILDS = "childs";
    static final String COLUMN_VIEW_RESERVATION_START_DATE = "start_date";
    static final String COLUMN_VIEW_RESERVATION_END_DATE = "end_date";
    static final String COLUMN_VIEW_RESERVATION_CREATION_DATE = "creation_date";
    static final String COLUMN_VIEW_RESERVATION_CREATED_BY = "created_by";
    static final int COLUMN_VIEW_RESERVATION_ID_INDEX = 1;
    static final int COLUMN_VIEW_RESERVATION_ROOM_NUMBER_INDEX = 2;
    static final int COLUMN_VIEW_RESERVATION_STATUS_INDEX = 3;
    static final int COLUMN_VIEW_RESERVATION_CUSTOMER_ID_INDEX = 4;
    static final int COLUMN_VIEW_RESERVATION_ADULTS_INDEX = 5;
    static final int COLUMN_VIEW_RESERVATION_CHILDS_INDEX = 6;
    static final int COLUMN_VIEW_RESERVATION_START_INDEX = 7;
    static final int COLUMN_VIEW_RESERVATION_END_INDEX = 8;
    static final int COLUMN_VIEW_RESERVATION_CREATION_DATE_INDEX = 9;
    static final int COLUMN_VIEW_RESERVATION_CREATED_BY_INDEX = 10;


    static final String VIEW_PAYMENTS_WITH_ACCOUNTS = "view_payments_with_accounts";
    static final String COLUMN_VIEW_PAYMENT_ID = "payment_id";
    static final String COLUMN_VIEW_PAYMENT_RESERVATION_ID = "reservation_id";
    static final String COLUMN_VIEW_PAYMENT_AMOUNT = "payment_amount";
    static final String COLUMN_VIEW_PAYMENT_TYPE = "payment_type";
    static final String COLUMN_VIEW_PAYMENT_ACCOUNT_DEBT = "account_debt";
    static final int COLUMN_VIEW_PAYMENT_ID_INDEX = 1;
    static final int COLUMN_VIEW_PAYMENT_RESERVATION_ID_INDEX = 2;
    static final int COLUMN_VIEW_PAYMENT_AMOUNT_INDEX = 3;
    static final int COLUMN_VIEW_PAYMENT_TYPE_INDEX = 4;
    static final int COLUMN_VIEW_PAYMENT_ACCOUNT_DEBT_INDEX = 5;





    /********** Reservation statements **********/
    static final String QUERY_VIEW_RESERVATION_PAGE = "SELECT * FROM " + VIEW_RESERVATIONS_PAGE;

    static final String QUERY_RESERVATIONS_VIEW_WITH_ID = "SELECT * FROM " + VIEW_RESERVATIONS_PAGE + " WHERE " + COLUMN_VIEW_RESERVATION_ID + " = ?";

    static final String QUERY_RESERVATIONS_VIEW_WITH_STATUS = "SELECT * FROM " + VIEW_RESERVATIONS_PAGE + " WHERE " + COLUMN_RESERVATION_STATUS + " = ?";

    static final String QUERY_RESERVATIONS_TABLE_WITH_RESERVATION_ID = "SELECT * FROM " + TABLE_RESERVATIONS + " WHERE " + COLUMN_RESERVATION_RESERVATION_ID + " = ?";

    static final String QUERY_RESERVATIONS_IDS_WITH_ROOM_NUMBER = "SELECT " + COLUMN_RESERVATION_ID + " FROM " + TABLE_RESERVATIONS_JOINT + " WHERE " + COLUMN_JOINT_ROOM_NUMBER + " = ?";
    
    static final String QUERY_VIEW_RESERVATIONS_WITH_CUSTOMER_ID_AND_STATUS = "SELECT * FROM " + VIEW_RESERVATIONS_PAGE + " WHERE " + COLUMN_VIEW_RESERVATION_CUSTOMER_ID + " = ? AND " + COLUMN_VIEW_RESERVATION_STATUS + " = ?";
    
    static final String QUERY_VIEW_RESERVATION_IDS_WITH_CUSTOMER_ID = "SELECT DISTINCT " + COLUMN_VIEW_RESERVATION_ID + " FROM " + VIEW_RESERVATIONS_PAGE + " WHERE " + COLUMN_VIEW_RESERVATION_CUSTOMER_ID + " = ?";

    static final String QUERY_MAX_RESERVATION_ID = "SELECT MAX(" + COLUMN_RESERVATION_RESERVATION_ID + ") " + " FROM " + TABLE_RESERVATIONS;



    static final String UPDATE_RESERVATION_DATE = "UPDATE " + TABLE_RESERVATIONS + " SET " + COLUMN_RESERVATION_START_DATE + " = ?, " + COLUMN_RESERVATION_END_DATE + " = ? WHERE " + COLUMN_RESERVATION_RESERVATION_ID + " = ?";
    static final String UPDATE_RESERVATION = "UPDATE " + TABLE_RESERVATIONS + " SET " + COLUMN_RESERVATION_START_DATE + " = ?, " + COLUMN_RESERVATION_END_DATE + " = ?, " + COLUMN_RESERVATION_STATUS + " = ? WHERE " + COLUMN_RESERVATION_RESERVATION_ID + " = ?";

    static final String UPDATE_RESERVATION_STATUS = "UPDATE " + TABLE_RESERVATIONS + " SET " + COLUMN_RESERVATION_STATUS + " = ? WHERE " + COLUMN_RESERVATION_RESERVATION_ID + " = ?";

    static final String UPDATE_RESERVATION_CUSTOMER = "UPDATE " + TABLE_RESERVATIONS_JOINT + " SET " + COLUMN_JOINT_CUSTOMER_ID + " = ? WHERE " + COLUMN_JOINT_ID + " = ?";

    static final String UPDATE_RESERVATION_ROOM = "UPDATE " + TABLE_RESERVATIONS_JOINT + " SET " + COLUMN_JOINT_ROOM_NUMBER + " = ? WHERE " + COLUMN_JOINT_ID + " = ?";

    static final String UPDATE_RESERVATION_JOINT = "UPDATE " + TABLE_RESERVATIONS_JOINT + " SET "  + COLUMN_JOINT_ROOM_NUMBER + " = ?, " + COLUMN_JOINT_CUSTOMER_ID + " = ?, " + COLUMN_JOINT_USERNAME + " = ? WHERE " + COLUMN_JOINT_ID + " = ?";

    static final String INSERT_RESERVATION = "INSERT INTO " + TABLE_RESERVATIONS + " ("
            + COLUMN_RESERVATION_RESERVATION_ID + ", "
            + COLUMN_RESERVATION_START_DATE + ", "
            + COLUMN_RESERVATION_END_DATE + ", "
            + COLUMN_RESERVATION_STATUS + ", "
            + COLUMN_RESERVATION_CREATION_DATE + ", "
            + COLUMN_RESERVATION_ADULTS + ", "
            + COLUMN_RESERVATION_CHILDS +
            ") VALUES (?, ?, ?, ?, ?, ?, ?)";

    static final String INSERT_RESERVATIONS_JOINT = "INSERT INTO " + TABLE_RESERVATIONS_JOINT + " VALUES (?, ?, ?, ?)";

    static final String DELETE_RESERVATIONS_WITH_RESERVATION_ID = "DELETE FROM " + TABLE_RESERVATIONS + " WHERE " + COLUMN_RESERVATION_RESERVATION_ID + " = ?";

    static final String DELETE_RESERVATIONS_JOINT_WITH_ID = "DELETE FROM " + TABLE_RESERVATIONS_JOINT + " WHERE " + COLUMN_JOINT_ID + " = ?";

    static final String DELETE_RESERVATIONS_JOINT_WITH_ROOM_NUMBER = "DELETE FROM " + TABLE_RESERVATIONS_JOINT + " WHERE " + COLUMN_JOINT_ROOM_NUMBER + " = ?";

    static final String DELETE_FROM_RESERVATIONS_WITH_ID = "DELETE FROM " + TABLE_RESERVATIONS + " WHERE " + COLUMN_RESERVATION_ID + " = ?";

    static final String DELETE_FROM_RESERVATIONS_WITH_RESERVATION_ID = "DELETE FROM " + TABLE_RESERVATIONS + " WHERE " + COLUMN_RESERVATION_RESERVATION_ID + " = ?";

    static final String DELETE_FROM_RESERVATIONS_JOINT_WITH_CUSTOMER = "DELETE FROM " + TABLE_RESERVATIONS_JOINT + " WHERE " + COLUMN_JOINT_CUSTOMER_ID + " = ?";



    /************* Room statements ****************/
    static final String QUERY_ROOM_WITH_ID = "SELECT * FROM " + TABLE_ROOMS + " WHERE " + COLUMN_ROOM_NUMBER + " = ?";
    static final String QUERY_ROOMS = "SELECT * FROM " + TABLE_ROOMS + " ORDER BY " + COLUMN_ROOM_NUMBER;
    static final String INSERT_INTO_ROOMS = "INSERT INTO " + TABLE_ROOMS + " VALUES(?, ?)";
    static final String UPDATE_ROOM_WITH_ROOM_NUMBER = "UPDATE " + TABLE_ROOMS + " SET " + COLUMN_ROOM_TYPE + " = ? " + " WHERE " + COLUMN_ROOM_NUMBER + " = ?";
    static final String DELETE_ROOM_WITH_ROOM_NUMBER = "DELETE FROM " + TABLE_ROOMS + " WHERE " + COLUMN_ROOM_NUMBER + " = ?";



    /************* Customer statements ****************/
    static final String QUERY_CUSTOMER_WITH_ID = "SELECT * FROM " + TABLE_CUSTOMERS + " WHERE " + COLUMN_CUSTOMER_ID + " = ?";
    static final String QUERY_CUSTOMERS = "SELECT * FROM " + TABLE_CUSTOMERS;
    static final String INSERT_INTO_CUSTOMERS = "INSERT INTO " + TABLE_CUSTOMERS + " VALUES (?,?,?,?,?)";
    static final String DELETE_FROM_CUSTOMERS = "DELETE FROM " + TABLE_CUSTOMERS + " WHERE " + COLUMN_CUSTOMER_ID + " = ?";
    static final String UPDATE_CUSTOMER = "UPDATE " + TABLE_CUSTOMERS + " SET " +
            COLUMN_CUSTOMER_ID + " = ?, " +
            COLUMN_CUSTOMER_NAME + " = ?, " +
            COLUMN_CUSTOMER_PHONE_NUMBER + " = ?, " +
            COLUMN_CUSTOMER_EMAIL + " = ?, " +
            COLUMN_CUSTOMER_ADDRESS + " = ? " +
            " WHERE " + COLUMN_CUSTOMER_ID + " = ? ";


    /********** Account Statements ***********/
    static final String QUERY_ACCOUNTS = "SELECT * FROM " + TABLE_ACCOUNTS;
    static final String QUERY_ACCOUNTS_WITH_RESERVATION_ID = "SELECT * FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_ACCOUNTS_RESERVATION_ID + " = ?";

    static final String UPDATE_ACCOUNT_DEBT_WITH_RESERVATION_ID = "UPDATE " + TABLE_ACCOUNTS + " SET " + COLUMN_ACCOUNTS_DEBT + " = ? WHERE " + COLUMN_ACCOUNTS_RESERVATION_ID + " = ?";

    static final String INSERT_INTO_ACCOUNTS = "INSERT INTO " + TABLE_ACCOUNTS + " VALUES(?,?)";

    static final String DELETE_ACCOUNT_WITH_RESERVATION_ID = "DELETE FROM " + TABLE_ACCOUNTS + " WHERE " + COLUMN_ACCOUNTS_RESERVATION_ID + " = ?";


    /********** Payment Statements ***********/
    static final String QUERY_PAYMENTS_WITH_RESERVATION_ID = "SELECT * FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_PAYMENTS_RESERVATION_ID + " = ? ";
    static final String QUERY_VIEW_PAYMENTS = "SELECT * FROM " + VIEW_PAYMENTS_WITH_ACCOUNTS + " ORDER BY " + COLUMN_VIEW_PAYMENT_RESERVATION_ID;
    static final String QUERY_VIEW_PAYMENTS_WITH_RES_ID = "SELECT * FROM " + VIEW_PAYMENTS_WITH_ACCOUNTS + " WHERE " + COLUMN_VIEW_PAYMENT_RESERVATION_ID + " = ?";

    static final String INSERT_INTO_PAYMENTS = "INSERT INTO " +
            TABLE_PAYMENTS + "(" +
            COLUMN_PAYMENTS_RESERVATION_ID + ", " +
            COLUMN_PAYMENTS_AMOUNT + ", " +
            COLUMN_PAYMENTS_TYPE + ") VALUES(?,?,?)";

    static final String DELETE_FROM_PAYMENTS_WITH_RESERVATION_ID = "DELETE FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_PAYMENTS_RESERVATION_ID + " = ?";

    static final String DELETE_SINGLE_PAYMENT = "DELETE FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_PAYMENTS_ID + " = (SELECT MIN(" + COLUMN_PAYMENTS_ID + ") FROM " + TABLE_PAYMENTS + " WHERE " + COLUMN_PAYMENTS_RESERVATION_ID + " = ? AND " + COLUMN_PAYMENTS_AMOUNT + " = ?)";




    /************ User Statements *************/
    static final String QUERY_USERS = "SELECT * FROM " + TABLE_USERS;
    static final String QUERY_USER_LOGIN_INFO_WITH_USERNAME = "SELECT " + COLUMN_USER_USERNAME + ", " + COLUMN_USER_PASSWORD + ", " + COLUMN_USER_SALT + " FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_USERNAME + " = ?";

    static final String QUERY_USERS_WITH_USERNAME = "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_USERNAME + " = ?";

    static final String UPDATE_USER = "UPDATE " + TABLE_USERS + " SET " +
            COLUMN_USER_ID + " = ?, " +
            COLUMN_USER_NAME + " = ?, " +
            COLUMN_USER_USERNAME + " = ?, " +
            COLUMN_USER_USER_TYPE + " = ?, " +
            COLUMN_USER_EMAIL + " = ?, " +
            COLUMN_USER_PHONE_NUMBER + " = ?, " +
            COLUMN_USER_ADDRESS + " = ? " +
            "WHERE " + COLUMN_USER_USERNAME  + " = ?";

    static final String UPDATE_USER_PASSWORD = "UPDATE " + TABLE_USERS + " SET " +
            COLUMN_USER_PASSWORD + " = ?, " +
            COLUMN_USER_SALT + " = ? " +
            "WHERE " + COLUMN_USER_USERNAME + " = ?";

    static final String INSERT_INTO_USERS = "INSERT INTO " + TABLE_USERS + " VALUES(?,?,?,?,?,?,?,?,?)";

    static final String DELETE_FROM_USERS = "DELETE FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_USERNAME + " = ?";


    static Connection conn;

    static PreparedStatement insertIntoReservations;
    static PreparedStatement insertIntoReservationsJoint;
    static PreparedStatement insertIntoPayments;
    static PreparedStatement insertIntoRooms;
    static PreparedStatement insertIntoCustomers;
    static PreparedStatement insertIntoAccounts;
    static PreparedStatement insertIntoUsers;

    static PreparedStatement updateReservation;
    static PreparedStatement updateReservationStatus;
    static PreparedStatement updateReservationCustomer;
    static PreparedStatement updateReservationRoom;
    static PreparedStatement updateReservationJoint;
    static PreparedStatement updateAccountDebt;
    static PreparedStatement updateRoom;
    static PreparedStatement updateCustomer;
    static PreparedStatement updateUser;
    static PreparedStatement updateUserPassword;

    static PreparedStatement deleteReservation;
    static PreparedStatement deleteReservationJoint;
    static PreparedStatement deleteReservationJointWithCustomer;
    static PreparedStatement deletePaymentWithReservationId;
    static PreparedStatement deleteSinglePayment;
    static PreparedStatement deleteRoom;
    static PreparedStatement deleteReservationJointsWithRoomNumber;
    static PreparedStatement deleteReservationWithId;
    static PreparedStatement deleteReservationsWithReservationId;
    static PreparedStatement deleteCustomerWithId;
    static PreparedStatement deleteAccountWithReservationId;
    static PreparedStatement deleteFromUsers;

    static PreparedStatement queryRoomWithRoomNumber;
    static PreparedStatement queryCustomerWithId;
    static PreparedStatement queryCustomers;
    static PreparedStatement queryViewReservationsWithCustomerIdAndStatus;
    static PreparedStatement queryViewReservationIdsWithCustomerId;
    static PreparedStatement queryRooms;
    static PreparedStatement queryViewReservationsPage;
    static PreparedStatement queryViewReservationsWithReservationId;
    static PreparedStatement queryViewReservationsWithStatus;
    static PreparedStatement queryTableReservationsWithReservationId;
    static PreparedStatement queryViewPaymentsWithReservationId;
    static PreparedStatement queryViewPayments;
    static PreparedStatement queryAccounts;
    static PreparedStatement queryReservationsIdsWithRoomNumber;
    static PreparedStatement queryMaxReservationId;
    static PreparedStatement queryUsers;
    static PreparedStatement queryUserLoginInfoWithUsername;
    static PreparedStatement queryUsersWithUsername;


    static List<PreparedStatement> preparedStatements = new ArrayList<>();

}

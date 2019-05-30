package alancerpro.model.data;

import alancerpro.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ReservationData extends Queries {


    private ReservationData() {
    }
    public static ReservationData getInstance() {
        return instance;
    }
    private static ReservationData instance = new ReservationData();


    public static ObservableMap<Long, ObservableList<Reservation>> reservationsMap = FXCollections.observableHashMap();

    public static ObservableList<Reservation> reservationsAsDistinctList = FXCollections.observableArrayList();

    public static ObservableList<Reservation> reservationsAsList = FXCollections.observableArrayList();

    public static void setLists() {
        reservationsAsDistinctList.setAll();
        reservationsAsList.setAll();
        for (ObservableList<Reservation> reservations: ReservationData.reservationsMap.values()) {
            reservationsAsDistinctList.add(reservations.get(0));
            reservationsAsList.addAll(reservations);
        }
    }


    /********** Query *********/

    ////////////View Queries
    public Map<Long, ObservableList<Reservation>> queryViewReservations() {
        try {
            return runReservationsViewQuery(queryViewReservationsPage);
        } catch (SQLException e) {
            System.out.println("Error querying reservations: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Reservation> queryViewReservationWithReservationId(long reservationId) {
        try {
            queryViewReservationsWithReservationId.setLong(1, reservationId);
            return runReservationsViewQuery(queryViewReservationsWithReservationId).get(reservationId);
        } catch (SQLException e) {
            System.out.println("Error querying reservation with id: " + e.getMessage());
            System.out.println(e.getSQLState());
            e.printStackTrace();
            return null;
        }
    }

    public List<Reservation> queryReservationsViewWithCustomerAndStatus(String customerId, ReservationStatus status) {
        try {
            queryViewReservationsWithCustomerIdAndStatus.setString(1, customerId);
            queryViewReservationsWithCustomerIdAndStatus.setString(2, status.toString());
            Map<Long, ObservableList<Reservation>> resultMap = runReservationsViewQuery(queryViewReservationsWithCustomerIdAndStatus);
            List<Reservation> reservationsList = new ArrayList<>();
            for (List<Reservation> reservations: resultMap.values()) {
                reservationsList.addAll(reservations);
            }
            return reservationsList;
        } catch (SQLException e) {
            System.out.println("Error querying reservations: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public List<Long> queryReservationIdsWithCustomer(String customerId) {
        try {
            List<Long> resIds = new ArrayList<>();
            queryViewReservationIdsWithCustomerId.setString(1, customerId);
            ResultSet resultSet = queryViewReservationIdsWithCustomerId.executeQuery();
            while(resultSet.next()) {
                resIds.add(resultSet.getLong(COLUMN_VIEW_RESERVATION_ID_INDEX));
            }
            return resIds;
        } catch (SQLException e) {
            System.out.println("Error querying reservations: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public long generateNewReservationId() {
        try {
            ResultSet resultSet = queryMaxReservationId.executeQuery();
            return resultSet.getLong(1) + 1;
        } catch (SQLException e) {
            System.out.println("Error querying max reservation id: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }


    public Map<Long, ObservableList<Reservation>> queryViewReservationsWithStatus(ReservationStatus status) {
        try {
            queryViewReservationsWithStatus.setString(1, status.toString());
            return runReservationsViewQuery(queryViewReservationsWithStatus);
        } catch (SQLException e) {
            System.out.println("Error querying reservations with status: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    private Map<Long, ObservableList<Reservation>> runReservationsViewQuery(PreparedStatement statement) throws SQLException {
        Map<Long, ObservableList<Reservation>> reservationsMap = new HashMap<>();

        ResultSet results = statement.executeQuery();
        while(results.next()) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.out.println("Interrupted: " + e.getMessage());
                e.printStackTrace();
            }

            long resId = results.getLong(COLUMN_VIEW_RESERVATION_ID_INDEX);
            int resRoomNumber = results.getInt(COLUMN_VIEW_RESERVATION_ROOM_NUMBER_INDEX);
            String resStatus = results.getString(COLUMN_VIEW_RESERVATION_STATUS_INDEX);
            String resCustomerId = results.getString(COLUMN_VIEW_RESERVATION_CUSTOMER_ID_INDEX);
            int resAdults = results.getInt(COLUMN_VIEW_RESERVATION_ADULTS_INDEX);
            int resChilds = results.getInt(COLUMN_VIEW_RESERVATION_CHILDS_INDEX);
            String resStartDate = results.getString(COLUMN_VIEW_RESERVATION_START_INDEX);
            String resEndDate = results.getString(COLUMN_VIEW_RESERVATION_END_INDEX);
            String resCreateDate = results.getString(COLUMN_VIEW_RESERVATION_CREATION_DATE_INDEX);
            String resCreatedBy = results.getString(COLUMN_VIEW_RESERVATION_CREATED_BY_INDEX);

            Customer customer = CustomerData.getInstance().queryCustomerWithId(resCustomerId);
            Room room = RoomData.getInstance().queryRoomWithNumber(resRoomNumber);
            Reservation reservation = new Reservation(resId, room, customer,resAdults, resChilds, resStartDate, resEndDate, ReservationStatus.valueOf(resStatus), resCreateDate, resCreatedBy);
            customer.addReservation(reservation);

            if(!reservationsMap.containsKey(resId)) {
                reservationsMap.put(resId, FXCollections.observableArrayList());
            }

            reservationsMap.get(resId).add(reservation);
        }
        return reservationsMap;
    }




    ////////////Reservations Table Queries
    public List<Long> queryTableReservationsGetIds(long id) {
      try {
          List<Long> idList = new ArrayList<>();
          queryTableReservationsWithReservationId.setLong(1, id);
          ResultSet resultSet = queryTableReservationsWithReservationId.executeQuery();
          while(resultSet.next()) {
              idList.add(resultSet.getLong(COLUMN_RESERVATION_ID_INDEX));
          }
          return idList;
      } catch (SQLException e) {
          System.out.println("Error querying reservation table ids: " + e.getMessage());
          e.printStackTrace();
          return null;
      }
    }



    ////////ReservationsJointQueries
    public List<Long> queryReservationsIdsWithRoomNumber(long roomNumber) {
        try {
            List<Long> uniqueIds = new ArrayList<>();
            queryReservationsIdsWithRoomNumber.setLong(1, roomNumber);
            ResultSet resultSet = queryReservationsIdsWithRoomNumber.executeQuery();
            while(resultSet.next()) {
                uniqueIds.add(resultSet.getLong(COLUMN_RESERVATION_ID_INDEX));
            }
            return uniqueIds;
        } catch (SQLException e) {
            System.out.println("Error querying reservation ids: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }






    /*********** Insert ***********/

    public boolean insertReservationList(List<Reservation> reservations, long resId) {
        try {
            conn.setAutoCommit(false);
            deleteReservationWithReservationId(resId);

            if(reservations == null) {
                return true;
            }

            for (Reservation r: reservations) {
                ResultSet resultSet = insertIntoReservations(r);
                insertIntoReservationsJoint(r, resultSet.getLong(COLUMN_RESERVATION_ID_INDEX));
            }
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting reservation: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Oh boy, be careful." + e2.getMessage());
                e.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Can not set autocommit to true: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private ResultSet insertIntoReservations(Reservation res) throws SQLException {
        insertIntoReservations.setLong(1, res.getReservationID());
        insertIntoReservations.setString(2, res.getStartDate());
        insertIntoReservations.setString(3, res.getEndDate());
        insertIntoReservations.setString(4, res.getReservationStatus().toString());
        insertIntoReservations.setString(5, res.getCreateDate());
        insertIntoReservations.setInt(6, res.getAdults());
        insertIntoReservations.setInt(7, res.getChilds());
        insertIntoReservations.execute();
        return insertIntoReservations.getGeneratedKeys();
    }

    private void insertIntoReservationsJoint(Reservation res, long id) throws SQLException {
        insertIntoReservationsJoint.setLong(1, id);
        insertIntoReservationsJoint.setString(2, res.getCustomer().getId());
        insertIntoReservationsJoint.setInt(3, res.getRoom().getRoomNumber());
        insertIntoReservationsJoint.setString(4, res.getCreatedBy());
        insertIntoReservationsJoint.execute();
    }





    /*********** Delete ***********/

    public boolean deleteReservation(long reservationId) {
        try {
            conn.setAutoCommit(false);
            deleteReservationWithReservationId(reservationId);
            AccountData.getInstance().deleteFromAccounts(reservationId);
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting reservation: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Can't rollback. We got into trouble" + e2.getMessage());
                e2.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Can't autocommit. Things are going crazy." + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private void deleteReservationWithReservationId(long reservation_id) throws SQLException {
        List<Long> idsToBeDeleted = queryTableReservationsGetIds(reservation_id);
        deleteFromReservations(reservation_id);
        //Delete them from reservations_joint using their _id indexes
        for (Long id: idsToBeDeleted) {
            deleteFromReservationsJoint(id);
        }
    }


    void deleteFromReservations(long reservation_id) throws SQLException {
        deleteReservation.setLong(1, reservation_id);
        deleteReservation.execute();
    }

    void deleteFromReservationsWithId(long _id) throws SQLException {
        deleteReservationWithId.setLong(1, _id);
        deleteReservationWithId.execute();
    }

    void deleteFromReservationsWithReservationId(long reservationId) throws SQLException {
        deleteReservationsWithReservationId.setLong(1, reservationId);
        deleteReservationsWithReservationId.execute();
    }

    void deleteFromReservationsJoint(long id) throws SQLException {
        deleteReservationJoint.setLong(1, id);
        deleteReservationJoint.execute();
    }

    void deleteFromReservationsJointWithCustomer(String id) throws SQLException {
        deleteReservationJointWithCustomer.setString(1, id);
        deleteReservationJointWithCustomer.execute();
    }

    void deleteReservationJointsWithRoomNumber(long roomNumber) throws SQLException {
        deleteReservationJointsWithRoomNumber.setLong(1, roomNumber);
        deleteReservationJointsWithRoomNumber.execute();
    }





    /********** Update ***********/
    public boolean updateStatusOfReservation(long resId, ReservationStatus status) {
        try {
            updateReservationStatus.setString(1, status.toString());
            updateReservationStatus.setLong(2, resId);
            updateReservationStatus.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating reservation status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }



}


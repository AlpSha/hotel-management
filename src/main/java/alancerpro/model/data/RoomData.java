package alancerpro.model.data;

import alancerpro.model.Room;
import alancerpro.model.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomData extends Queries {
    private RoomData() {}
    public static RoomData getInstance() {
        return instance;
    }
    private static RoomData instance = new RoomData();

    public static ObservableList<Room> rooms = FXCollections.observableArrayList();


    public List<Room> queryRooms() {
        try {
           return runRoomQuery(queryRooms);
        } catch (SQLException e) {
            System.out.println("Error querying rooms: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Room queryRoomWithNumber(int roomNumber) {
        try {
            queryRoomWithRoomNumber.setInt(1, roomNumber);
            return runRoomQuery(queryRoomWithRoomNumber).get(0);
        } catch (SQLException e) {
            System.out.println("Error querying rooms with room number: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    private List<Room> runRoomQuery(PreparedStatement statement) throws SQLException {
        List<Room> rooms = new ArrayList<>();

        ResultSet results = statement.executeQuery();
        while(results.next()) {
            int roomId = results.getInt(COLUMN_ROOM_NUMBER_INDEX);
            String roomType = results.getString(COLUMN_ROOM_TYPE_INDEX);
            rooms.add(new Room(roomId, RoomType.valueOf(roomType)));
        }
        return rooms;
    }

    public boolean insertIntoRooms(Room room) {
        try {
            insertIntoRooms.setInt(1, room.getRoomNumber());
            insertIntoRooms.setString(2, room.getRoomType().toString());
            insertIntoRooms.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting into rooms: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRoom(Room room, RoomType type) {
        try {
            updateRoom.setString(1, type.toString());
            updateRoom.setInt(2, room.getRoomNumber());
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating room: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void deleteRoom(Room room) throws SQLException {
        deleteRoom.setInt(1, room.getRoomNumber());
        deleteRoom.execute();
    }


    public boolean deleteRoomWithReservations(Room room) {
        try {
            conn.setAutoCommit(false);
            List<Long> resIdsForRoom = ReservationData.getInstance().queryReservationsIdsWithRoomNumber(room.getRoomNumber());
            if(resIdsForRoom == null) {
                return false;
            }
            ReservationData.getInstance().deleteReservationJointsWithRoomNumber(room.getRoomNumber());
            for (Long id:resIdsForRoom) {
                ReservationData.getInstance().deleteFromReservationsWithId(id);
            }
            deleteRoom(room);
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error deleting room with reservations: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Oh man, this is not good. " + e2.getMessage());
                e.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Can't set autocommit. Things are going bad" + e.getMessage());
                e.printStackTrace();
            }
        }
    }


}

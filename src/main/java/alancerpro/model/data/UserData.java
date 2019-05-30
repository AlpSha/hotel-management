package alancerpro.model.data;

import alancerpro.model.User;
import alancerpro.model.UserType;
import alancerpro.util.PasswordUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserData extends Queries {
    private static UserData ourInstance = new UserData();

    public static UserData getInstance() {
        return ourInstance;
    }

    private UserData() {
    }


    public static ObservableList<User> users = FXCollections.observableArrayList();


    public List<User> queryUsers() {
        try {
            return runUserQueries(queryUsers);
        } catch (SQLException e) {
            System.out.println("Error querying users: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    private List<User> runUserQueries(PreparedStatement statement) throws SQLException {
        List<User> usersList = new ArrayList<>();
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            String name = resultSet.getString(COLUMN_USER_NAME_INDEX);
            String id = resultSet.getString(COLUMN_USER_ID_INDEX);
            String username = resultSet.getString(COLUMN_USER_USERNAME_INDEX);
            UserType userType = UserType.valueOf(resultSet.getString(COLUMN_USER_USER_TYPE_INDEX));
            String email = resultSet.getString(COLUMN_USER_EMAIL_INDEX);
            String address = resultSet.getString(COLUMN_USER_ADDRESS_INDEX);
            String phoneNumber = resultSet.getString(COLUMN_USER_PHONE_NUMBER_INDEX);
            usersList.add(new User(id, name, username, userType, email, address, phoneNumber));
        }
        return usersList;
    }

    public boolean updateUserAndPassword(String oldUserName, User newUser, String newPassword) {
        try {
            conn.setAutoCommit(false);
            runUpdateUserPassword(oldUserName, newPassword);
            runUpdateUser(oldUserName, newUser);
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e2) {
                System.out.println("Can't rollback to changes before. That is bad. " + e2.getMessage());
                e2.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("You are in trouble, mate. " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public boolean updateUser(String oldUsername, User newUser) {
        try {
            runUpdateUser(oldUsername, newUser);
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void runUpdateUser(String username, User user) throws SQLException {
        updateUser.setString(1, user.getId());
        updateUser.setString(2, user.getName());
        updateUser.setString(3, user.getUsername());
        updateUser.setString(4, user.getUserType().toString());
        updateUser.setString(5, user.getEmail());
        updateUser.setString(6, user.getPhoneNumber());
        updateUser.setString(7, user.getAddress());
        updateUser.setString(8, username);
        updateUser.execute();
    }

    private void runUpdateUserPassword(String username, String password) throws SQLException {
        String salt = PasswordUtils.getSalt(30);
        String securePassword = PasswordUtils.generateSecurePassword(password, salt);
        updateUserPassword.setString(1, securePassword);
        updateUserPassword.setString(2, salt);
        updateUserPassword.setString(3, username);
        updateUserPassword.execute();
    }

    public boolean insertIntoUsers(User user, String password) {
        try {
            String salt = PasswordUtils.getSalt(30);
            String securedPassword = PasswordUtils.generateSecurePassword(password, salt);

            insertIntoUsers.setString(1, user.getId());
            insertIntoUsers.setString(2, user.getName());
            insertIntoUsers.setString(3, user.getUsername());
            insertIntoUsers.setString(4, securedPassword);
            insertIntoUsers.setString(5, user.getUserType().toString());
            insertIntoUsers.setString(6, user.getEmail());
            insertIntoUsers.setString(7, user.getAddress());
            insertIntoUsers.setString(8, user.getPhoneNumber());
            insertIntoUsers.setString(9, salt);
            insertIntoUsers.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error inserting into users: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFromUsers(String username) {
        try {
            deleteFromUsers.setString(1, username);
            deleteFromUsers.execute();
            return true;
        } catch(SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public User checkLogin(String username, String password) {
      try {
          Map<String, String> loginMap =  queryUserLoginInfo(username);
          if(loginMap == null || loginMap.size() == 0) {
              return null;
          }
          String securedPassword = PasswordUtils.generateSecurePassword(password, loginMap.get("salt"));
          if(!securedPassword.equals(loginMap.get("password"))) {
              return null;
          }
          return queryUsersWithUsername(username);
      } catch (SQLException e) {
          System.out.println("Error checking login information: " + e.getMessage());
          e.printStackTrace();
          return null;
      }
    }

    private Map<String, String> queryUserLoginInfo(String username) throws SQLException {
        Map<String, String> resultMap = new HashMap<>();
        queryUserLoginInfoWithUsername.setString(1, username);
        ResultSet resultSet = queryUserLoginInfoWithUsername.executeQuery();
        if (resultSet.next()) {
            resultMap.put("username", resultSet.getString(1));
            resultMap.put("password", resultSet.getString(2));
            resultMap.put("salt", resultSet.getString(3));
        }
        return resultMap;
    }

    private User queryUsersWithUsername(String username) throws SQLException {
        queryUsersWithUsername.setString(1, username);
        List<User> resultList = runUserQueries(queryUsersWithUsername);
        if(resultList.size() != 0) {
            return resultList.get(0);
        }
        return null;
    }
}

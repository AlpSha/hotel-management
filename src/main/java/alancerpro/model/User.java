package alancerpro.model;

import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;

public class User extends Person<User> {
    public static User loggedInUser;
    private SimpleStringProperty username;
    private SimpleStringProperty userType;

    public User(String id, String name, String username, UserType userType, String email, String address, String phoneNumber) {
        super(id, name, email, address, phoneNumber);
        this.userType = new SimpleStringProperty(userType.toString());
        this.username = new SimpleStringProperty(username);
    }

    public User(User u) {
        this(u.getId(), u.getName(), u.getUsername(), u.getUserType(), u.getEmail(), u.getAddress(), u.getPhoneNumber());
    }

    public UserType getUserType() {
        return UserType.valueOf(userType.get());
    }

    public void setUserType(UserType userType) {
        this.userType.set(userType.toString());
    }

    public SimpleStringProperty userTypeProperty() {
        return userType;
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public void setAll(User user) {
        this.setUsername(user.getUsername());
        this.setId(user.getId());
        this.setUserType(user.getUserType());
        this.setName(user.getName());
        this.setEmail(user.getEmail());
        this.setAddress(user.getAddress());
        this.setPhoneNumber(user.getPhoneNumber());
    }
}

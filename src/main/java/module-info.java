module HotelManagementProject {
    requires javafx.fxml;
    requires javafx.controls;
    requires com.jfoenix;
    requires javafx.graphics;
    requires java.sql;

    exports alancerpro to javafx.graphics;

    opens alancerpro.controller;
}
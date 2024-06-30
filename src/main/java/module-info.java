module com.example.application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.application to javafx.fxml;
    exports com.example.application;
}
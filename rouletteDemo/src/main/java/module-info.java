module com.example.roulettedemo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.roulettedemo to javafx.fxml;
    exports com.example.roulettedemo;
}
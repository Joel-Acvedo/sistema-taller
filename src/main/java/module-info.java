module com.gesa {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.gesa to javafx.fxml;
    exports com.gesa;
}

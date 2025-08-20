module com.securesign.securesign {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.securesign.securesign to javafx.fxml;
    exports com.securesign.securesign;
}
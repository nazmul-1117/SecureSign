module com.securesign.securesign {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;


    opens com.securesign.securesign to javafx.fxml;
    exports com.securesign.securesign;

    exports com.securesign.controllers.mainController;
    opens com.securesign.controllers.mainController to javafx.fxml;

    exports com.securesign.controllers.containerController;
    opens com.securesign.controllers.containerController to javafx.fxml;

    // Home
    exports com.securesign.controllers.home;
    opens com.securesign.controllers.home to javafx.fxml;

    // RSA
    exports com.securesign.controllers.rsa_key;
    opens com.securesign.controllers.rsa_key to javafx.fxml;

    // DOC
    exports com.securesign.controllers.documents;
    opens com.securesign.controllers.documents to javafx.fxml;

    // SIGN
    exports com.securesign.controllers.signing;
    opens com.securesign.controllers.signing to javafx.fxml;

    // Ver
    exports com.securesign.controllers.verification;
    opens com.securesign.controllers.verification to javafx.fxml;

    // manual
    exports com.securesign.controllers.manual;
    opens com.securesign.controllers.manual to javafx.fxml;

    // about
    exports com.securesign.controllers.about;
    opens com.securesign.controllers.about to javafx.fxml;

//    exports com.securesign.controllers.home;
//    opens com.securesign.controllers.home to javafx.fxml;
}
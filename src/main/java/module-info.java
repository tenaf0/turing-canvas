module turingcanvas {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens turing to javafx.fxml;
    exports turing;
}
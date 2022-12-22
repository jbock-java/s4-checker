module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.parmigiano;

    opens uppu to javafx.fxml;
    exports uppu;
}

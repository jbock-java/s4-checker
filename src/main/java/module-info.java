module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    
    opens uppu to javafx.fxml;
    exports uppu;
}

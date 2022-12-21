package uppu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uppu.view.PermutationView;


public class S4Checker extends Application {

    @Override
    public void start(Stage stage) {
        PermutationView.create(stage).init();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

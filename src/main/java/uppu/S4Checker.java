package uppu;

import javafx.application.Application;
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

package uppu.test.game;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import uppu.view.PermutationView;

/**
 * JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8
 * actuallyRun=1
 */
public class GameTest extends Application {

    @Override
    public void start(Stage stage) {
        PermutationView view = PermutationView.create(stage);
        view.init();
        stage.show();
        new GamePresenter(view).run();
    }

    @Test
    void launch() {
        // Good?
        if (System.getenv("actuallyRun") == null) {
            return;
        }
        launch(new String[0]);
    }
}

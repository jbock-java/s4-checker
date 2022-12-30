package uppu.test;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import uppu.Presenter;
import uppu.model.ActionSequence;
import uppu.view.PermutationView;

import java.nio.file.Path;
import java.util.List;

public class PresenterTest extends Application {

    @Override
    public void start(Stage stage) {
        List<ActionSequence> actions = List.of();
        PermutationView view = PermutationView.create(stage);
        view.init();
        stage.show();
        new Presenter(view, Mockito.mock(Path.class), actions).run();
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

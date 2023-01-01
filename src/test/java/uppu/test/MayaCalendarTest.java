package uppu.test;

import io.parmigiano.Permutation;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.parmigiano.Permutation.cycle;
import static uppu.test.CalendarTest.klein;

public class MayaCalendarTest extends Application {

    @Override
    public void start(Stage stage) {
        List<List<Permutation>> permutations = new ArrayList<>();
        permutations.add(List.of(klein(2)));
        permutations.add(List.of(klein(3)));
        permutations.add(List.of(klein(2)));
        permutations.add(List.of(cycle(0, 1, 2), klein(3)));
        permutations.add(List.of(klein(2)));
        permutations.add(List.of(klein(3)));
        permutations.add(List.of(klein(2)));
        permutations.add(List.of(cycle(0, 1, 2), klein(3)));
        permutations.add(List.of(klein(2)));
        permutations.add(List.of(klein(3)));
        permutations.add(List.of(klein(2)));
        permutations.add(List.of(cycle(0, 1, 2), klein(3)));
        CalendarTest.run(stage, permutations);
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

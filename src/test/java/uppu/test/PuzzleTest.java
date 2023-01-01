package uppu.test;

import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static uppu.test.Month.AUGUST;
import static uppu.test.Month.FEBRUARY;
import static uppu.test.Month.JANUARY;
import static uppu.test.Month.NOVEMBER;
import static uppu.test.Month.OCTOBER;

public class PuzzleTest extends Application {

    @Override
    public void start(Stage stage) {
        List<List<Month>> permutations = new ArrayList<>();

        permutations.add(List.of(JANUARY));
        permutations.add(List.of(FEBRUARY));
        permutations.add(List.of(NOVEMBER, AUGUST, NOVEMBER, OCTOBER));
        permutations.add(List.of(FEBRUARY));
        CalendarTest.run(stage, permutations.stream()
                .map(months -> months.stream().map(Month::permutation).toList())
                .toList());
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

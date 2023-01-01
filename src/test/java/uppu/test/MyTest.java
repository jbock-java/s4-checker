package uppu.test;

import io.parmigiano.Permutation;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.parmigiano.Permutation.cycle;

public class MyTest extends Application {

    @Override
    public void start(Stage stage) {
        List<List<Permutation>> permutations = new ArrayList<>();
        Permutation p = cycle(0, 1, 2);
        Permutation q = cycle(0, 1, 3);

        List<Permutation> january = List.of(p);
        List<Permutation> april = List.of(p, q, p);
        List<Permutation> october = List.of(p, q, p, p, q);
        List<Permutation> july = List.of(p, p, q);

        permutations.add(january);
        permutations.add(january);
        permutations.add(april);
        permutations.add(october);
        permutations.add(october);
        permutations.add(january);
        permutations.add(april);
        permutations.add(april);
        permutations.add(january);
        permutations.add(july);
        permutations.add(july);
        permutations.add(april);
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

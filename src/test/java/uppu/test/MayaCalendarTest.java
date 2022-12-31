package uppu.test;

import io.parmigiano.Permutation;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import uppu.Presenter;
import uppu.model.ActionSequence;
import uppu.model.CommandSequence;
import uppu.model.State;
import uppu.parse.Row;
import uppu.view.PermutationView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static io.parmigiano.Permutation.cycle;
import static uppu.test.CalendarTest.Month.monthOf;
import static uppu.test.CalendarTest.klein;

public class MayaCalendarTest extends Application {
    
    @Override
    public void start(Stage stage) {
        Permutation current = Permutation.identity();
        List<List<Permutation>> permutations = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
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
        }
        List<CommandSequence> result = new ArrayList<>();
        for (List<Permutation> p : permutations) {
            CommandSequence.Result r = CommandSequence.toSequence(new Row.ExplicitRow(p), current);
            result.add(r.sequence().title(monthOf(current).title));
            current = r.permutation().compose(current);
        }
        PermutationView view = PermutationView.create(stage);
        view.init();
        stage.show();
        Consumer<List<ActionSequence>> onSave = MyTest::onSave;
        new Presenter(view, onSave, State.create().getActions(result)).run();
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

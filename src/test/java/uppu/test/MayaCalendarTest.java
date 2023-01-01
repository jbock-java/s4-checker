package uppu.test;

import io.parmigiano.Permutation;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import uppu.Presenter;
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
        List<CommandSequence> result = State.create().getCommands(permutations.stream().map(Row::explicitRow).toList()).stream()
                .map(r -> r.sequence().title(monthOf(r.permutation()).title())).toList();
        PermutationView view = PermutationView.create(stage);
        view.init();
        stage.show();
        Consumer<List<CommandSequence>> onSave = MyTest::onSave;
        Presenter.create(view, onSave, result).run();
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

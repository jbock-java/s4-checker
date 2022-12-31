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
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class PresenterTest extends Application {

    @Override
    public void start(Stage stage) {
        Permutation current = Permutation.identity();
        List<Permutation> permutations1 = new ArrayList<>(Permutation.symmetricGroup(4));
        List<Permutation> permutations2 = new ArrayList<>(Permutation.symmetricGroup(4));
        Collections.shuffle(permutations1);
        Collections.shuffle(permutations2);
        List<CommandSequence> result = new ArrayList<>(2 * permutations1.size() * permutations1.size());
        for (Permutation p : permutations1) {
            if (p.isIdentity()) {
                continue;
            }
            for (Permutation q : permutations2) {
                if (q.isIdentity()) {
                    continue;
                }
                if (q.equals(p)) {
                    continue;
                }
                CommandSequence.Result r = CommandSequence.toSequence(new Row.ExplicitRow(List.of(p, q)), current);
                result.add(r.sequence());
                current = r.permutation().compose(current);
                if (!current.isIdentity()) {
                    r = CommandSequence.toSequence(Row.HOME_ROW, current);
                    result.add(r.sequence());
                    current = r.permutation().compose(current);
                }
            }
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

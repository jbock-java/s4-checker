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
import uppu.util.Suppliers;
import uppu.view.PermutationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.parmigiano.Permutation.cycle;

public class CalendarTest extends Application {

    enum Month {

        JANUARY(Permutation.identity()),
        FEBRUARY(cycle(0, 1, 2)),
        MARCH(cycle(0, 2, 1)),

        APRIL(cycle(0, 2).compose(1, 3)),
        MAY(cycle(0, 3, 1)),
        JUNE(cycle(1, 2, 3)),

        JULY(cycle(0, 1).compose(2, 3)),
        AUGUST(cycle(1, 3, 2)),
        SEPTEMBER(cycle(0, 3, 2)),

        OCTOBER(cycle(0, 3).compose(1, 2)),
        NOVEMBER(cycle(0, 2, 3)),
        DECEMBER(cycle(0, 1, 3)),
        ;

        final Permutation p;

        static final Supplier<Map<Permutation, Month>> MAP = Suppliers.memoize(() -> {
            Map<Permutation, Month> monthMap = new HashMap<>();
            for (Month month : Month.values()) {
                monthMap.put(month.p, month);
            }
            return monthMap;
        });

        static Month get(Permutation p) {
            return MAP.get().get(p);
        }

        Month(Permutation p) {
            this.p = p;
        }
    }

    @Override
    public void start(Stage stage) {
        Permutation current = Permutation.identity();
        List<List<Permutation>> permutations = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            permutations.add(List.of(cycle(0, 1, 2)));
            permutations.add(List.of(cycle(0, 1, 2)));
            permutations.add(List.of(cycle(3, 1).compose(0, 2), cycle(0, 1, 2)));
            permutations.add(List.of(cycle(0, 2, 3)));
            permutations.add(List.of(cycle(0, 2, 3)));
            permutations.add(List.of(cycle(0, 3).compose(1, 2), cycle(0, 2, 3)));
            permutations.add(List.of(cycle(0, 3, 1)));
            permutations.add(List.of(cycle(0, 3, 1)));
            permutations.add(List.of(cycle(0, 2).compose(3, 1), cycle(0, 3, 1)));
            permutations.add(List.of(cycle(1, 3, 2)));
            permutations.add(List.of(cycle(1, 3, 2)));
            permutations.add(List.of(cycle(3, 0).compose(1, 2), cycle(1, 3, 2)));
        }
        List<CommandSequence> result = new ArrayList<>();
        for (List<Permutation> p : permutations) {
            System.out.println(Month.get(current));
            CommandSequence.Result r = CommandSequence.toSequence(new Row.ExplicitRow(p), current);
            result.add(r.sequence());
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

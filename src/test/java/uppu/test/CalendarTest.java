package uppu.test;

import io.parmigiano.Permutation;
import javafx.application.Application;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import uppu.Presenter;
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
import static java.util.Objects.requireNonNull;
import static uppu.test.CalendarTest.Month.monthOf;

public class CalendarTest extends Application {

    enum Month {
        JANUARY(cycle(0, 1, 2), "01 Январь"),
        FEBRUARY(cycle(0, 2, 1), "02 Февраль"),
        MARCH(cycle(0, 2).compose(1, 3), "03 Март"),

        APRIL(cycle(0, 3, 1), "04 Апрель"),
        MAY(cycle(1, 2, 3), "05 Май"),
        JUNE(cycle(0, 1).compose(2, 3), "06 Июнь"),

        JULY(cycle(1, 3, 2), "07 Июль"),
        AUGUST(cycle(0, 3, 2), "08 Август"),
        SEPTEMBER(cycle(0, 3).compose(1, 2), "09 Сентябрь"),

        OCTOBER(cycle(0, 2, 3), "10 Октябрь"),
        NOVEMBER(cycle(0, 1, 3), "11 Ноябрь"),
        DECEMBER(Permutation.identity(), "12 Декабрь"),
        ;

        private final Permutation p;
        private final String title;

        static final Supplier<Map<Permutation, Month>> MAP = Suppliers.memoize(() -> {
            Map<Permutation, Month> monthMap = new HashMap<>();
            for (Month month : Month.values()) {
                monthMap.put(month.p, month);
            }
            return monthMap;
        });

        static Month monthOf(Permutation p) {
            return requireNonNull(MAP.get().get(p));
        }

        Permutation permutation() {
            return p;
        }

        String title() {
            return title + " " + p.toString();
        }

        Month(Permutation p, String title) {
            this.p = p;
            this.title = title;
        }
    }

    @Override
    public void start(Stage stage) {
        List<List<Permutation>> permutations = new ArrayList<>();
        permutations.add(List.of(cycle(0, 1, 2)));
        permutations.add(List.of(cycle(0, 1, 2)));
        permutations.add(List.of(klein(2), cycle(0, 1, 2)));
        permutations.add(List.of(cycle(0, 2, 3)));
        permutations.add(List.of(cycle(0, 2, 3)));
        permutations.add(List.of(klein(3), cycle(0, 2, 3)));
        permutations.add(List.of(cycle(0, 3, 1)));
        permutations.add(List.of(cycle(0, 3, 1)));
        permutations.add(List.of(klein(2), cycle(0, 3, 1)));
        permutations.add(List.of(cycle(1, 3, 2)));
        permutations.add(List.of(cycle(1, 3, 2)));
        permutations.add(List.of(klein(3), cycle(1, 3, 2)));
        run(stage, permutations);
    }

    static void run(
            Stage stage,
            List<List<Permutation>> permutations) {
        List<CommandSequence> result = State.create().getCommands(permutations.stream().map(Row::explicitRow).toList()).stream()
                .map(r -> r.sequence().title(monthOf(r.permutation()).title())).toList();
        PermutationView view = PermutationView.create(stage);
        view.init();
        stage.show();
        Consumer<List<CommandSequence>> onSave = MyTest::onSave;
        Presenter.create(view, onSave, result).run();
    }

    static Permutation klein(int j) {
        int k = -1;
        int l = -1;
        for (int m = 1; m < 4; m++) {
            if (m != j) {
                if (k == -1) {
                    k = m;
                } else if (l == -1) {
                    l = m;
                } else {
                    throw new IllegalArgumentException("j=" + j);
                }
            }
        }
        return cycle(0, j).compose(k, l);
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

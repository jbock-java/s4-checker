package uppu.test;

import io.parmigiano.Permutation;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
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
import java.util.stream.Collectors;

import static io.parmigiano.Permutation.cycle;
import static uppu.test.Month.monthOf;

public class CalendarTest extends Application {

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
        List<CommandSequence.Result> commands = State.create().getCommands(permutations.stream().map(Row::explicitRow).toList());
        List<CommandSequence.Result> result = commands.stream()
                .map(r -> r.title(monthOf(r.permutation()).title())).toList();
        PermutationView view = PermutationView.create(stage);
        view.init();
        stage.show();
        Consumer<List<CommandSequence.Result>> onSave = CalendarTest::onSave;
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

    static void onSave(List<CommandSequence.Result> newActions) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Saving disabled");
        alert.setHeaderText("Saving disabled");
        alert.setContentText("Saving disabled, please copy this manually:");
        TextArea textArea = new TextArea(newActions.stream()
                .map(CommandSequence.Result::sequence)
                .map(CommandSequence::toString)
                .collect(Collectors.joining(System.lineSeparator())));
        textArea.setEditable(false);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.show();
    }
}

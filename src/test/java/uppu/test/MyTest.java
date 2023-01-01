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
import uppu.model.ActionSequence;
import uppu.model.CommandSequence;
import uppu.model.State;
import uppu.parse.Row;
import uppu.view.PermutationView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.parmigiano.Permutation.cycle;

public class MyTest extends Application {

    @Override
    public void start(Stage stage) {
        Permutation current = Permutation.identity();
        List<Permutation> permutations1 = List.of(
                cycle(0, 1).compose(2, 3), cycle(0, 1));
        List<Permutation> permutations2 = List.of(
                cycle(0, 1).compose(2, 3), cycle(0, 3));
        List<CommandSequence> result = new ArrayList<>();
        for (List<Permutation> permutations : List.of(permutations1, permutations2)) {
            CommandSequence.Result r = CommandSequence.toSequence(new Row.ExplicitRow(permutations), current);
            result.add(r.sequence());
            current = r.permutation().compose(current);
            r = CommandSequence.toSequence(Row.HOME_ROW, current);
            result.add(r.sequence());
            current = r.permutation().compose(current);
        }
        PermutationView view = PermutationView.create(stage);
        view.init();
        stage.show();
        Consumer<List<CommandSequence>> onSave = MyTest::onSave;
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

    static void onSave(List<CommandSequence> newActions) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Saving disabled");
        alert.setHeaderText("Saving disabled");
        alert.setContentText("Saving disabled, please copy this manually:");
        TextArea textArea = new TextArea(newActions.stream()
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
        alert.showAndWait();
    }
}

package uppu.test;

import io.parmigiano.Permutation;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import uppu.model.CommandSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static io.parmigiano.Permutation.cycle;

public class MyTest extends Application {

    @Override
    public void start(Stage stage) {
        List<List<Permutation>> permutations = new ArrayList<>();
        Permutation p = cycle(0, 1, 2);
        Permutation q = cycle(0, 1, 3);
        Permutation qq = q.compose(q);
        
        Permutation march = p.compose(q);
        Permutation june = p.compose(qq).compose(p);

        List<Permutation> january = List.of(june, qq, june);
        List<Permutation> april = List.of(june, p, june);
        List<Permutation> october = List.of(march, p, march);
        List<Permutation> july = List.of(march, qq, march);

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

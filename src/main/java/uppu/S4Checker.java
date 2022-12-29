package uppu;

import io.jbock.util.Either;
import io.parmigiano.Permutation;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import uppu.model.ActionSequence;
import uppu.model.CommandSequence;
import uppu.model.State;
import uppu.parse.LineParser;
import uppu.parse.Row;
import uppu.view.PermutationView;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static io.jbock.util.Either.right;

public class S4Checker extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parameters parameters = getParameters();
        CommandLine commandLine = new CommandLineParser().parseOrExit(parameters.getRaw().toArray(new String[0]));
        List<String> lines = Files.readAllLines(commandLine.input().toPath());
        readLines(lines).ifLeftOrElse(
                error -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
                    alert.showAndWait();
                },
                sequences -> {
                    List<ActionSequence> actions = State.create().getActions(sequences);
                    PermutationView view = PermutationView.create(stage);
                    view.init();
                    stage.show();
                    new Presenter(view, commandLine.input().toPath(), actions).run();
                });
    }

    public static void main(String[] args) {
        launch(args);
    }

    static Either<String, List<CommandSequence>> readLines(List<String> lines) {
        Permutation current = Permutation.identity();
        List<CommandSequence> result = new ArrayList<>(lines.size());
        for (String line : lines) {
            Either<String, Row> parsed = LineParser.parse(line);
            if (parsed.isLeft()) {
                return parsed.map(x -> List.of());
            }
            Row row = parsed.getRight().orElseThrow();
            if (row.permutations(current).isEmpty()) {
                continue;
            }
            CommandSequence.Result r = CommandSequence.toSequence(row, current);
            result.add(r.sequence());
            current = r.permutation().compose(current);
        }
        return right(result);
    }
}

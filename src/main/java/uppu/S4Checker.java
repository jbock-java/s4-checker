package uppu;

import io.jbock.util.Either;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import uppu.model.CommandSequence;
import uppu.model.State;
import uppu.parse.LineParser;
import uppu.view.PermutationView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Consumer;

import static io.jbock.util.Eithers.firstFailure;

public class S4Checker extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parameters parameters = getParameters();
        CommandLine commandLine = new CommandLineParser().parseOrExit(parameters.getRaw().toArray(new String[0]));
        List<String> lines = Files.readAllLines(commandLine.input().toPath());
        readLines(lines).ifLeftOrElse(
                error -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, error, ButtonType.OK);
                    alert.show();
                },
                sequences -> {
                    PermutationView view = PermutationView.create(stage);
                    view.init();
                    stage.show();
                    Consumer<List<CommandSequence>> onSave = newActions ->
                            writeToFile(commandLine.input().toPath(), newActions);
                    Presenter.create(view, onSave, sequences).run();
                });
    }

    public static void main(String[] args) {
        launch(args);
    }

    static Either<String, List<CommandSequence>> readLines(List<String> lines) {
        return lines.stream().map(LineParser::parse).collect(firstFailure())
                .map(rows -> State.create().getCommands(rows).stream()
                        .map(CommandSequence.Result::sequence)
                        .toList());
    }


    private void writeToFile(Path path, List<CommandSequence> newActions) {
        try {
            List<String> lines = newActions.stream().map(CommandSequence::toString).toList();
            Files.write(path, lines, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.toString(), ButtonType.OK);
            alert.show();
        }
    }
}

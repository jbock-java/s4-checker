package uppu.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uppu.model.ActionSequence;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class InputView {

    private static final int HEIGHT_TEXTAREA = 600;
    private final Stage stage = new Stage(StageStyle.DECORATED);
    private final TextArea textArea = new TextArea();
    private final Button saveButton = new Button("Save");
    private final BorderPane borderPane = new BorderPane();

    private InputView() {
    }

    public static InputView create() {
        InputView result = new InputView();
        result.init();
        return result;
    }

    private void init() {
        stage.setTitle("Edit");
        stage.setScene(createScene());
        stage.show();
    }

    private Scene createScene() {
        borderPane.setCenter(textArea);
        borderPane.setBottom(saveButton);
        return new Scene(borderPane, PermutationView.WIDTH_PANEL * 2, HEIGHT_TEXTAREA);
    }

    public void setContent(List<ActionSequence> actions) {
        for (ActionSequence action : actions) {
            textArea.appendText(action.toString());
            textArea.appendText(System.lineSeparator());
        }
    }

    public void setOnSave(Consumer<List<String>> consumer) {
        saveButton.setOnMouseClicked(e -> {
            String text = textArea.getText();
            String[] lines = text.split("\\R", -1);
            consumer.accept(Stream.of(lines).filter(line -> !line.isEmpty()).toList());
        });
    }

    public void close() {
        stage.close();
    }
}

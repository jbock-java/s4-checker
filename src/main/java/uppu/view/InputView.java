package uppu.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uppu.model.ActionSequence;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class InputView {

    private static final int HEIGHT_TEXTAREA = 600;
    private final Stage stage = new Stage(StageStyle.DECORATED);
    private final TextArea textArea = new TextArea();
    private final GridPane buttonPanel = new GridPane();
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
        buttonPanel.setAlignment(Pos.BASELINE_CENTER);
        buttonPanel.add(saveButton, 0, 0);
        borderPane.setBottom(buttonPanel);
        URL style = Objects.requireNonNull(getClass().getResource("/uppu/css/style.css"));
        Scene scene = new Scene(borderPane, PermutationView.WIDTH_PANEL * 2, HEIGHT_TEXTAREA);
        scene.getStylesheets().add(style.toExternalForm());
        return scene;
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

    public void setOnCancel(Runnable onCancel) {
        stage.setOnCloseRequest(e -> onCancel.run());
    }

    public void close() {
        stage.close();
    }
}

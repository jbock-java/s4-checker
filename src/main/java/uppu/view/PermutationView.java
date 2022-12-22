package uppu.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uppu.model.ActionSequence;
import uppu.model.HomePoints;

import java.net.URL;
import java.util.List;
import java.util.function.Consumer;

import static javafx.collections.FXCollections.observableArrayList;

public class PermutationView {

    private static final int WIDTH_CANVAS = (int) (280 * HomePoints.SCALE);
    private static final int HEIGHT = (int) (300 * HomePoints.SCALE);
    private static final int WIDTH_PANEL = 500;
    private static final int HEIGHT_SLIDER = 12;
    private static final int INITIAL_SPEED = 16;
    private static final int HEIGHT_BUTTON_PANE = 20;
    
    private static final Color GRAY = Color.rgb(64, 64, 64);

    private final Canvas canvas = new Canvas(WIDTH_CANVAS, HEIGHT);

    private final ListView<ActionSequence> actions = new ListView<>();
    private final Stage stage;
    private final SplitPane splitPane = new SplitPane();
    private final GridPane buttonPanel = new GridPane();
    private final BorderPane borderPane = new BorderPane();
    private final Slider slider = new Slider(0, 32, INITIAL_SPEED);
    private final BorderPane sidePanel = new BorderPane();
    private final Button pauseButton = new Button("Pause");
    private final Button editButton = new Button("Edit");

    private PermutationView(Stage stage) {
        this.stage = stage;
    }

    public static PermutationView create(Stage stage) {
        return new PermutationView(stage);
    }

    public void init() {
        createElements();
        borderPane.setCenter(splitPane);
        borderPane.setBottom(slider);
        Scene scene = new Scene(borderPane);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });
        URL style = getClass().getResource("/uppu/css/style.css");
        scene.getStylesheets().add(style.toExternalForm());
        stage.setScene(scene);
    }

    private void createElements() {
        StackPane canvasPane = new StackPane();
        canvasPane.setMaxWidth(WIDTH_CANVAS);
        canvasPane.setMinWidth(WIDTH_CANVAS);
        canvasPane.getChildren().add(canvas);
        canvasPane.setBackground(new Background(new BackgroundFill(GRAY, null, null)));
        sidePanel.setMinWidth(WIDTH_PANEL);
        sidePanel.setBackground(new Background(new BackgroundFill(GRAY, null, null)));
        sidePanel.setCenter(actions);
        sidePanel.setBottom(buttonPanel);
        buttonPanel.setAlignment(Pos.BASELINE_CENTER);
        buttonPanel.add(pauseButton, 0, 0);
        buttonPanel.add(editButton, 1, 0);
        splitPane.getItems().addAll(canvasPane, sidePanel);
        splitPane.setDividerPositions(0.5f, 0.5f);
    }

    public GraphicsContext getGraphicsContext() {
        return canvas.getGraphicsContext2D();
    }

    public void setTitle(String title) {
        stage.setTitle(title);
    }

    public void setOnActionSelected(Consumer<ActionSequence> consumer) {
        actions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ActionSequence>() {
            @Override
            public void changed(
                    ObservableValue<? extends ActionSequence> observable,
                    ActionSequence oldValue, 
                    ActionSequence newValue) {
                consumer.accept(newValue);
            }
        });
    }

    public void setSelectedAction(ActionSequence action) {
        actions.getSelectionModel().select(action);
    }

    public void setActions(List<ActionSequence> actions) {
        ObservableList<ActionSequence> data = observableArrayList();
        data.addAll(actions);
        this.actions.setItems(data);
    }
}

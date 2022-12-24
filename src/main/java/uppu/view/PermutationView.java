package uppu.view;

import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import uppu.engine.Mover;
import uppu.model.Action;
import uppu.model.ActionSequence;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static javafx.collections.FXCollections.observableArrayList;
import static uppu.model.Spheres.spheres;

public class PermutationView {

    private static final int WIDTH_CANVAS = 560;
    private static final int HEIGHT = 600;
    private static final int WIDTH_PANEL = 500;
    private static final int HEIGHT_SLIDER = 12;
    private static final int INITIAL_SPEED = 16;
    private static final int HEIGHT_BUTTON_PANE = 20;

    private static final Color GRAY = Color.rgb(64, 64, 64);

    private final ListView<ActionSequence> actions = new ListView<>();
    private final Stage stage;
    private final SplitPane splitPane = new SplitPane();
    private final GridPane buttonPanel = new GridPane();
    private final BorderPane borderPane = new BorderPane();
    private final Slider slider = new Slider(0, 32, INITIAL_SPEED);
    private final BorderPane sidePanel = new BorderPane();
    private final Button pauseButton = new Button("Pause");
    private final Button editButton = new Button("Edit");
    Consumer<ActionSequence> onSelected = action -> {
    };
    private final ChangeListener<ActionSequence> changeListener = (observable, oldValue, newValue) -> onSelected.accept(newValue);
    private Runnable onFinished = () -> {
    };

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
        URL style = Objects.requireNonNull(getClass().getResource("/uppu/css/style.css"));
        scene.getStylesheets().add(style.toExternalForm());
        stage.setScene(scene);
    }

    private void createElements() {
        sidePanel.setMinWidth(WIDTH_PANEL);
        sidePanel.setBackground(new Background(new BackgroundFill(GRAY, null, null)));
        sidePanel.setCenter(actions);
        sidePanel.setBottom(buttonPanel);
        buttonPanel.setAlignment(Pos.BASELINE_CENTER);
        buttonPanel.add(pauseButton, 0, 0);
        buttonPanel.add(editButton, 1, 0);
        splitPane.getItems().addAll(createSubScene(), sidePanel);
        splitPane.setDividerPositions(0.5f);
        actions.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private SubScene createSubScene() {
        // Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                new Translate(-0.5f, 0, -25),
                new Rotate(-30, new Point3D(1, 0, 0)),
                new Rotate(9, new Point3D(0, 1, 0)));

        // Build the Scene Graph
        Group root = new Group();
        root.getChildren().add(camera);
        root.getChildren().add(spheres().redSphere().sphere());
        root.getChildren().add(spheres().blueSphere().sphere());
        root.getChildren().add(spheres().greenSphere().sphere());
        root.getChildren().add(spheres().silverSphere().sphere());

        // Use a SubScene
        SubScene subScene = new SubScene(root, WIDTH_CANVAS, HEIGHT, true, SceneAntialiasing.DISABLED);
        subScene.setFill(GRAY);
        subScene.setCamera(camera);
        return subScene;
    }

    public void setTitle(String title) {
        stage.setTitle(title);
    }

    public void setOnActionSelected(Consumer<ActionSequence> onSelected) {
        this.onSelected = onSelected;
        actions.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public void setOnAnimationFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    public void setSelectedAction(ActionSequence actions) {
        selectInListView(actions);
        runNextAction(new ArrayDeque<>(actions.actions()));
    }

    private void runNextAction(Deque<Action> actions) {
        Action action = actions.pollFirst();
        if (action == null) {
            onFinished.run();
        }
        if (action instanceof Action.WaitAction) {
            PauseTransition wait = new PauseTransition(Duration.millis(250));
            wait.setOnFinished(e -> runNextAction(actions));
            wait.play();
        }
        AtomicInteger count = new AtomicInteger(4);
        if (action instanceof Action.MoveAction) {
            for (Mover mover : ((Action.MoveAction) action).movers()) {
                mover.ball().move(mover.source(), mover.target(), 3, () -> {
                    if (count.decrementAndGet() == 0) {
                        runNextAction(actions);
                    }
                });
            }
        }
    }

    private void selectInListView(ActionSequence actionSequence) {
        actions.getSelectionModel().selectedItemProperty().removeListener(changeListener);
        actions.getSelectionModel().select(actionSequence);
        actions.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public void setActions(List<ActionSequence> actions) {
        ObservableList<ActionSequence> data = observableArrayList();
        data.addAll(actions);
        this.actions.setItems(data);
    }

    public void setRunning(boolean running) {
        // TODO
    }
}

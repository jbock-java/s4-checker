package uppu.view;

import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
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
import uppu.engine.Path;
import uppu.model.Action;
import uppu.model.ActionSequence;
import uppu.model.Colour;
import uppu.model.Rotation;

import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static java.util.stream.Collectors.groupingBy;
import static javafx.collections.FXCollections.observableArrayList;

public class PermutationView {

    private static final Point3D CAMERA_POINT = new Point3D(0, -2, -5);
    private static final int WIDTH_CANVAS = 860;
    private static final int HEIGHT = 900;
    static final int WIDTH_PANEL = 500;

    private static final Color GRAY = Color.rgb(64, 64, 64);

    private final ListView<ActionSequence> actions = new ListView<>();
    private final Stage stage;
    private final SplitPane splitPane = new SplitPane();
    private final GridPane buttonPanel = new GridPane();
    private final BorderPane borderPane = new BorderPane();
    private final BorderPane sidePanel = new BorderPane();
    private final Button pauseButton = new Button("Pause");
    private final Button editButton = new Button("Edit");
    private final Group root = new Group();
    private final SubScene subScene = new SubScene(root, WIDTH_CANVAS, HEIGHT, true, SceneAntialiasing.DISABLED);

    Consumer<ActionSequence> onSelected = action -> {
    };
    private final ChangeListener<ActionSequence> changeListener = (observable, oldValue, newValue) -> onSelected.accept(newValue);
    private Runnable onFinished = () -> {
    };
    private Runnable onPauseButtonClicked = () -> {
    };
    private Runnable onEditButtonClicked = () -> {
    };

    private PauseTransition wait;
    private final List<Timeline> tl = new ArrayList<>();

    // Build the Scene Graph

    private PermutationView(Stage stage) {
        this.stage = stage;
    }

    public static PermutationView create(Stage stage) {
        return new PermutationView(stage);
    }

    public void init() {
        createElements();
        borderPane.setCenter(splitPane);
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
        pauseButton.setOnMouseClicked(e -> onPauseButtonClicked.run());
        editButton.setOnMouseClicked(e -> onEditButtonClicked.run());
        actions.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                stage.close();
                return;
            }
            if (e.getCode() == KeyCode.SPACE) {
                onPauseButtonClicked.run();
                return;
            }
            if (e.getCode() == KeyCode.E) {
                onEditButtonClicked.run();
            }
        });
    }

    private SubScene createSubScene() {
        // Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                new Translate(CAMERA_POINT.getX(), CAMERA_POINT.getY(), CAMERA_POINT.getZ()),
                new Rotate(-20.6f, new Point3D(1, 0, 0)));

        root.getChildren().add(camera);
        for (Colour color : Colour.getValues()) {
            color.homeSphere().setLocation(color.homePoint());
            color.sphere().setLocation(color.homePoint());
            root.getChildren().add(color.homeSphere().sphere());
            root.getChildren().add(color.sphere().sphere());
        }

        // Use a SubScene
        subScene.setFill(GRAY);
        subScene.setCamera(camera);
        return subScene;
    }

    public void setOnActionSelected(Consumer<ActionSequence> onSelected) {
        this.onSelected = onSelected;
        actions.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public void setOnAnimationFinished(Runnable onFinished) {
        this.onFinished = onFinished;
    }

    public void setSelectedAction(ActionSequence actionSequence) {
        actions.getSelectionModel().selectedItemProperty().removeListener(changeListener);
        actions.getSelectionModel().select(actionSequence);
        actions.getSelectionModel().selectedItemProperty().addListener(changeListener);
        runNextAction(new ArrayDeque<>(actionSequence.actions()));
    }

    private void runNextAction(Deque<Action> actions) {
        tl.clear();
        Action action = actions.pollFirst();
        if (action == null) {
            wait = new PauseTransition(Duration.millis(400));
            wait.setOnFinished(e -> {
                wait = null;
                tl.clear();
                onFinished.run();
            });
            wait.play();
            return;
        }
        if (action instanceof Action.WaitAction) {
            if (wait != null) {
                wait.stop();
            }
            wait = new PauseTransition(Duration.millis(320));
            wait.setOnFinished(e -> {
                wait = null;
                runNextAction(actions);
            });
            wait.play();
        }
        if (action instanceof Action.MoveAction) {
            List<Mover> zero = ((Action.MoveAction) action).zeroMovers();
            for (Mover m : zero) {
                m.ball().setLocation(m.path().destination().homePoint());
            }
            List<Mover> allMovers = ((Action.MoveAction) action).nonZeroMovers();
            if (allMovers.isEmpty()) {
                runNextAction(actions);
                return;
            }
            AtomicInteger count = new AtomicInteger(allMovers.size());
            if (allMovers.size() == 3) {
                Mover m0 = allMovers.get(0);
                Mover m1 = allMovers.get(1);
                Mover m2 = allMovers.get(2);
                Point3D axis = m0.midPoint().add(m1.midPoint()).add(m2.midPoint());
                Rotation rotation = Rotation.fromAxis(axis);
                Point3D source = m0.source().homePoint();
                Point3D dest = m0.destination().homePoint();
                if (rotation.apply(source, 0.01).distance(dest) > source.distance(dest)) {
                    rotation = rotation.invert();
                }
                for (int i = 0; i < 3; i++) {
                    tl.add(allMovers.get(i).moveCircle(rotation, 3, Math.PI * (2f / 3f), () -> {
                        if (count.decrementAndGet() == 0) {
                            runNextAction(actions);
                        }
                    }));
                }
                return;
            }
            Map<Path, List<Mover>> m = allMovers.stream()
                    .collect(groupingBy(mover -> mover.path().normalize()));
            if (m.size() == 2 && m.values().stream().allMatch(movers -> movers.size() == 2)) {
                List<List<Mover>> values = List.copyOf(m.values());
                List<Mover> group0 = values.get(0);
                List<Mover> group1 = values.get(1);
                Mover a = group0.get(0);
                Mover b = group1.get(0);
                Point3D axis = a.midPoint().subtract(b.midPoint());
                Rotation rotation = Rotation.fromAxis(axis);
                for (Mover mover : allMovers) {
                    tl.add(mover.moveCircle(rotation, 4.2, Math.PI, () -> {
                        if (count.decrementAndGet() == 0) {
                            runNextAction(actions);
                        }
                    }));
                }
                return;
            }
            for (List<Mover> movers : m.values()) {
                if (movers.size() == 1) {
                    tl.add(movers.get(0).moveSimple(3, () -> {
                        if (count.decrementAndGet() == 0) {
                            runNextAction(actions);
                        }
                    }));
                } else {
                    tl.add(movers.get(0).move(movers.get(0).midPoint().normalize(), 3, () -> {
                        if (count.decrementAndGet() == 0) {
                            runNextAction(actions);
                        }
                    }));
                    tl.add(movers.get(1).move(movers.get(0).midPoint().normalize().multiply(-1), 3, () -> {
                        if (count.decrementAndGet() == 0) {
                            runNextAction(actions);
                        }
                    }));
                }
            }
        }
    }

    public void setActions(List<ActionSequence> actions) {
        this.actions.getSelectionModel().selectedItemProperty().removeListener(changeListener);
        this.actions.setItems(observableArrayList(actions));
        this.actions.getSelectionModel().selectedItemProperty().addListener(changeListener);
    }

    public void setOnEditButtonClicked(Runnable onClick) {
        this.onEditButtonClicked = onClick;
    }

    public void setOnPauseButtonClicked(Runnable onClick) {
        this.onPauseButtonClicked = onClick;
    }

    public void setRunning(boolean running) {
        if (!running && wait != null) {
            wait.pause();
        }
        if (running && wait != null) {
            wait.play();
        }
        if (running) {
            for (Timeline timeline : tl) {
                timeline.play();
            }
        } else {
            for (Timeline timeline : tl) {
                timeline.pause();
            }
        }
    }

    public void stop() {
        if (wait != null) {
            wait.stop();
            wait = null;
        }
        for (Timeline timeline : tl) {
            timeline.stop();
        }
        tl.clear();
    }

    public void setHomesVisible(
            boolean visible) {
        for (Colour color : Colour.getValues()) {
            color.homeSphere().sphere().setVisible(visible);
        }
    }
}

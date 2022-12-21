package uppu.view;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import uppu.model.HomePoints;

public class PermutationView {

    private static final int WIDTH_CANVAS = (int) (280 * HomePoints.SCALE);
    private static final int HEIGHT = (int) (300 * HomePoints.SCALE);
    public static final int WIDTH_PANEL = 500;
    public static final int HEIGHT_SLIDER = 12;
    public static final int INITIAL_SPEED = 16;
    public static final int HEIGHT_BUTTON_PANE = 20;
    public static final Color GRAY = Color.rgb(64, 64, 64);

    private final Canvas canvas = new Canvas(WIDTH_CANVAS, HEIGHT);
    
    private final Stage stage;
    private final Scene scene;
    private final SplitPane splitPane;

    private PermutationView(Stage stage, Scene scene, SplitPane splitPane) {
        this.stage = stage;
        this.scene = scene;
        this.splitPane = splitPane;
    }

    public static PermutationView create(Stage stage) {
        SplitPane sp = new SplitPane();
        Scene scene = new Scene(sp);
        scene.setFill(Color.BLACK);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, t -> {
            if (t.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });
        return new PermutationView(stage, scene, sp);
    }

    public void init() {
        stage.setWidth(WIDTH_CANVAS + WIDTH_PANEL);
        stage.setHeight(HEIGHT + HEIGHT_SLIDER);
        createElements();
        stage.setScene(scene);
    }

    private void createElements() {
        StackPane sp1 = new StackPane();
        sp1.getChildren().add(canvas);
        sp1.setBackground(new Background(new BackgroundFill(GRAY, null, null)));
        StackPane sp2 = new StackPane();
        sp2.setBackground(new Background(new BackgroundFill(GRAY, null, null)));
        sp2.getChildren().add(new Button("OK"));
        splitPane.getItems().addAll(sp1, sp2);
        splitPane.setDividerPositions(0.5f, 0.5f);
    }
}

package uppu.model;

import java.util.Arrays;
import java.util.List;

public enum Color {

    // WILD_WATERMELON
    RED(javafx.scene.paint.Color.rgb(252, 108, 133).brighter()),

    // PANTONE_GREEN
    GREEN(javafx.scene.paint.Color.rgb(152, 251, 152)),

    // NCS_YELLOW
//    YELLOW(new java.awt.Color(255, 211, 0)),

    // ROBIN_EGG_BLUE
    BLUE(javafx.scene.paint.Color.rgb(0, 204, 204).brighter()),

    SILVER(javafx.scene.paint.Color.rgb(220, 220, 220)),
    ;

    private final javafx.scene.paint.Color awtColor;
    private final javafx.scene.paint.Color glowColor;

    Color(javafx.scene.paint.Color awtColor) {
        this.awtColor = awtColor;
        this.glowColor = awtColor.darker();
    }

    public javafx.scene.paint.Color awtColor() {
        return awtColor;
    }

    public javafx.scene.paint.Color glowColor() {
        return glowColor;
    }

    public static List<Color> colors(int n) {
        return Arrays.stream(values()).limit(n).toList();
    }
}

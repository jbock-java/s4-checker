package uppu.model;

import javafx.geometry.Point3D;
import uppu.util.Suppliers;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public enum Color {

    // WILD_WATERMELON
    RED(javafx.scene.paint.Color.rgb(252, 108, 133).brighter()),

    // PANTONE_GREEN
    GREEN(javafx.scene.paint.Color.rgb(152, 251, 152)),

    // NCS_YELLOW
//    YELLOW(new java.awt.Color(255, 211, 0)),

    // ROBIN_EGG_BLUE
    BLUE(javafx.scene.paint.Color.rgb(0, 204, 204).brighter()),

    SILVER(javafx.scene.paint.Color.rgb(220, 220, 220))
    ;

    private final javafx.scene.paint.Color awtColor;
    private final javafx.scene.paint.Color solidColor;

    Color(javafx.scene.paint.Color awtColor) {
        this.awtColor = awtColor.deriveColor(0, 1, 0.9, 1d);
        this.solidColor = awtColor;
    }

    private static final Supplier<List<Color>> VALUES = Suppliers.memoize(() -> List.of(values())); 
    
    public static List<Color> getValues() {
        return VALUES.get();
    }

    public javafx.scene.paint.Color awtColor() {
        return awtColor;
    }

    public javafx.scene.paint.Color solidColor() {
        return solidColor;
    }

    public Ball ball() {
        return Spheres.spheres().get(this);
    }

    public static List<Color> colors(int n) {
        return Arrays.stream(values()).limit(n).toList();
    }

    public Point3D homePoint() {
        return HomePoints.homePoint(this);
    }
}

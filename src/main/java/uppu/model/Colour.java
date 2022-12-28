package uppu.model;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import uppu.util.Suppliers;

import java.util.List;
import java.util.function.Supplier;

public enum Colour {

    RED(Color.rgb(237, 201, 175).brighter()),

    GREEN(Color.rgb(169, 186, 157).deriveColor(1, 1, 1.2, 1)),

    BLUE(Color.rgb(175, 219, 245).brighter()),

    SILVER(Color.rgb(220, 220, 220).deriveColor(1, 1, 0.9, 1));

    private final Color color;

    Colour(Color color) {
        this.color = color;
    }

    private static final Supplier<List<Colour>> VALUES = Suppliers.memoize(() -> List.of(values()));

    public static List<Colour> getValues() {
        return VALUES.get();
    }

    public static Colour get(int ordinal) {
        return getValues().get(ordinal);
    }

    public Color color() {
        return color;
    }

    public Ball sphere() {
        return Spheres.spheres().get(this);
    }

    public Ball homeSphere() {
        return Spheres.spheres().getHome(this);
    }

    public Point3D homePoint() {
        return HomePoints.homePoint(this);
    }
}

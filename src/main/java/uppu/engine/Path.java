package uppu.engine;

import javafx.geometry.Point3D;
import uppu.model.Colour;

public record Path(
        Colour source, // from which color-home
        Colour destination // to which color-home
) {

    public Colour lower() {
        return source.ordinal() < destination.ordinal() ? source : destination;
    }

    public Point3D midPoint() {
        return source.homePoint().add(destination.homePoint()).multiply(0.5d);
    }

    public boolean isZero() {
        return source == destination;
    }

    public boolean contains(Colour colour) {
        return source == colour || destination == colour;
    }
}

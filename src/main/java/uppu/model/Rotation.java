package uppu.model;

import javafx.geometry.Point3D;

import java.util.function.DoubleBinaryOperator;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public record Rotation(
        int inverted,
        DoubleBinaryOperator r00,
        DoubleBinaryOperator r01,
        DoubleBinaryOperator r02,
        DoubleBinaryOperator r10,
        DoubleBinaryOperator r11,
        DoubleBinaryOperator r12,
        DoubleBinaryOperator r20,
        DoubleBinaryOperator r21,
        DoubleBinaryOperator r22) {

    public static Rotation fromAxis(Point3D u) {
        u = u.normalize();
        double x = u.getX();
        double y = u.getY();
        double z = u.getZ();
        double xx = x * x;
        double yy = y * y;
        double zz = z * z;
        double xy = x * y;
        double xz = x * z;
        double yz = y * z;
        return new Rotation(1,
                (sin_t, cos_t) -> cos_t + xx * (1 - cos_t),
                (sin_t, cos_t) -> xy * (1 - cos_t) + z * sin_t,
                (sin_t, cos_t) -> xz * (1 - cos_t) - y * sin_t,
                (sin_t, cos_t) -> xy * (1 - cos_t) - z * sin_t,
                (sin_t, cos_t) -> cos_t + yy * (1 - cos_t),
                (sin_t, cos_t) -> yz * (1 - cos_t) + x * sin_t,
                (sin_t, cos_t) -> xz * (1 - cos_t) + y * sin_t,
                (sin_t, cos_t) -> yz * (1 - cos_t) - x * sin_t,
                (sin_t, cos_t) -> cos_t + zz * (1 - cos_t));
    }

    public Rotation invert() {
        return new Rotation(inverted * -1, 
                r00, r01, r02,
                r10, r11, r12,
                r20, r21, r22);
    }

    public Point3D apply(Point3D p, double t) {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        double sin_t = sin(t) * inverted;
        double cos_t = cos(t);
        double x2 = r00.applyAsDouble(sin_t, cos_t) * x
                + r01.applyAsDouble(sin_t, cos_t) * y
                + r02.applyAsDouble(sin_t, cos_t) * z;
        double y2 = r10.applyAsDouble(sin_t, cos_t) * x
                + r11.applyAsDouble(sin_t, cos_t) * y
                + r12.applyAsDouble(sin_t, cos_t) * z;
        double z2 = r20.applyAsDouble(sin_t, cos_t) * x
                + r21.applyAsDouble(sin_t, cos_t) * y
                + r22.applyAsDouble(sin_t, cos_t) * z;
        return new Point3D(x2, y2, z2);
    }
}

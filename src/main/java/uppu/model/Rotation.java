package uppu.model;

import javafx.geometry.Point3D;

import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public record Rotation(
        DoubleUnaryOperator r00,
        DoubleUnaryOperator r10,
        DoubleUnaryOperator r20,
        DoubleUnaryOperator r01,
        DoubleUnaryOperator r11,
        DoubleUnaryOperator r21,
        DoubleUnaryOperator r02,
        DoubleUnaryOperator r12,
        DoubleUnaryOperator r22) {

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
        return new Rotation(
                // ROW_0
                t -> {
                    double cos_t = cos(t);
                    return cos_t + xx * (1 - cos_t);
                },
                t -> xy * (1 - cos(t)) - z * sin(t),
                t -> xz * (1 - cos(t)) + y * sin(t),
                // ROW_1
                t -> xy * (1 - cos(t)) + z * sin(t),
                t -> {
                    double cos_t = cos(t);
                    return cos_t + yy * (1 - cos_t);
                },
                t -> yz * (1 - cos(t)) - x * sin(t),
                // ROW_2
                t -> xz * (1 - cos(t)) - y * sin(t),
                t -> yz * (1 - cos(t)) + x * sin(t),
                t -> {
                    double cos_t = cos(t);
                    return cos_t + zz * (1 - cos_t);
                });
    }

    public Point3D apply(Point3D p, double t) {
        double x = p.getX();
        double y = p.getY();
        double z = p.getZ();
        double x2 = r00.applyAsDouble(t) * x + r01.applyAsDouble(t) * y + r02.applyAsDouble(t) * z;
        double y2 = r10.applyAsDouble(t) * x + r11.applyAsDouble(t) * y + r12.applyAsDouble(t) * z;
        double z2 = r20.applyAsDouble(t) * x + r21.applyAsDouble(t) * y + r22.applyAsDouble(t) * z;
        return new Point3D(x2, y2, z2);
    }
}

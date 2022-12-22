package uppu.model;

import javafx.scene.canvas.GraphicsContext;

public final class Quadruple {

    private static final int X0 = 0;
    private static final int Y0 = 1;
    private static final int Z0 = 2;
    private static final int X1 = 3;
    private static final int Y1 = 4;
    private static final int Z1 = 5;
    private static final int X2 = 6;
    private static final int Y2 = 7;
    private static final int Z2 = 8;
    private static final int X3 = 9;
    private static final int Y3 = 10;
    private static final int Z3 = 11;
    private final int offsetX;
    private final int offsetY;
    private final float[] state;
    private final Color[] colors;

    private final int clear_offsetX;
    private final int clear_offsetY;
    private final int clear_width;
    private final int clear_height;

    private Quadruple(
            int offsetX,
            int offsetY,
            float[] state,
            Color[] colors) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.state = state;
        this.colors = colors;
         clear_offsetX = offsetX - Action.GLOW_SIZE;
         clear_offsetY = offsetY - Action.GLOW_SIZE;
         clear_width = getWidth() + 2 * Action.GLOW_SIZE;
         clear_height = getHeight() + 2 * Action.GLOW_SIZE;
    }

    public static Quadruple create() {
        Color[] colors = Color.values();
        return new Quadruple(0, 0, new float[12], colors);
    }

    public Quadruple offset(int x, int y) {
        return new Quadruple(x, y, state, colors);
    }

    public void set(Color c, float x, float y, float z) {
        setX(c, x);
        setY(c, y);
        setZ(c, z);
    }

    private void setX(Color c, float x) {
        switch (c) {
            case RED -> state[X0] = x;
            case GREEN -> state[X1] = x;
            case BLUE -> state[X2] = x;
            case SILVER -> state[X3] = x;
        }
    }

    private void setY(Color c, float y) {
        switch (c) {
            case RED -> state[Y0] = y;
            case GREEN -> state[Y1] = y;
            case BLUE -> state[Y2] = y;
            case SILVER -> state[Y3] = y;
        }
    }

    private void setZ(Color c, float z) {
        switch (c) {
            case RED -> state[Z0] = z;
            case GREEN -> state[Z1] = z;
            case BLUE -> state[Z2] = z;
            case SILVER -> state[Z3] = z;
        }
    }

    public float getX(Color c) {
        return switch (c) {
            case RED -> state[X0];
            case GREEN -> state[X1];
            case BLUE -> state[X2];
            case SILVER -> state[X3];
        };
    }

    public float getY(Color c) {
        return switch (c) {
            case RED -> state[Y0];
            case GREEN -> state[Y1];
            case BLUE -> state[Y2];
            case SILVER -> state[Y3];
        };
    }

    public float getZ(Color c) {
        return switch (c) {
            case RED -> state[Z0];
            case GREEN -> state[Z1];
            case BLUE -> state[Z2];
            case SILVER -> state[Z3];
        };
    }

    public int getWidth() {
        return HomePoints.MAX + Action.BALL_SIZE;
    }

    public int getHeight() {
        return HomePoints.MAX + Action.BALL_SIZE;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public Color[] colors() {
        return colors;
    }

    public void clearRect(GraphicsContext g) {
        g.clearRect(clear_offsetX, clear_offsetY, clear_width, clear_height);
    }
}

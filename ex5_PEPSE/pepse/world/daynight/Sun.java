package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class to create the sun object
 */
public class Sun {
    private static final Vector2 SUN_DIMENSIONS = new Vector2(120, 120);
    private static final float RADIUS_SCALE_FACTOR = 3f;
    private static final String SUN_TAG = "sun";

    /**
     * Creating the sun object
     */
    public static GameObject create(Vector2 windowDimensions,
                                    float cycleLength) {
        OvalRenderable ovalShape = new OvalRenderable(Color.yellow);
        GameObject sun = new GameObject(Vector2.ZERO, SUN_DIMENSIONS, ovalShape);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(SUN_TAG);

        Vector2 initialSunCenter = new Vector2(windowDimensions.x() / 2,
                (float) (windowDimensions.y() / 2.4));
        Vector2 cycleCenter = new Vector2(windowDimensions.x() / 2,
                (float) (windowDimensions.y() / 1.7));

        new Transition<>(sun,
                (Float angle) -> {
                    Vector2 sunRadius = initialSunCenter.subtract(cycleCenter);
                    sunRadius = sunRadius.mult(RADIUS_SCALE_FACTOR);
                    sun.setCenter(sunRadius.rotated(angle).add(cycleCenter));
                },
                0f, 360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }
}

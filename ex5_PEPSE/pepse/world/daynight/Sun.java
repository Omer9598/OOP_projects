package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {

    public static GameObject create(Vector2 windowDimensions, float cycleLength,
                                    GameObjectCollection gameObjects, int layer) {
        OvalRenderable ovalShape = new OvalRenderable(Color.orange);
        Vector2 sunDimensions = new Vector2(120, 120);
        GameObject sun = new GameObject(Vector2.ZERO, sunDimensions,ovalShape);
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag("sun");
        gameObjects.addGameObject(sun, layer);
        new Transition<>(sun,
                angle -> sun.setCenter(calcPosition(windowDimensions, angle)),
                0f,360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,null);
        return sun;
    }

    /**
     * This function will calculate the position of the sun according to the
     * angleInSky (in degrees) and windowDimensions given
     * @return A Vector2 object that represents the location of the sun
     */
    private static Vector2 calcPosition(Vector2 windowDimensions,
                                        float angleInSky) {
        // Ensure the angle is in the range [0, 360]
        angleInSky = angleInSky % 360;
        if(angleInSky < 0) {
            angleInSky += 360;
        }
        // Calculating the position if the sun
        float angleInRadians = (float) Math.toRadians(angleInSky);
        float xVal = (float) (windowDimensions.x() / 2f +
                550 * Math.sin(angleInRadians));
        float yVal = (float) (windowDimensions.y() / 1.4f +
                (-400) * Math.cos(angleInRadians));
        return new Vector2(xVal, yVal);
    }
}

package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;

public class Night {

    private static final float MIDNIGHT_OPACITY = 0.5f;

    public static GameObject create(GameObjectCollection gameObjects,
                                    Vector2 windowDimensions,
                                    float cycleLength,
                                    int layer) {
        // Creating the night "cube" to cover the whole screen
        Renderable blackCube = new RectangleRenderable(Color.BLACK);
        GameObject night = new GameObject(Vector2.ZERO, windowDimensions,
                blackCube);
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night, layer);
        night.setTag("night");
        new Transition<> (night, night.renderer()::setOpaqueness, 0f,
                MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength / 2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        return night;
    }
}

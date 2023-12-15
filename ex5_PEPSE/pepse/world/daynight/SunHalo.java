package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    private static final Vector2 HALO_DIMENSIONS = new Vector2(300, 300);

    public static GameObject create(GameObjectCollection gameObjects,
                                    GameObject sun,
                                    Color color,
                                    int layer) {
        OvalRenderable ovalShape = new OvalRenderable(color);
        GameObject sunHalo = new GameObject(Vector2.ZERO, HALO_DIMENSIONS,
                ovalShape);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.setTag("sun halo");
        // Making the sunHalo to follow the sun
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}

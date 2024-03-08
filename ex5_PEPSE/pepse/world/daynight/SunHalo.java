package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A class to create the sun halo
 */
public class SunHalo {
    private static final Vector2 HALO_DIMENSIONS = new Vector2(300, 300);
    private static final Color haloColor = new Color(255, 255, 0, 20);
    private static final String SUN_HALO_TAG = "sun halo";

    /**
     * Create the sun halo
     */
    public static GameObject create(GameObject sun) {
        OvalRenderable ovalShape = new OvalRenderable(haloColor);
        GameObject sunHalo = new GameObject(Vector2.ZERO, HALO_DIMENSIONS,
                ovalShape);
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sunHalo.setTag(SUN_HALO_TAG);
        // Making the sunHalo to follow the sun
        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));
        return sunHalo;
    }
}

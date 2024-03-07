package pepse.world;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

/**
 * A class for the energy display
 */
public class EnergyDisplay implements EnergyChangeCallback{
    private static final String ENERGY_FORMAT = "%d%%";
    private final TextRenderable textRenderable;
    private final GameObject displayObject;
    private static final Vector2 DIMENSIONS = new Vector2(80, 40);

    /**
     * Constructor of the class
     */
    public EnergyDisplay() {
        this.textRenderable = new TextRenderable("");
        this.displayObject = new GameObject(Vector2.ZERO, DIMENSIONS,
                textRenderable);
    }

    /**
     * Return the gameObject of the display
     */
    public GameObject getDisplayObject () {
        return displayObject;
    }

    public void onEnergyChanged(int newEnergy) {
        textRenderable.setString(String.format(ENERGY_FORMAT, newEnergy));
    }
}
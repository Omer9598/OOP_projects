package pepse.world.trees;

import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.awt.*;

/**
 * A class to create the fruit in the game
 */
public class Fruit extends Block {
    /**
     * The tag of the fruit
     */
    public static final String FRUIT_TAG = "fruit";

    /**
     * Constructor of the class
     */
    public Fruit(Vector2 topLeftCorner) {
        super(topLeftCorner, new OvalRenderable(Color.RED));
        this.setTag(FRUIT_TAG);
    }
}


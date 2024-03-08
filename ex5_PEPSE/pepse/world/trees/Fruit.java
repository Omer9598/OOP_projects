package pepse.world.trees;

import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.awt.*;

/**
 * A class to create the fruit in the game
 */
public class Fruit extends Block implements JumpObserver {
    /**
     * The tag of the fruit
     */
    public static final String FRUIT_TAG = "fruit";
    private boolean redColor;

    /**
     * Constructor of the class
     */
    public Fruit(Vector2 topLeftCorner) {
        super(topLeftCorner, new OvalRenderable(Color.RED));
        this.setTag(FRUIT_TAG);
        this.redColor = true;
    }

    @Override
    public void onJump() {
        if (redColor) {
            redColor = false;
            renderer().setRenderable(new OvalRenderable(Color.YELLOW));
            return;
        }
        renderer().setRenderable(new OvalRenderable(Color.RED));
        redColor = true;
    }
}

package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.awt.*;

/**
 * A class to create the tree trunk
 */
public class Trunk extends Block implements JumpObserver {

    /**
     * The tag of the tree trunk
     */
    public static final String TRUNK_TAG = "trunk";
    private static final Color TREE_TRUNK_COLOR = new Color(100, 50, 20);

    /**
     * Constructor
     */
    public Trunk(Vector2 topLeftCorner) {
        super(topLeftCorner, new RectangleRenderable(ColorSupplier
                        .approximateColor(TREE_TRUNK_COLOR)));
        this.setTag(TRUNK_TAG);
    }

    /**
     * Change the tree trunks when the avatar jumps
     */
    @Override
    public void onJump() {
        RectangleRenderable newRenderable = new
                RectangleRenderable(ColorSupplier
                .approximateColor(TREE_TRUNK_COLOR));
        renderer().setRenderable(newRenderable);
    }
}

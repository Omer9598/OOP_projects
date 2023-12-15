package pepse.world.trees;

import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

public class Leaf extends Block {

    private final int lifeTime;

    public Leaf(Vector2 topLeftCorner, Renderable renderable, int lifeTime) {
        super(topLeftCorner, renderable);
        this.lifeTime = lifeTime;
    }
}

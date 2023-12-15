package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import java.awt.*;

public class Terrain {
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final float groundHeightAtX0;
    private final int seed;
    private static final int TERRAIN_DEPTH = 20 * Block.SIZE;

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);

    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.seed = seed;
        this.groundHeightAtX0 = windowDimensions.x() / 3;
    }

    /**
     * This function will determine the terrain height in a given x value,
     * returning the y value
     * The y value will be determined by the Perlin Noise function
     */
    public float groundHeightAt(float x) {
        NoiseGenerator perlinFunction = new NoiseGenerator(seed);
        float scaleFactor = 130.0f;
        float offset = 150.0f;
        return groundHeightAtX0 + scaleFactor *
                (float) perlinFunction.noise(x) + offset;
    }

    /**
     * This function will create the terrain in the game, using groundHeightAt
     * function to determine the height
     */
    public void createInRange(int minX, int maxX) {
        RectangleRenderable renderable =
                new RectangleRenderable(ColorSupplier.approximateColor
                (BASE_GROUND_COLOR));

        // Change the minX and maxX to be divided by Block.SIZE
        if(minX % Block.SIZE != 0) {
            minX = (int) Math.floor((double) minX / Block.SIZE) * Block.SIZE;
        }
        if(maxX % Block.SIZE != 0) {
            maxX = (int) Math.ceil((double) maxX / Block.SIZE) * Block.SIZE;
        }

        // Creating the terrain
        for (int xVal = minX; xVal < maxX; xVal += Block.SIZE) {
            int yVal = ((int)(groundHeightAt(xVal) / Block.SIZE)) * Block.SIZE;
            for (int depth = 0; depth < TERRAIN_DEPTH; depth++) {
                Vector2 blockPosition = new Vector2(xVal, yVal);
                Block block = new Block(blockPosition, renderable);
                gameObjects.addGameObject(block, groundLayer);
                block.setTag("ground");
                yVal += Block.SIZE;
            }
        }
    }
}

package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import java.awt.*;
import pepse.util.NoiseGenerator;

public class Terrain {
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final int seed;
    private static final float TERRAIN_DEPTH = 18;
    private static final Color BASE_GROUND_COLOR = new Color(180, 115, 50);
    private final Vector2 windowDimensions;
    public Terrain(GameObjectCollection gameObjects,
                   int groundLayer, Vector2 windowDimensions,
                   int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.seed = seed;
        this.windowDimensions = windowDimensions;
    }

    /**
     * This function will determine the terrain height in a given x value,
     * returning the y value
     * The y value will be determined by the Perlin NoiseGenerator function
     */
    public float groundHeightAt(float x) {
        NoiseGenerator perlinFunction = new NoiseGenerator(seed);
        float value = (float) (windowDimensions.y()
                -perlinFunction.noise(x / (Block.SIZE))
                * windowDimensions.y() - windowDimensions.y() / 3);
        return changeMinMaxX(value, true);
    }

    /**
     * This function will create the terrain in the game, using groundHeightAt
     * function to determine the height
     */
    public void createInRange(float minX, float maxX) {
        // Change the minX and maxX to be divided by Block.SIZE
        minX = changeMinMaxX(minX, true);
        maxX = changeMinMaxX(maxX, false);
        // Creating the terrain
        for (float xVal = minX; xVal < maxX; xVal += Block.SIZE) {
            float yVal = ((int)(groundHeightAt(xVal) / Block.SIZE)) * Block.SIZE;
            // Setting only the 2 first layers to be solid ground
            int layer = groundLayer;
            for (int depth = 0; depth < TERRAIN_DEPTH; depth++) {
                RectangleRenderable renderable =
                        new RectangleRenderable(ColorSupplier.approximateColor
                                (BASE_GROUND_COLOR));
                if(depth < 2) {
                    layer = groundLayer;
                }
                createBlock(renderable, xVal, yVal, "ground block", layer);
                yVal += Block.SIZE;
                layer = groundLayer - 1;
            }
        }
    }

    /**
     * This function will create a single ground block
     */
    public void createBlock(RectangleRenderable renderable, float xVal,
                            float yVal, String tag, int layer) {
        Vector2 blockPosition = new Vector2(xVal, yVal);
        Block block = new Block(blockPosition, renderable);
        gameObjects.addGameObject(block, layer);
        block.setTag(tag);
    }

    /**
     * This function will change minX and maxX such that they will be divided by
     * Block. SIZE
     */
    public static float changeMinMaxX(float xValue, boolean isMin) {
        float minX = (int) Math.floor(xValue / Block.SIZE) * Block.SIZE;
        float maxX = (int) Math.ceil(xValue / Block.SIZE) * Block.SIZE;
        if(isMin) {
            return minX;
        }
        return maxX;
    }
}

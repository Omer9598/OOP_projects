package pepse.world;


import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Terrain {
    private final int seed;
    private static final float TERRAIN_DEPTH = 20;
    private static final String GROUND_TAG = "ground block";
    private final float groundHeightAtX0;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    public Terrain(Vector2 windowDimensions, int seed) {
        this.seed = seed;
        this.groundHeightAtX0 = windowDimensions.y() * 2 / 3;
    }

    /**
     * This function will determine the terrain height in a given x value,
     * returning the y value
     * The y value will be determined by the Perlin NoiseGenerator function
     */
    public float groundHeightAt(float x) {
        NoiseGenerator perlinFunction = new NoiseGenerator(seed,
                (int) groundHeightAtX0);
        float noise = (float) perlinFunction.noise(x, Block.SIZE * 2);
        return groundHeightAtX0 + noise;
    }

    /**
     * This function will create the terrain in the game, using groundHeightAt
     * function to determine the height
     */
     public List<Block> createInRange(float minX, float maxX) {
         List<Block> terrain = new ArrayList<>();
         // Change the minX and maxX to be divided by Block.SIZE
         minX = changeMinMaxX(minX, true);
         maxX = changeMinMaxX(maxX, false);

         for(float xVal = minX; xVal < maxX; xVal += Block.SIZE) {
             float yVal = ((int)(groundHeightAt(xVal) / Block.SIZE))
                     * Block.SIZE;
             for(int depth = 0; depth < TERRAIN_DEPTH; depth++) {
                 RectangleRenderable renderable = new
                         RectangleRenderable(ColorSupplier.approximateColor
                                (BASE_GROUND_COLOR));
                 terrain.add(createBlock(renderable, xVal, yVal));
                 yVal += Block.SIZE;
             }
         }
         return terrain;
     }

    /**
     * This function will create a single ground block
     */
    private Block createBlock(RectangleRenderable renderable, float xVal,
                            float yVal) {
        Vector2 blockPosition = new Vector2(xVal, yVal);
        Block block = new Block(blockPosition, renderable);
        block.setTag(GROUND_TAG);
        return block;
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

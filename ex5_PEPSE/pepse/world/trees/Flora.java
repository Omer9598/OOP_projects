package pepse.world.trees;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.ArrayList;
import java.util.List;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

/**
 * A class for the trees in the game
 */
public class Flora {
    private static final Color TREE_TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAVES_COLOR = new Color(50, 200, 30);
    private static final float FRUIT_PROBABILITY = 0.9f;
    /**
     * The tag of a tree trunk
     */
    public static final String TRUNKS_TAG = "trunk";
    private final Random random = new Random();
    private final int treeHeight;
    private final Terrain terrain;

    /**
     * Class constructor
     */
    public Flora(Terrain terrain) {
        this.terrain = terrain;
        this.treeHeight = random.nextInt(4) + 5;
    }

    /**
     * This function will create trees randomly - in probability of 0.1 for each
     * x value in the range given.
     */
    public List<Block> createInRange(float minX, float maxX) {
        List<Block> trees = new ArrayList<>();
        // Change the minX and maxX to be divided by Block.SIZE
        minX = Terrain.changeMinMaxX(minX, true);
        maxX = Terrain.changeMinMaxX(maxX, false);

        for (float xVal = minX; xVal < maxX; xVal += Block.SIZE) {
            // Plant the trees pseudo-randomly
            Random randomTree = new Random(Objects.hash(xVal));
            // Plant tree in probability of 0.1
            if (randomTree.nextDouble() < 0.1) {
                float yVal = ((int) (terrain.groundHeightAt(xVal) /
                        Block.SIZE)) * Block.SIZE - Block.SIZE;
                for (int i = 0; i < treeHeight; i++) {
                    RectangleRenderable treeRenderable =
                            new RectangleRenderable(ColorSupplier
                                    .approximateColor(TREE_TRUNK_COLOR));
                    Block trunk = createTreeBlock(treeRenderable, xVal, yVal);
                    trees.add(trunk);
                    yVal -= Block.SIZE;
                }
                // Handling the tree tops
                trees.addAll(createTreeTop(xVal, yVal));
            }
        }
        return trees;
    }

    /**
     * This function will create a single fruit
     */
    private Block createFruit(float xVal, float yVal) {
        Vector2 fruitPosition = new Vector2(xVal, yVal);
        return new Fruit(fruitPosition);
    }

    /**
     * This function will create a single tree block
     */
    private Block createTreeBlock(RectangleRenderable renderable, float xVal,
                                 float yVal) {
        Vector2 blockPosition = new Vector2(xVal, yVal);
        Block block = new Block(blockPosition, renderable);
        block.setTag(TRUNKS_TAG);
        return block;
    }

    /**
     * This function will create a tree top
     * Choosing randomly from 2 to 4 to represent the size of the tree top
     * xVal and yVal are the values of the root block of the current tree
     */
    private List<Block> createTreeTop(float rootX, float rootY) {
        List<Block> treeTops = new ArrayList<>();
        int treeSize = random.nextInt(2) + 1;
        float leafBlockSize = Block.SIZE;

        for (int row = 0; row < treeSize * 2 + 1; row++) {
            float currentY = rootY - treeSize * leafBlockSize +
                    row * leafBlockSize;

            for (int col = 0; col < treeSize * 2 + 1; col++) {
                float currentX = rootX - treeSize * leafBlockSize +
                        col * leafBlockSize;
                // Create leaves in probability of 0.7 and fruit in 0.1
                if (random.nextDouble() > 0.7) {
                    if(random.nextDouble() > 0.8) {
                        Block fruit = createFruit(currentX, currentY);
                        treeTops.add(fruit);
                    }
                    continue;
                }
                RectangleRenderable leavesRenderable =
                        new RectangleRenderable(ColorSupplier.approximateColor(
                                LEAVES_COLOR));
                Vector2 leafPosition = new Vector2(currentX, currentY);
                Leaf leaf = new Leaf(leafPosition, leavesRenderable);
                treeTops.add(leaf);
            }
        }
        return treeTops;
    }
}


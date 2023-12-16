package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;

public class Tree {
    private static final Color TREE_TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAVES_COLOR = new Color(30, 180, 65);
    private static final int TREE_HEIGHT = 10;
    private final Terrain terrain;
    private final GameObjectCollection gameObjects;
    private final int treesLayer;
    private final int leavesLayer;

    public Tree(Terrain terrain, GameObjectCollection gameObjects,
                int treesLayer, int leavesLayer) {
        this.terrain = terrain;
        this.gameObjects = gameObjects;
        this.treesLayer = treesLayer;
        this.leavesLayer = leavesLayer;
    }

    /**
     * This function will create trees randomly - in probability of 0.1 for each
     * x value in the range given.
     */
    public void createInRange(float minX, float maxX) {
        // Change the minX and maxX to be divided by Block.SIZE
        minX = Terrain.changeMinMaxX(minX, true);
        maxX = Terrain.changeMinMaxX(maxX, false);

        RectangleRenderable treeRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(
                        TREE_TRUNK_COLOR));
        RectangleRenderable leavesRenderable =
                new RectangleRenderable(ColorSupplier.approximateColor(
                        LEAVES_COLOR));

        Random random = new Random();
        for (float xVal = minX; xVal < maxX; xVal += Block.SIZE) {
            // Plant tree in probability of 0.1
            if (random.nextDouble() < 0.1) {
                float yVal = ((int) (terrain.groundHeightAt(xVal) / Block.SIZE)) *
                        Block.SIZE - Block.SIZE;
                for (int i = 0; i < TREE_HEIGHT; i++) {
                    terrain.createBlock(treeRenderable, xVal, yVal,
                            "tree block", treesLayer);
                    yVal -= Block.SIZE;
                }
                // Handling the tree tops
                createTreeTop(leavesRenderable, xVal, yVal);
            }
        }
    }

    /**
     * This function will create a tree top
     * Choosing randomly from 2 to 4 to represent the size of the tree top
     * xVal and yVal are the values of the root block of the current tree
     */
    private void createTreeTop(RectangleRenderable rectangleRenderable,
                               float rootX, float rootY) {
        Random random = new Random();
        int treeSize = random.nextInt(2) + 1;
        float leafBlockSize = Block.SIZE;

        for (int row = 0; row < treeSize * 2 + 1; row++) {
            float currentY = rootY - treeSize * leafBlockSize +
                    row * leafBlockSize;

            for (int col = 0; col < treeSize * 2 + 1; col++) {
                float currentX = rootX - treeSize * leafBlockSize +
                        col * leafBlockSize;
                // Create leaves in probability of 0.8
                if (random.nextDouble() > 0.8) {
                    continue;
                }
                Vector2 leafPosition = new Vector2(currentX, currentY);
                Leaf leaf = new Leaf(leafPosition, rectangleRenderable);
                gameObjects.addGameObject(leaf, leavesLayer);
            }
        }
    }
}


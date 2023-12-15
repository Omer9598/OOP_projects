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
    private static final Color LEAVES_COLOR = new Color(50, 180, 30);
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
    public void createInRange(int minX, int maxX) {
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
        for (int xVal = minX; xVal < maxX; xVal += Terrain.TERRAIN_JUMP) {
            // Plant tree in probability of 0.1
            if (random.nextDouble() < 0.1) {
                int yVal = ((int) (terrain.groundHeightAt(xVal) / Block.SIZE)) *
                        Block.SIZE - Block.SIZE;
                for (int i = 0; i < TREE_HEIGHT; i++) {
                    terrain.createBlock(treeRenderable, xVal, yVal,
                            "tree block", treesLayer);
                    yVal -= Terrain.TERRAIN_JUMP;
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
                               int rootX, int rootY) {
        Random random = new Random();
        int treeSize = random.nextInt(2) + 2;
        int leafBlockSize = Block.SIZE;

        for (int row = 0, currentY = rootY - treeSize * leafBlockSize;
             row < treeSize * 2 + 1;
             row++, currentY += leafBlockSize) {
            for (int col = 0, currentX = rootX - treeSize * leafBlockSize;
                 col < treeSize * 2 + 1;
                 col++, currentX += leafBlockSize) {
                // Create leaves in probability of 0.6
                if(random.nextDouble() > 0.8) {
                    continue;
                }
                Vector2 leafPosition = new Vector2(currentX, currentY);
                Leaf leaf = new Leaf(leafPosition, rectangleRenderable, 20);
                gameObjects.addGameObject(leaf, leavesLayer);
                leaf.setTag("leaf");
            }
        }
    }
}


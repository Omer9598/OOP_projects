package pepse.world.trees;

import pepse.world.Block;
import pepse.world.Terrain;

public class Tree {

    /**
     * This function will create trees randomly - in probability of 0.1 for each
     * x value in the range given.
     */
    public void createInRange(int minX, int maxX) {
        // Change the minX and maxX to be divided by Block.SIZE
        Terrain.changeMinMaxX(minX, true);
        Terrain.changeMinMaxX(maxX, false);

    }
}

package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

/**
 * A class for the double-behaviour strategy
 */
public class DoubledStrategy implements CollisionStrategy {

    private final CollisionStrategy firstStrategy;
    private final CollisionStrategy secondStrategy;
    private final Counter brickCounter;

    /**
     * Class constructor
     */
    public DoubledStrategy(CollisionStrategy firstStrategy,
                           CollisionStrategy secondStrategy,
                           Counter brickCounter) {

        this.firstStrategy = firstStrategy;
        this.secondStrategy = secondStrategy;
        this.brickCounter = brickCounter;
    }

    /**
     * Creating 2 random behaviours when the brick being hit
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        firstStrategy.onCollision(thisObj, otherObj);
        secondStrategy.onCollision(thisObj, otherObj);
        // brickCounter decremented twice due to doubled behavior
        brickCounter.increment();
    }
}

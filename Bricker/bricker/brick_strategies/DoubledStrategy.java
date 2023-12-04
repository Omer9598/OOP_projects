package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

public class DoubledStrategy implements CollisionStrategy {

    private final CollisionStrategy firstStrategy;
    private final CollisionStrategy secondStrategy;
    private final Counter brickCounter;

    public DoubledStrategy(CollisionStrategy firstStrategy,
                           CollisionStrategy secondStrategy,
                           Counter brickCounter) {

        this.firstStrategy = firstStrategy;
        this.secondStrategy = secondStrategy;
        this.brickCounter = brickCounter;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        firstStrategy.onCollision(thisObj, otherObj);
        secondStrategy.onCollision(thisObj, otherObj);
        // brickCounter decremented twice due to doubled behavior
        brickCounter.increment();
    }
}

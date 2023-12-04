package bricker.brick_strategies;

import danogl.GameObject;
import danogl.util.Counter;

public class TripledStrategy extends DoubledStrategy{

    private final CollisionStrategy thirdStrategy;
    private final Counter brickCounter;

    public TripledStrategy(CollisionStrategy firstStrategy,
                           CollisionStrategy secondStrategy,
                           CollisionStrategy thirdStrategy,
                           Counter brickCounter) {
        super(firstStrategy, secondStrategy, brickCounter);
        this.thirdStrategy = thirdStrategy;
        this.brickCounter = brickCounter;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        thirdStrategy.onCollision(thisObj, otherObj);
        brickCounter.increment();
    }
}

package bricker.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class BrickStrategyFactory {

    private final GameObjectCollection gameObjects;
    private final Counter brickCounter;

    public BrickStrategyFactory(GameObjectCollection gameObjects,
                                Counter brickCounter) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
    }

    /**
     * Choose randomly between the possible brick strategies
     */
    public CollisionStrategy getStrategy() {
        return new RemoveBrickStrategy(gameObjects, brickCounter);
    }
}

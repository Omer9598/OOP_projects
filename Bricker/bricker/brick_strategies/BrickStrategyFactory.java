package bricker.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;

import java.util.Random;

public class BrickStrategyFactory {

    private final GameObjectCollection gameObjects;
    private final Counter brickCounter;
    private final Renderable renderable;
    private final Sound collisionSound;

    public BrickStrategyFactory(GameObjectCollection gameObjects,
                                Counter brickCounter, Renderable renderable,
                                Sound collisionSound) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
        this.renderable = renderable;
        this.collisionSound = collisionSound;
    }

    /**
     * Choose randomly between the possible brick strategies.
     * There atr 6 different strategies:
     * 1. Regular - brick disappears
     * 2. Mock balls added
     * 3. Extra paddle
     * 4. Display change (camera)
     * 5. Life added
     * 6. Doubled strategy
     */
    public CollisionStrategy getStrategy() {
        Random random = new Random();
        int randomStrategy = random.nextInt(6);
        CollisionStrategy collisionStrategy = null;

        switch (randomStrategy) {
            case 0 -> collisionStrategy =
                    new RemoveBrickStrategy(gameObjects, brickCounter);
            case 1 -> collisionStrategy =
                    new CreateBallsStrategy(gameObjects, renderable,
                            collisionSound, brickCounter);
            default -> collisionStrategy =
                    new RemoveBrickStrategy(gameObjects, brickCounter);

//            case 2 -> collisionStrategy = null;
//            case 3 -> collisionStrategy = null;
//            case 4 -> collisionStrategy = null;
//            case 5 -> collisionStrategy = null;
        }
        return collisionStrategy;
    }
}

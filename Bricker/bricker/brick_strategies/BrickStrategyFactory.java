package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

public class BrickStrategyFactory {

    private final GameObjectCollection gameObjects;
    private final Counter brickCounter;
    private final Counter paddleCounter;
    private final ImageReader imageReader;
    private final SoundReader soundReader;
    private final Vector2 windowDimensions;
    private final UserInputListener userInputListener;
    private final Ball ball;
    private final BrickerGameManager brickerGameManager;
    private final Counter livesCounter;
    private final Vector2 heartDimensions;

    public BrickStrategyFactory(GameObjectCollection gameObjects,
                                Counter brickCounter, ImageReader imageReader,
                                SoundReader soundReader,
                                UserInputListener userInputListener,
                                Vector2 windowDimensions, Ball ball,
                                BrickerGameManager brickerGameManager,
                                Counter livesCounter, Vector2 heartDimensions) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.userInputListener = userInputListener;
        this.windowDimensions = windowDimensions;
        this.ball = ball;
        this.brickerGameManager = brickerGameManager;
        this.livesCounter = livesCounter;
        this.heartDimensions = heartDimensions;
        this.paddleCounter = new Counter(0);
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
        int randomStrategy = random.nextInt(5);
        CollisionStrategy collisionStrategy;

        switch (randomStrategy) {
            case 0 -> collisionStrategy =
                    new RemoveBrickStrategy(gameObjects, brickCounter);
            case 1 -> collisionStrategy = new CreateBallsStrategy(gameObjects,
                    brickCounter, imageReader, soundReader);
            case 2 -> collisionStrategy =
                    new CreateSecondPaddle(gameObjects, brickCounter,
                            gameObjects, imageReader, userInputListener,
                            windowDimensions, paddleCounter);
            case 3 -> collisionStrategy = new CameraChangeStrategy(gameObjects,
                    ball, brickerGameManager, brickCounter);
            case 4 -> collisionStrategy = new ExtraLifeStrategy(gameObjects,
                    brickCounter, livesCounter, heartDimensions, imageReader);
//            case 5 -> collisionStrategy = null;
            default -> collisionStrategy =
                    new RemoveBrickStrategy(gameObjects, brickCounter);
        }
        return collisionStrategy;
    }
}

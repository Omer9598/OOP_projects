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

/**
 * A factory to build brick strategies
 */
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
    private final CollisionStrategy[] collisionStrategiesArr;
    private final int MOCK_BALLS = 0;
    private final int SECOND_PADDLE = 1;
    private final int CAMERA_CHANGE = 2;
    private final int EXTRA_LIFE = 3;
    private static final int DOUBLED_BEHAVIOR = 4;
    private final int REGULAR_STRATEGY = 5;

    /**
     * Factory's constructor
     */
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
        this.collisionStrategiesArr = new CollisionStrategy[6];
        createStrategyArr();
    }

    /**
     * This function will create the collision strategy array to choose from
     * randomly
     */
    private void createStrategyArr() {
        collisionStrategiesArr[MOCK_BALLS] =
                new CreateBallsStrategy(gameObjects, brickCounter,
                        imageReader, soundReader);
        collisionStrategiesArr[SECOND_PADDLE] =
                new CreateSecondPaddle(gameObjects, brickCounter,
                        gameObjects, imageReader, userInputListener,
                        windowDimensions, paddleCounter);
        collisionStrategiesArr[CAMERA_CHANGE] =
                new CameraChangeStrategy(gameObjects, ball,
                        brickerGameManager, brickCounter);
        collisionStrategiesArr[EXTRA_LIFE] =
                new ExtraLifeStrategy(gameObjects, brickCounter,
                        livesCounter, heartDimensions, imageReader);
        collisionStrategiesArr[REGULAR_STRATEGY] =
                new BasicCollisionStrategy(gameObjects, brickCounter);
    }

    /**
     * Choose randomly between the possible brick strategies.
     * There are 6 different strategies:
     * 1. Regular - brick disappears
     * 2. Mock balls added
     * 3. Extra paddle
     * 4. Display change (camera)
     * 5. Life added
     * 6. Doubled strategy
     * each brick has a maximum of 3 strategies, including doubled strategy,
     * that is why this function can be called only twice
     */
    public CollisionStrategy getStrategy() {
        Random random = new Random();
        int randomStrategy = random.nextInt(10);
        switch (randomStrategy) {
            case MOCK_BALLS, SECOND_PADDLE, CAMERA_CHANGE, EXTRA_LIFE -> {
                return getSpecialStrategy(randomStrategy);
            }
            case DOUBLED_BEHAVIOR -> {
                // 2 new random numbers - not including regular behavior
                int firstRandom = random.nextInt(5);
                int secondRandom = random.nextInt(5);
                if(firstRandom == DOUBLED_BEHAVIOR ||
                        secondRandom == DOUBLED_BEHAVIOR) {
                    return getTripledStrategy();
                }
                CollisionStrategy firstStrategy =
                        getSpecialStrategy(firstRandom);
                CollisionStrategy secondStrategy =
                        getSpecialStrategy(secondRandom);
                return new DoubledStrategy(firstStrategy, secondStrategy,
                        brickCounter);
            }
            // Probability of 0.5 for regular behaviour
            default -> {
                return collisionStrategiesArr[REGULAR_STRATEGY];
            }
        }
    }

    /**
     * This function will return a triple strategy, in case that one of the
     * strategies in a doubled strategy is a doubled strategy
     */
    private CollisionStrategy getTripledStrategy() {
        Random random = new Random();
        int firstRandom = random.nextInt(4);
        int secondRandom = random.nextInt(4);
        int thirdRandom = random.nextInt(4);
        CollisionStrategy firstStrategy =
                getSpecialStrategy(firstRandom);
        CollisionStrategy secondStrategy =
                getSpecialStrategy(secondRandom);
        CollisionStrategy thirdStrategy =
                getSpecialStrategy(thirdRandom);
        return new TripledStrategy(firstStrategy, secondStrategy, thirdStrategy,
                brickCounter);
    }

    /**
     * This function will return one of the special collision strategies, not
     * including the regular strategy
     * To be called to choose a strategy, and for the doubled strategy
     */
    private CollisionStrategy getSpecialStrategy(int randomNum) {
        switch (randomNum) {
            case MOCK_BALLS -> {
                return collisionStrategiesArr[MOCK_BALLS];
            }
            case SECOND_PADDLE -> {
                return collisionStrategiesArr[SECOND_PADDLE];
            }
            case CAMERA_CHANGE -> {
                return collisionStrategiesArr[CAMERA_CHANGE];
            }
            case EXTRA_LIFE -> {
                return collisionStrategiesArr[EXTRA_LIFE];
            }
            default -> {
                return null;
            }
        }
    }
}

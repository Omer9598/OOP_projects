package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.SecondPaddle;

/**
 * A class that creates a second paddle
 */
public class CreateSecondPaddle extends BasicCollisionStrategy
        implements CollisionStrategy{
    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private final UserInputListener userInputListener;
    private final Vector2 windowDimensions;
    private final  Counter paddleCounter;
    private static final String PADDLE = "assets/paddle.png";
    private static final Vector2 PADDLE_DIMENSIONS = new Vector2(100, 15);

    /**
     * Class constructor
     */
    public CreateSecondPaddle(GameObjectCollection gameObjects,
                              Counter brickCounter,
                              GameObjectCollection gameObjects1,
                              ImageReader imageReader,
                              UserInputListener userInputListener,
                              Vector2 windowDimensions, Counter paddleCounter) {
        super(gameObjects, brickCounter);
        this.gameObjects = gameObjects1;
        this.imageReader = imageReader;
        this.userInputListener = userInputListener;
        this.windowDimensions = windowDimensions;
        this.paddleCounter = paddleCounter;
    }

    /**
     * This function extends the function in RemoveBrickStrategy, to delete the
     * hitted brick and then creating a second paddle
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Deleting the brick
        super.onCollision(thisObj, otherObj);
        // Checking if there is a second paddle already
        if (paddleCounter.value() >= 1)
        {
            // Fixing a bug that causes paddleCounter == 2
            paddleCounter.reset();
            paddleCounter.increment();
            return;
        }
        // creating a new paddle at the center of the screen
        Vector2 topLeft = new Vector2(windowDimensions.x() / 2 -
                PADDLE_DIMENSIONS.x() / 2,
                windowDimensions.y() / 2 -
                PADDLE_DIMENSIONS.y() / 2);
        GameObject middlePaddle = new SecondPaddle(topLeft, PADDLE_DIMENSIONS,
                imageReader.readImage(PADDLE, false),
                userInputListener, windowDimensions, paddleCounter,
                new Counter(4),
                gameObjects);
        paddleCounter.increaseBy(1);
        gameObjects.addGameObject(middlePaddle);
    }
}

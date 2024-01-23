package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.game_objects.SecondPaddle;

/**
 * A class that creates a second paddle
 */
public class CreateSecondPaddle extends RemoveBrickStrategy
        implements CollisionStrategy{
    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private final UserInputListener userInputListener;
    private final Vector2 windowDimensions;
    private final  Counter paddleCounter;
    private final Counter numOfHits;

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
        this.numOfHits = new Counter(3);
    }

    /**
     * This function extends the function in RemoveBrickStrategy, to delete the
     * hitted brick and then creating a second paddle
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // deleting the brick
        super.onCollision(thisObj, otherObj);
        // checking if there is a second paddle already
        if (paddleCounter.value() >= 1)
        {
            // Fixing a bug that causes paddleCounter == 2
            paddleCounter.reset();
            paddleCounter.increment();
            return;
        }
        // creating a new paddle at the center of the screen
        Vector2 topLeft = new Vector2(windowDimensions.x() / 2 - 50,
                windowDimensions.y() / 2 - 7);
        Vector2 dimensions = new Vector2(100, 15);
        GameObject middlePaddle = new SecondPaddle(topLeft, dimensions,
                imageReader.readImage("assets/botGood.png", false),
                userInputListener, windowDimensions, paddleCounter, numOfHits,
                gameObjects);
        paddleCounter.increaseBy(1);
        gameObjects.addGameObject(middlePaddle);
    }
}

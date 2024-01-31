package bricker.brick_strategies;

import bricker.gameobjects.WeightedHeart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class for the extra life strategy
 */
public class ExtraLifeStrategy extends BasicCollisionStrategy
        implements CollisionStrategy{
    private final Counter livesCounter;
    private final Vector2 dimensions;
    private final ImageReader imageReader;
    private final GameObjectCollection gameObjects;
    private static final String heartPath = "assets/heart.png";

    /**
     * Class constructor
     */
    public ExtraLifeStrategy(GameObjectCollection gameObjects,
                             Counter brickCounter, Counter livesCounter,
                             Vector2 dimensions, ImageReader imageReader) {
        super(gameObjects, brickCounter);
        this.livesCounter = livesCounter;
        this.dimensions = dimensions;
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
    }

    /**
     * Adds a life for the player when this brick is hitted
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Deleting brick
        super.onCollision(thisObj, otherObj);
        float xTopLeftCorner = thisObj.getCenter().x() - dimensions.x() / 2;
        Vector2 heartTopLeftCorner = new Vector2(xTopLeftCorner,
                thisObj.getTopLeftCorner().y());
        // Creating a graphic heart
        GameObject fallingHeart = new WeightedHeart(heartTopLeftCorner,
                dimensions,
                imageReader.readImage(heartPath, true),
                livesCounter, gameObjects);
        gameObjects.addGameObject(fallingHeart);
    }
}

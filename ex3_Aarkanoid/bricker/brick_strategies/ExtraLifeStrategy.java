package bricker.brick_strategies;

import bricker.gameobjects.WeightedHeart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.util.Counter;
import danogl.util.Vector2;

public class ExtraLifeStrategy extends RemoveBrickStrategy
        implements CollisionStrategy{

    private final Counter livesCounter;
    private final Vector2 dimensions;
    private final ImageReader imageReader;
    private final GameObjectCollection gameObjects;


    public ExtraLifeStrategy(GameObjectCollection gameObjects,
                             Counter brickCounter, Counter livesCounter,
                             Vector2 dimensions, ImageReader imageReader) {
        super(gameObjects, brickCounter);
        this.livesCounter = livesCounter;
        this.dimensions = dimensions;
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
    }

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
                imageReader.readImage("assets/heart.png", true),
                livesCounter, gameObjects);
        gameObjects.addGameObject(fallingHeart);
    }
}

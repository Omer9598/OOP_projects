package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * A class to remove a brick
 */
public class BasicCollisionStrategy implements CollisionStrategy{
    private final GameObjectCollection gameObjects;
    private final Counter brickCounter;

    public BasicCollisionStrategy(GameObjectCollection gameObjects,
                                  Counter brickCounter) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
    }

    /**
     * This function will delete otherObj parameter when called
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // removing the brick when being hit by the ball
        gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS);
        brickCounter.decrement();
    }
}



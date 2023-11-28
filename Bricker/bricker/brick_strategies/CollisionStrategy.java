package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

public class CollisionStrategy {
    private final GameObjectCollection gameObjects;
    private final Counter brickCounter;

    public CollisionStrategy(GameObjectCollection gameObjects,
                             Counter brickCounter) {
        this.gameObjects = gameObjects;
        this.brickCounter = brickCounter;
    }
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // removing the brick when being hit by the ball
        gameObjects.removeGameObject(thisObj, Layer.STATIC_OBJECTS);
        brickCounter.decrement();
    }
}

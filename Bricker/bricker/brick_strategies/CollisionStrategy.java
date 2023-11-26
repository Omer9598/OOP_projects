package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

public class CollisionStrategy {
    private final GameObjectCollection gameObjects;

    public CollisionStrategy(GameObjectCollection gameObjects) {
        this.gameObjects = gameObjects;
    }
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // removing the brick when being hit by the ball
        gameObjects.removeGameObject(thisObj);
    }
}

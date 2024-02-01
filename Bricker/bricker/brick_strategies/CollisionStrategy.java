package bricker.brick_strategies;

import danogl.GameObject;

/**
 * An interface for the collision strategy
 */
public interface CollisionStrategy {
    /**
     * What to do in case of collision with the brick
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}

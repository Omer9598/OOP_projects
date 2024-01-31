package bricker.brick_strategies;

import danogl.GameObject;

/**
 * An interface for the collision strategy
 */
public interface CollisionStrategy {
    void onCollision(GameObject thisObj, GameObject otherObj);
}

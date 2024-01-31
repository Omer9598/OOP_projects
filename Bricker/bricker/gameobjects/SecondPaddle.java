package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class for the second (middle paddle)
 */
public class SecondPaddle extends Paddle implements CollisionStrategy {
    private final Counter numOfHits;
    private final Counter paddleCounter;
    private final GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates
     *                         (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null,
     *                         in which case the GameObject will not be rendered.
     * @param inputListener    Object to get input from the user to move the paddle
     * @param windowDimensions The dimensions of the game window
     * @param paddleCounter    A counter to check if there is a second paddle
     *                         already
     * @param numOfHits        Number of hits to the second paddle
     */
    public SecondPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                        Renderable renderable, UserInputListener inputListener,
                        Vector2 windowDimensions,
                        Counter paddleCounter, Counter numOfHits,
                        GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener,
                windowDimensions);
        this.paddleCounter = paddleCounter;
        this.numOfHits = numOfHits;
        this.gameObjects = gameObjects;
    }

    /**
     * Update the second paddle if needed
     *
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by
     *                  multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        // Second paddle moves the same as the regular paddle
        super.update(deltaTime);
    }

    /**
     * On collision do something
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped
     *                  (collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        onCollision(this, other);
    }

    /**
     * Decrementing the number of hits
     * Deleting the second paddle if necessary
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        numOfHits.decrement();
        if (numOfHits.value() <= 0) {
            gameObjects.removeGameObject(this);
            paddleCounter.decrement();
        }
    }
}

package bricker.game_objects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * A class to create the game ball
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private final Counter collisionCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates
     *                     (pixels).
     *                      Note that (0,0) is the top-left corner of the
     *                      window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null,
     *                     in which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = new Counter(0);
    }

    /**
     * What to do when the ball collides with something
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(
     *                  collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // Setting the new velocity after collision
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionSound.play();
        if(collisionCounter.value() > 0) {collisionCounter.decrement();}
    }

    /**
     * This function will set the ball in a random diagonal direction
     */
    public void setBallRandomDirection() {
        // setting the ball's velocity - start in a random direction
        float ballSpeed = 250;
        float ballVelX = ballSpeed;
        float ballVelY = ballSpeed;
        Random random = new Random();
        if (random.nextBoolean()) {
            ballVelX *= -1;
        }
        if (random.nextBoolean()) {
            ballVelY *= -1;
        }
        this.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     * Setting the ball collision counter
     */
    public void setCollisionCounter(int i) {collisionCounter.increaseBy(i);}

    /**
     * Return the collision counter
     */
    public int getCollisionCounter() {return collisionCounter.value();}
}

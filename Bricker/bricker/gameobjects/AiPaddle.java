package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * A class for the AI paddle
 */
public class AiPaddle extends GameObject {
    private static final float PADDLE_SPEED = 300;
    private final GameObject objectToFollow;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public AiPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                    Renderable renderable, GameObject objectToFollow) {
        super(topLeftCorner, dimensions, renderable);
        this.objectToFollow = objectToFollow;
    }

    /**
     * Update the paddle's movement according to the ball
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
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (objectToFollow.getCenter().x() < getCenter().x()) {
            movementDir = Vector2.LEFT;
        }
        if (objectToFollow.getCenter().x() > getCenter().x()) {
            movementDir = Vector2.RIGHT;
        }
        setVelocity(movementDir.mult(PADDLE_SPEED));
    }
}

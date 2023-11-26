package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class UserPaddle extends GameObject {
    private static final float PADDLE_MOVEMENT_SPEED = 400;
    private static final float MIN_DISTANCE_FROM_SCREEN_EDGE = 3;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;

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
     */
    public UserPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                      Renderable renderable, UserInputListener inputListener,
                      Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    @Override
    public void update(float deltaTime) {
        // deltaTime is the time that elapsed from the last frame
        super.update(deltaTime);
        // handling left and right key presses - moving the paddle
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(PADDLE_MOVEMENT_SPEED));

        // checking that the paddle doesn't go out of the screen
        if (MIN_DISTANCE_FROM_SCREEN_EDGE > getTopLeftCorner().x()) {
            // setting the paddle to the left border
            setTopLeftCorner(new Vector2(MIN_DISTANCE_FROM_SCREEN_EDGE,
                    windowDimensions.y() - 38));
        }
        if (windowDimensions.x() - MIN_DISTANCE_FROM_SCREEN_EDGE -
                getDimensions().x() < getTopLeftCorner().x()) {
            // setting the paddle to the right border
            setTopLeftCorner(new Vector2(windowDimensions.x() -
                    MIN_DISTANCE_FROM_SCREEN_EDGE -
                    getDimensions().x(),
                    windowDimensions.y() - 38));
        }
    }
}

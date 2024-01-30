package bricker.brick_strategies;

import bricker.game_objects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class for camera change strategy
 */
public class CameraChangeStrategy extends RemoveBrickStrategy
        implements CollisionStrategy{
    private final Ball ball;
    private final BrickerGameManager brickerGameManager;

    /**
     * Camera change strategy constructor
     */
    public CameraChangeStrategy(GameObjectCollection gameObjects, Ball ball,
                                BrickerGameManager brickerGameManager,
                                Counter brickCounter) {
        super(gameObjects, brickCounter);
        this.ball = ball;
        this.brickerGameManager = brickerGameManager;
    }

    /**
     * Updating the camera when a ball hit the camera change brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Deleting the brick hitted first
        super.onCollision(thisObj, otherObj);
        // Check if the main ball hitted the brick
        if (!(otherObj instanceof Ball)) {
            return;
        }
        // Checking current camera
        if (brickerGameManager.camera() != null) {
            return;
        }
        // Collision counter to monitor collisions (one for the brick collision)
        ball.setCollisionCounter(5);
        // Changing the camera view to the ball
        brickerGameManager.setCamera(new Camera(ball, Vector2.ZERO,
                brickerGameManager.getWindowDimensions().mult(1.2f),
                brickerGameManager.getWindowDimensions()));
    }
}

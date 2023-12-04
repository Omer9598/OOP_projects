package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

public class CameraChangeStrategy extends RemoveBrickStrategy
        implements CollisionStrategy{

    private final Ball ball;
    private final BrickerGameManager brickerGameManager;

    public CameraChangeStrategy(GameObjectCollection gameObjects, Ball ball,
                                BrickerGameManager brickerGameManager,
                                Counter brickCounter) {
        super(gameObjects, brickCounter);
        this.ball = ball;
        this.brickerGameManager = brickerGameManager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // deleting the brick hitted first
        super.onCollision(thisObj, otherObj);
        // checking current camera
        if (brickerGameManager.camera() != null) {
            return;
        }
        // collision counter to monitor collisions (one for the brick collision)
        ball.setCollisionCounter(5);
        // changing the camera view to the ball
        brickerGameManager.setCamera(new Camera(ball, Vector2.ZERO,
                brickerGameManager.getWindowDimensions().mult(1.2f),
                brickerGameManager.getWindowDimensions()));
    }
}

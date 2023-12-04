package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * This strategy will create 3 mock balls when a certain brick is collided
 */
public class CreateBallsStrategy extends RemoveBrickStrategy
        implements CollisionStrategy {

    private final GameObjectCollection gameObjects;
    private final ImageReader imageReader;
    private final SoundReader soundReader;

    public CreateBallsStrategy(GameObjectCollection gameObjects,
                               Counter brickCounter, ImageReader imageReader,
                               SoundReader soundReader) {
        super(gameObjects, brickCounter);
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
    }


    /**
     * This function will create the 3 mock balls instead of the brick
     * @param thisObj A ball that hits the brick
     * @param otherObj The brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Deleting brick
        super.onCollision(thisObj, otherObj);
        float brickWidth = otherObj.getDimensions().x();
        float ballSize = brickWidth / 3;
        // creating 3 mock balls
        for (int i = 0; i < 3; i++) {
            float xTopLeft = i * ballSize + otherObj.getTopLeftCorner().x();
            float yTopLeft = otherObj.getTopLeftCorner().y();
            Vector2 ballTopLeftCorner = new Vector2(xTopLeft, yTopLeft);
            Vector2 ballDimensions = new Vector2(15, 15);
            Renderable ballImage = imageReader.readImage(
                    "assets/mockBall.png", true);
            Sound collisionSound = soundReader.readSound(
                    "assets/blop_cut_silenced.wav");
            Ball mockBall = new Ball(ballTopLeftCorner, ballDimensions,
                    ballImage, collisionSound);
            mockBall.setBallRandomDirection();
            gameObjects.addGameObject(mockBall);
        }
    }
}

package bricker.brick_strategies;

import bricker.gameobjects.Paddle;
import bricker.gameobjects.SecondPaddle;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class WeightedHeart extends GameObject {

    private final Counter livesCounter;
    private final GameObjectCollection gameObjects;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param livesCounter  Counter to the number of lives in the game
     */
    public WeightedHeart(Vector2 topLeftCorner, Vector2 dimensions,
                         Renderable renderable, Counter livesCounter, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable);
        this.livesCounter = livesCounter;
        this.gameObjects = gameObjects;
        // making the heart fall
        this.setVelocity(new Vector2(0, 100));
    }

    /**
     * Making sure that the heart collides with the regular paddle only
     * @param other Other game object (mock ball, mock paddle, etc)
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        super.shouldCollideWith(other);
        return other instanceof Paddle && !(other instanceof SecondPaddle);
    }

    /**
     * Called only when a heart collides with the regular paddle
     * The collision causing the heart to disappear and add an extra life if
     * the number of lives is smaller then 4
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(livesCounter.value() < 4) {
            livesCounter.increment();
        }
        gameObjects.removeGameObject(this);
    }

    //    @Override
//    public void update(float deltaTime) {
//        super.update(deltaTime);
//        // deleting the heart if it left the screen
//        if(this.getCenter().y() > dimensions.y()) {
//
//        }
//    }
}

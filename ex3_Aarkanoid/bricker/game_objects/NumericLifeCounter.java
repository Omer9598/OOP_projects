package bricker.game_objects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class for the numeric life counter
 */
public class NumericLifeCounter extends GameObject {
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;
    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private GameObject textGameObject;
    private static final String counterFormat = "Lives: %d";

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public NumericLifeCounter(Vector2 topLeftCorner, Vector2 dimensions,
                              Counter livesCounter,
                              GameObjectCollection gameObjectCollection) {
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.gameObjectCollection = gameObjectCollection;
        this.numOfLives = livesCounter.value();
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        // creating the initial lives counter gameObject
        TextRenderable numericLives = new
                TextRenderable(String.format(counterFormat, numOfLives));
        this.textGameObject = new GameObject(topLeftCorner, dimensions,
                numericLives);
        gameObjectCollection.addGameObject(textGameObject, Layer.BACKGROUND);
    }

    /**
     * Update the numeric life counter according to the game
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
        if (livesCounter.value() != numOfLives) {
            // removing the old number of lives and creating the new
            gameObjectCollection.removeGameObject(textGameObject,
                    Layer.BACKGROUND);
            TextRenderable numericLives = new TextRenderable(
                    String.format(counterFormat, livesCounter.value()));
            textGameObject = new GameObject(topLeftCorner, dimensions,
                    numericLives);
            gameObjectCollection.addGameObject(textGameObject, Layer.BACKGROUND);
            numOfLives = livesCounter.value();
        }
    }
}

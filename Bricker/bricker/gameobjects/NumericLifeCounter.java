package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class NumericLifeCounter extends GameObject {

    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private final int numOfLives;
    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private GameObject textGameObject;

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
                TextRenderable(String.format("Lives: %d", numOfLives));
        this.textGameObject = new GameObject(topLeftCorner, dimensions,
                numericLives);
        gameObjectCollection.addGameObject(textGameObject, Layer.BACKGROUND);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (livesCounter.value() < numOfLives) {
            // removing the old number of lives and creating the new
            gameObjectCollection.removeGameObject(textGameObject,
                    Layer.BACKGROUND);
            TextRenderable numericLives = new TextRenderable(
                    String.format("Lives: %d", livesCounter.value()));
            textGameObject = new GameObject(topLeftCorner, dimensions,
                    numericLives);
            gameObjectCollection.addGameObject(textGameObject, Layer.BACKGROUND);
        }
    }
}

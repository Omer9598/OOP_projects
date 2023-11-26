package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {

    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;
    private GameObject[] heartsArr;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner         Position of the object, in window coordinates (pixels).
     *                              Note that (0,0) is the top-left corner of the window.
     * @param dimensions            Width and height in window coordinates.
     * @param renderable            The renderable representing the object. Can be null, in which case
     *                              the GameObject will not be rendered.
     */
    public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions,
                              Renderable renderable, Counter livesCounter,
                              GameObjectCollection gameObjectCollection,
                              int numOfLives) {
        super(topLeftCorner, dimensions, renderable);
        this.livesCounter = livesCounter;
        this.livesCounter.increaseBy(numOfLives);
        this.gameObjectCollection = gameObjectCollection;
        this.numOfLives = numOfLives;
        this.heartsArr = new GameObject[numOfLives];

        float xCord = topLeftCorner.x();
        float yCord = topLeftCorner.y();

        // creating the initial number of lives
        for (int i = 0; i < numOfLives; i++) {
            GameObject heart = new GameObject(
                    new Vector2(xCord, yCord),
                    dimensions, renderable);
            xCord += dimensions.x() + 2;
            gameObjectCollection.addGameObject(heart, Layer.STATIC_OBJECTS);
            heartsArr[i] = heart;
        }
    }


    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (numOfLives > livesCounter.value()) {
            // deleting one of the hearts and updating the number of lives
            gameObjectCollection.removeGameObject(
                    heartsArr[livesCounter.value()], Layer.STATIC_OBJECTS);
        }
    }
}

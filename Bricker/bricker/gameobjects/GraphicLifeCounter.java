package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class for the graphic life counter
 */
public class GraphicLifeCounter extends GameObject {
    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private final Renderable renderable;
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectCollection;
    private int numOfLives;
    private final GameObject[] heartsArr;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window
     *                      coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of
     *                      the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object.
     *                      Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public GraphicLifeCounter(Vector2 topLeftCorner, Vector2 dimensions,
                              Renderable renderable, Counter livesCounter,
                              GameObjectCollection gameObjectCollection,
                              int numOfLives) {
        super(topLeftCorner, dimensions, renderable);
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.renderable = renderable;
        this.livesCounter = livesCounter;
        this.livesCounter.increaseBy(numOfLives);
        this.gameObjectCollection = gameObjectCollection;
        this.numOfLives = numOfLives;
        this.heartsArr = new GameObject[4];
        createHeartsArr();
    }

    /**
     * This function will create the hearts array, containing 3 heart instances
     * at the beginning of each game
     */
    private void createHeartsArr() {
        float xCord = topLeftCorner.x();
        float yCord = topLeftCorner.y();

        // Creating the initial number of lives
        for (int i = 0; i < numOfLives; i++) {
            GameObject heart = createHeart(dimensions,
                    renderable, xCord, yCord);
            xCord += dimensions.x() + 2;
            gameObjectCollection.addGameObject(heart, Layer.BACKGROUND);
            heartsArr[i] = heart;
        }
    }


    /**
     * This function will create a single heart
     */
    private static GameObject createHeart(Vector2 dimensions,
                                          Renderable renderable,
                                          float xCord, float yCord) {
        return new GameObject(
                new Vector2(xCord, yCord),
                dimensions, renderable);
    }

    /**
     * Update the graphic life counter according to the game
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
        // Deleting one of the hearts and updating the number of lives
        if (numOfLives > livesCounter.value()) {
            gameObjectCollection.removeGameObject(
                    heartsArr[livesCounter.value()], Layer.BACKGROUND);
            numOfLives = livesCounter.value();
        }
        // Adding a heart and updating the number of lives
        if (numOfLives < livesCounter.value()) {
            float xCord = heartsArr[numOfLives - 1].getTopLeftCorner().x()
                    + dimensions.x();
            float yCord = heartsArr[numOfLives - 1].getTopLeftCorner().y();
            GameObject newHeart = createHeart(dimensions, renderable, xCord,
                    yCord);
            heartsArr[numOfLives] = newHeart;
            gameObjectCollection.addGameObject(newHeart, Layer.BACKGROUND);
            numOfLives = livesCounter.value();
        }
    }
}

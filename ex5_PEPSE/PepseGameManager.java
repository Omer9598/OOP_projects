import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;

public class PepseGameManager extends GameManager {

    // todo - choose the right numbers
    private static final int MIN_X_TERRAIN = -30;
    private static final int MAX_X_TERRAIN = 1800;
    private static final int CYCLE_LENGTH = 50;

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        Sky.create(gameObjects(), windowDimensions, Layer.BACKGROUND);
        Terrain terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS,
                windowDimensions, 5);
        terrain.createInRange(MIN_X_TERRAIN, MAX_X_TERRAIN);
        Night.create(gameObjects(), windowDimensions, CYCLE_LENGTH,
                Layer.FOREGROUND);
        Sun.create(windowDimensions, CYCLE_LENGTH, gameObjects(),
                Layer.BACKGROUND + 1);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

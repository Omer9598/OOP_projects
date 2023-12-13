import danogl.GameManager;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import pepse.world.Sky;
import pepse.world.Terrain;

public class PepseGameManager extends GameManager {

    // todo - choose th right numbers
    private static final int MIN_X_TERRAIN = -30;
    private static final int MAX_X_TERRAIN = 1800;

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        Sky.create(gameObjects(), windowController.getWindowDimensions(),
                Layer.BACKGROUND);
        Terrain terrain = new Terrain(gameObjects(), Layer.STATIC_OBJECTS,
                windowController.getWindowDimensions(), 2);
        terrain.createInRange(MIN_X_TERRAIN, MAX_X_TERRAIN);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

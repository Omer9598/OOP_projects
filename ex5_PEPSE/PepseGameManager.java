import danogl.GameManager;
import danogl.GameObject;
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
import pepse.world.daynight.SunHalo;

import java.awt.*;

public class PepseGameManager extends GameManager {
    private static final int MIN_X_TERRAIN = -30;
    private static final int MAX_X_TERRAIN = 1800;
    private static final int CYCLE_LENGTH = 50;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    // Game layers - in ascending order
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int SUN_LAYER = SKY_LAYER + 1;
    private static final int TREE_TRUNKS_LAYER = SKY_LAYER + 2;
    private static final int LEAVES_LAYER = SKY_LAYER + 3;
    private static final int SUN_HALO_LAYER = SKY_LAYER + 10;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int GAME_OBJECTS_LAYER = TERRAIN_LAYER + 1;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Terrain terrain = new Terrain(gameObjects(), TERRAIN_LAYER,
                windowDimensions, 5);
        terrain.createInRange(MIN_X_TERRAIN, MAX_X_TERRAIN);
        Night.create(gameObjects(), windowDimensions, CYCLE_LENGTH, NIGHT_LAYER);
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH,
                gameObjects(),SUN_LAYER);
        SunHalo.create(gameObjects(), sun, SUN_HALO_COLOR, SUN_HALO_LAYER);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

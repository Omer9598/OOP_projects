import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;

public class PepseGameManager extends GameManager {
    private static final int MIN_X_TERRAIN = -180;
    private static final int MAX_X_TERRAIN = 1700;
    private static final int CYCLE_LENGTH = 50;
    private static final float worldChunk = Block.SIZE * 5;
    private static float leftBorder;
    private static float rightBorder;
    private static float avatarPrevX;
    private static Avatar avatar;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    // Game layers - in ascending order
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int TREE_TRUNKS_LAYER = Layer.STATIC_OBJECTS + 10;
    private static final int LEAVES_LAYER = Layer.STATIC_OBJECTS + 20;
//        private static final int GAME_OBJECTS_LAYER = Layer.STATIC_OBJECTS + 30;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static Terrain terrain;
    private static Tree trees;

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        windowController.setTargetFramerate(80);
        gameObjects().layers().shouldLayersCollide(LEAVES_LAYER, TERRAIN_LAYER,
                true);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        Sky.create(gameObjects(), windowDimensions, SKY_LAYER);
        Night.create(gameObjects(), windowDimensions, CYCLE_LENGTH, NIGHT_LAYER);
        leftBorder = Terrain.changeMinMaxX(MIN_X_TERRAIN, false);
        rightBorder = Terrain.changeMinMaxX(MAX_X_TERRAIN, true);
        terrain = createTerrain(windowDimensions);
        createSunAndHalo(windowDimensions);
        trees = createTrees(terrain);
        float avatarYCord = terrain.groundHeightAt(windowDimensions.x() * 2)
                - Block.SIZE * 2;
        avatarPrevX = windowDimensions.x() * 0.5f;
        avatar = Avatar.create(gameObjects(), AVATAR_LAYER,
                new Vector2(avatarPrevX, avatarYCord),
                inputListener, imageReader);
        setCamera(new Camera(avatar,
                windowDimensions.mult(0.5f).subtract(avatar.getCenter()),
                windowDimensions, windowDimensions));
    }

    private Tree createTrees(Terrain terrain) {
        Tree trees = new Tree(terrain, gameObjects(), TREE_TRUNKS_LAYER,
                LEAVES_LAYER);
        trees.createInRange(leftBorder, rightBorder);
        return trees;
    }

    private void createSunAndHalo(Vector2 windowDimensions) {
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH,
                gameObjects(),SUN_LAYER);
        SunHalo.create(gameObjects(), sun, SUN_HALO_COLOR, SUN_HALO_LAYER);
    }

    private Terrain createTerrain(Vector2 windowDimensions) {
        Terrain terrain = new Terrain(gameObjects(), TERRAIN_LAYER,
                windowDimensions, 5);
        terrain.createInRange(leftBorder, rightBorder);
        return terrain;
    }

    /**
     * Updating the world according to the avatar movement
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        avatarMovement();
    }

    /**
     * This function handles avatar movement - creating and deleting the world
     */
    private void avatarMovement() {
        // If the avatar moved worldChunk to the right
        if(avatar.getCenter().x() >= avatarPrevX + worldChunk) {
            avatarPrevX = avatar.getCenter().x();
            // Creating a world chunk to the right
            terrain.createInRange(rightBorder, rightBorder + worldChunk);
            trees.createInRange(rightBorder, rightBorder + worldChunk);
            removeWorldChunk(leftBorder, leftBorder + worldChunk);
            leftBorder += worldChunk;
            rightBorder += worldChunk;
        }
        // If the avatar moved worldChunk to the left
        if(avatar.getCenter().x() <= avatarPrevX - worldChunk) {
            avatarPrevX = avatar.getCenter().x();
            // Creating a world chunk to the left
            terrain.createInRange(leftBorder - worldChunk, leftBorder);
            trees.createInRange(leftBorder - worldChunk, leftBorder);
            removeWorldChunk(rightBorder - worldChunk, rightBorder);
            rightBorder -= worldChunk;
            leftBorder -= worldChunk;
        }
    }

    /**
     * This function will remove all the objects in all layers, from
     * x value of startPoint to endPoint
     */
    private void removeWorldChunk(float startPoint, float endPoint) {
        for(GameObject gameObject: gameObjects()) {
            if(startPoint <= gameObject.getCenter().x() &&
                    gameObject.getCenter().x() <= endPoint) {
                gameObjects().removeGameObject(gameObject, LEAVES_LAYER);
                gameObjects().removeGameObject(gameObject, TERRAIN_LAYER);
                gameObjects().removeGameObject(gameObject, TERRAIN_LAYER - 1);
                gameObjects().removeGameObject(gameObject, TREE_TRUNKS_LAYER);
            }
        }
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}

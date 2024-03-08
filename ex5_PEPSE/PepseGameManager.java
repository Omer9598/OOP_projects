import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.trees.Fruit;
import pepse.world.trees.Leaf;
import pepse.world.trees.Trunk;

import java.util.List;

/**
 * Game manager to init the game
 */
public class PepseGameManager extends GameManager {
    private static final int MIN_X_TERRAIN = -180;
    private static final int MAX_X_TERRAIN = 1710;
    private static final int CYCLE_LENGTH = 30;
    private static float leftBorder;
    private static float rightBorder;
    // Game layers - in ascending order
    private static final int SKY_LAYER = Layer.BACKGROUND;
    private static final int SUN_LAYER = Layer.BACKGROUND + 1;
    private static final int ENERGY_DISPLAY_LAYER = Layer.BACKGROUND + 5;
    private static final int SUN_HALO_LAYER = Layer.BACKGROUND + 10;
    private static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    private static final int TREE_TRUNKS_LAYER = Layer.DEFAULT;
    private static final int FRUIT_LAYER = Layer.DEFAULT;
    private static final int LEAVES_LAYER = Layer.STATIC_OBJECTS + 20;
    private static final int AVATAR_LAYER = Layer.DEFAULT;
    private static final int NIGHT_LAYER = Layer.FOREGROUND;
    private static Avatar avatar;

    /**
     * Init the game
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        windowController.setTargetFramerate(60);
        gameObjects().layers().shouldLayersCollide(LEAVES_LAYER, TERRAIN_LAYER,
                true);
        Vector2 windowDimensions = windowController.getWindowDimensions();

        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, SKY_LAYER);

        GameObject night = Night.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(night, NIGHT_LAYER);

        leftBorder = Terrain.changeMinMaxX(MIN_X_TERRAIN, false);
        rightBorder = Terrain.changeMinMaxX(MAX_X_TERRAIN, true);
        Terrain terrain = createTerrain(windowDimensions);

        float avatarYCord = terrain.groundHeightAt(windowDimensions.x() * 2)
                - Block.SIZE * 2;
        float avatarXCord = windowDimensions.x() * 0.5f;

        avatar = new Avatar(new Vector2(avatarXCord, avatarYCord),
                inputListener, imageReader);
        gameObjects().addGameObject(avatar, AVATAR_LAYER);

        EnergyDisplay energyDisplay = new EnergyDisplay();
        avatar.setEnergyChangeCallback(energyDisplay);
        GameObject energy = energyDisplay.getDisplayObject();
        gameObjects().addGameObject(energy, ENERGY_DISPLAY_LAYER);

        createSunAndHalo(windowDimensions);
        createTrees(terrain);
    }

    private void createTrees(Terrain terrain) {
        Flora trees = new Flora(terrain);
        List<Block> blockList = trees.createInRange(leftBorder, rightBorder);
        for(Block block: blockList) {
            if(block.getTag().equals(Leaf.LEAF_TAG)) {
                gameObjects().addGameObject(block, LEAVES_LAYER);
                avatar.registerJumpObserver((Leaf) block);
            }
            if(block.getTag().equals(Trunk.TRUNK_TAG)) {
                gameObjects().addGameObject(block, TREE_TRUNKS_LAYER);
                avatar.registerJumpObserver((Trunk) block);
            }
            if(block.getTag().equals(Fruit.FRUIT_TAG)) {
                gameObjects().addGameObject(block, FRUIT_LAYER);
                avatar.registerJumpObserver((Fruit) block);
            }
        }
    }

    private void createSunAndHalo(Vector2 windowDimensions) {
        GameObject sun = Sun.create(windowDimensions, CYCLE_LENGTH);
        gameObjects().addGameObject(sun, SUN_LAYER);
        GameObject sunHalo = SunHalo.create(sun);
        gameObjects().addGameObject(sunHalo, SUN_HALO_LAYER);
        SunHalo.create(sun);
    }

    private Terrain createTerrain(Vector2 windowDimensions) {
        Terrain terrain = new Terrain(windowDimensions, 5);
        List<Block> blocksList = terrain.createInRange(leftBorder, rightBorder);
        for(Block block: blocksList) {
            gameObjects().addGameObject(block, TERRAIN_LAYER);
        }
        return terrain;
    }

    /**
     * Main function of the game
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}


//    /**
//     * Updating the world according to the avatar movement
//     */
//    @Override
//    public void update(float deltaTime) {
//        super.update(deltaTime);
////        avatarMovement();
//    }
//
//    /**
//     * This function handles avatar movement - creating and deleting the world
//     */
//    private void avatarMovement() {
//        // If avatar moved worldChunk to the right
//        if(avatar.getCenter().x() >= avatarPrevX + worldChunk) {
//            avatarPrevX = avatar.getCenter().x();
//            // Creating a world chunk to the right
//            terrain.createInRange(rightBorder, rightBorder + worldChunk);
//            trees.createInRange(rightBorder, rightBorder + worldChunk);
//            removeWorldChunk(leftBorder, leftBorder + worldChunk);
//            leftBorder += worldChunk;
//            rightBorder += worldChunk;
//        }
//        // If avatar moved worldChunk to the left
//        if(avatar.getCenter().x() <= avatarPrevX - worldChunk) {
//            avatarPrevX = avatar.getCenter().x();
//            // Creating a world chunk to the left
//            terrain.createInRange(leftBorder - worldChunk, leftBorder);
//            trees.createInRange(leftBorder - worldChunk, leftBorder);
//            removeWorldChunk(rightBorder - worldChunk, rightBorder);
//            rightBorder -= worldChunk;
//            leftBorder -= worldChunk;
//        }
//    }
//
//    /**
//     * This function will remove all the objects in all layers, from
//     * x value of startPoint to endPoint
//     */
//    private void removeWorldChunk(float startPoint, float endPoint) {
//        for(GameObject gameObject: gameObjects()) {
//            if(startPoint <= gameObject.getCenter().x() &&
//                    gameObject.getCenter().x() <= endPoint) {
//                gameObjects().removeGameObject(gameObject, LEAVES_LAYER);
//                gameObjects().removeGameObject(gameObject, TERRAIN_LAYER);
//                // None-solid terrain layer
//                gameObjects().removeGameObject(gameObject, TERRAIN_LAYER - 1);
//                gameObjects().removeGameObject(gameObject, TREE_TRUNKS_LAYER);
//            }
//        }
//    }

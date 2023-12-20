package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.security.Key;

public class Avatar extends GameObject {
    private static final float ENERGY_CHANGE = 0.5f;
    private static final float INITIAL_ENERGY = 100f;
    private static UserInputListener inputListener;
    private static GameObjectCollection gameObjects;
    private static final Vector2 AVATAR_DIMENSIONS = new Vector2(20, 35);
    // Avatar phase renderers
    private static AnimationRenderable staticAvatar;
    private static AnimationRenderable moveRightAvatar;
    private static AnimationRenderable moveLeftAvatar;
    private static AnimationRenderable jumpAvatar;
    private static AnimationRenderable flyAvatar;
    private float energy;
    private boolean isJumping = false;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.energy = INITIAL_ENERGY;
        this.setTag("Avatar");
    }

    /**
     * Create the avatar with initial energy of 100
     */
    public static Avatar create(GameObjectCollection gameObjects,
                                int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener,
                                ImageReader imageReader) {
        Avatar.gameObjects = gameObjects;
        Avatar.inputListener = inputListener;

        // Creating the avatar renderables
        Renderable[] staticAvatarArr = loadRenderables("avatar",
                4, imageReader);
        Renderable[] rightMoveArr = loadRenderables("right", 3,
                imageReader);
        Renderable[] leftMoveArr = loadRenderables("right", 3,
                imageReader);
        Renderable[] flyArr = loadRenderables("fly", 6,
                imageReader);
        Renderable[] jumpArr = loadRenderables("jump", 3,
                imageReader);

        staticAvatar = new AnimationRenderable(staticAvatarArr, 0.2f);
        moveRightAvatar = new AnimationRenderable(rightMoveArr, 0.1f);
        moveLeftAvatar = new AnimationRenderable(leftMoveArr, 0.1f);
        flyAvatar = new AnimationRenderable(flyArr, 0.1f);
        jumpAvatar = new AnimationRenderable(jumpArr, 0.1f);
        Avatar avatar = new Avatar(topLeftCorner, AVATAR_DIMENSIONS,
                staticAvatar);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * This function will create the renderable arrays for each of the avatar
     * phases
     */
    private static Renderable[] loadRenderables(String baseFileName, int count,
                                         ImageReader imageReader) {
        Renderable[] renderables = new Renderable[count];
        String directoryPath = "ex5_PEPSE/pepse/renderers/";

        for (int i = 0; i < count; i++) {
            String fullPath = directoryPath + baseFileName + (i + 1) + ".png";
            renderables[i] = imageReader.readImage(fullPath, true);
        }
        return renderables;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Avatar didn't move - adding energy
        if(getVelocity().y() == 0) {
            isJumping = false;
            if(energy < INITIAL_ENERGY) {
                energy += ENERGY_CHANGE;
            }
        }

        float xVelocity = 0;
        float yVelocity = 0;

        boolean rightClick = inputListener.isKeyPressed(KeyEvent.VK_RIGHT);
        boolean leftClick = inputListener.isKeyPressed(KeyEvent.VK_LEFT);
        boolean jumpClick = inputListener.isKeyPressed(KeyEvent.VK_SPACE);
        boolean flying = energy > 0 &&
                inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_SHIFT);

    }
}

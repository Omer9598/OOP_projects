package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final float ENERGY_CHANGE = 0.5f;
    private static final float INITIAL_ENERGY = 100f;
    private static final float HORIZONTAL_VELOCITY = 200f;
    private static final float VERTICAL_UP_VELOCITY = -300f;
    private static final float VERTICAL_ACCELERATION = 500f;
    private static UserInputListener inputListener;
    private static final Vector2 AVATAR_DIMENSIONS = new Vector2(25, 40);
    // Avatar phase renderers
    private static AnimationRenderable staticAvatar;
    private static AnimationRenderable moveRightAvatar;
    private static AnimationRenderable jumpAvatar;
    private static AnimationRenderable flyAvatar;
    private float energy;
    private static boolean isJumping = false;

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
        Avatar.inputListener = inputListener;
        // Creating the avatar renderables
        Renderable[] staticAvatarArr = loadRenderables("avatar",
                4, imageReader);
        Renderable[] rightMoveArr = loadRenderables("right", 6,
                imageReader);
        Renderable[] flyArr = loadRenderables("fly", 6,
                imageReader);
        Renderable[] jumpArr = loadRenderables("jump", 3,
                imageReader);

        staticAvatar = new AnimationRenderable(staticAvatarArr, 0.2f);
        moveRightAvatar = new AnimationRenderable(rightMoveArr, 0.1f);
        flyAvatar = new AnimationRenderable(flyArr, 0.1f);
        jumpAvatar = new AnimationRenderable(jumpArr, 0.1f);
        Avatar avatar = new Avatar(topLeftCorner, AVATAR_DIMENSIONS,
                staticAvatar);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.layers().shouldLayersCollide(layer, Layer.STATIC_OBJECTS, true);
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

        // Avatar doesn't move vertically - adding energy
        if(getVelocity().y() == 0) {
            isJumping = false;
            if(energy < INITIAL_ENERGY) {
                energy += ENERGY_CHANGE;
            }
        }

        // Checking user input (click)
        boolean rightClick = inputListener.isKeyPressed(KeyEvent.VK_RIGHT);
        boolean leftClick = inputListener.isKeyPressed(KeyEvent.VK_LEFT);
        boolean jumpClick = inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0 && !isJumping;
        boolean flying = energy > 0 &&
                inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_SHIFT);

        // Updating the current rendering
        if(!flying && !jumpClick && !leftClick && !rightClick &&
                this.getVelocity().y() == 0)
        {
            // Not moving or falling (flying) than Avatar is static
            renderer().setRenderable(staticAvatar);
        }

        // Avatar is moving - finding the velocity
        setVelocityX(rightClick, leftClick);
        setVelocityY(jumpClick, flying);

        if (!flying && !jumpClick && !rightClick && !leftClick){
            renderer().setRenderable(staticAvatar);
        }
    }

    /**
     * This function will check the vertical velocity of the avatar, updating
     * the renderer, the velocity and the energy
     */
    private void setVelocityY(boolean jumpClick, boolean flying) {
        if(jumpClick) {
            isJumping = true;
            energy -= ENERGY_CHANGE;
            renderer().setRenderable(jumpAvatar);
            transform().setVelocityY(VERTICAL_UP_VELOCITY);
            transform().setAccelerationY(VERTICAL_ACCELERATION);
        }
        if(flying) {
            energy -= ENERGY_CHANGE;
            renderer().setRenderable(flyAvatar);
            transform().setVelocityY(VERTICAL_UP_VELOCITY);
            transform().setAccelerationY(VERTICAL_ACCELERATION);
        }
    }

    /**
     * This function will set the horizontal velocity - checking if the avatar
     * is moving left or right.
     * The function will also set the velocity and renderer of the avatar
     */
    private void setVelocityX(boolean rightClick, boolean leftClick) {
        if(rightClick) {
            transform().setVelocityX(HORIZONTAL_VELOCITY);
            renderer().setRenderable(moveRightAvatar);
            renderer().setIsFlippedHorizontally(false);
            return;
        }
        if(leftClick) {
            transform().setVelocityX(-HORIZONTAL_VELOCITY);
            renderer().setRenderable(moveRightAvatar);
            // Flip the avatar to the left
            renderer().setIsFlippedHorizontally(true);
            return;
        }
        // If no right or left keys pressed - returning the avatar to static
        transform().setVelocityX(0f);
        renderer().setRenderable(staticAvatar);
    }
}

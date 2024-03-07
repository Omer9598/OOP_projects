package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final int INITIAL_ENERGY = 100;
    private int energy;
    private static final Vector2 AVATAR_DIMENSIONS = new Vector2(50, 70);
    private static AnimationRenderable staticAvatar;
    private static AnimationRenderable moveRightAvatar;
    private static AnimationRenderable jumpAvatar;
    private final UserInputListener inputListener;
    private EnergyChangeCallback energyChangeCallback;
    private final List<JumpObserver> jumpObservers = new ArrayList<>();
    public Avatar(Vector2 pos, UserInputListener inputListener,
                  ImageReader imageReader) {
        super(pos, AVATAR_DIMENSIONS,
                imageReader.readImage("pepse/assets/idle_0.png",
                        true));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.energy = INITIAL_ENERGY;
        this.setTag("avatar");
        createRenderables(imageReader);
    }

    private void createRenderables(ImageReader imageReader) {
        Renderable[] staticAvatarArr = loadRenderables("idle_",
                3, imageReader);
        Renderable[] rightMoveArr = loadRenderables("run_", 5,
                imageReader);
        Renderable[] jumpArr = loadRenderables("jump_", 3,
                imageReader);

        staticAvatar = new AnimationRenderable(staticAvatarArr, 0.3f);
        moveRightAvatar = new AnimationRenderable(rightMoveArr, 0.2f);
        jumpAvatar = new AnimationRenderable(jumpArr, 0.2f);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy > 0) {
            xVel -= VELOCITY_X;
            energy--;
            renderer().setRenderable(moveRightAvatar);
            renderer().setIsFlippedHorizontally(true);
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy > 0) {
            xVel += VELOCITY_X;
            energy--;
            renderer().setRenderable(moveRightAvatar);
            renderer().setIsFlippedHorizontally(false);
        }
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0 && energy > 10) {
            transform().setVelocityY(VELOCITY_Y);
            energy -= 10;
            renderer().setRenderable(jumpAvatar);
        }
        if(getVelocity().y() == 0 && getVelocity().x() == 0 && energy < 100) {
            energy++;
        }
        if(getVelocity().x() == 0) {
            renderer().setRenderable(staticAvatar);
        }
        // Notify energy change with the callback
        energyChangeCallback.onEnergyChanged(energy);
    }

    /**
     * Add energy to the avatar
     */
    public void addEnergy(int amount) {
        energy += amount;
    }

    /**
     * Register a JumpObserver
     */
    public void registerJumpObserver(JumpObserver observer) {
        jumpObservers.add(observer);
    }

    /**
     * Unregister a JumpObserver
     */
    public void unregisterJumpObserver(JumpObserver observer) {
        jumpObservers.remove(observer);
    }

    /**
     * Notify all registered observers when a jump occurs
     */
    private void notifyJumpObservers() {
        for (JumpObserver observer : jumpObservers) {
            observer.onJump();
        }
    }

    /**
     * Setting the energy change of the callback
     */
    public void setEnergyChangeCallback(EnergyChangeCallback callback) {
        this.energyChangeCallback = callback;
    }


    private static Renderable[] loadRenderables(String baseFileName, int count,
                                                ImageReader imageReader) {
        Renderable[] renderables = new Renderable[count];
        String directoryPath = "pepse/assets/";
        for (int i = 0; i < count; i++) {
            String fullPath = directoryPath + baseFileName + (i + 1) + ".png";
            renderables[i] = imageReader.readImage(fullPath, true);
        }
        return renderables;
    }
}

package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 400;
    private static final float VELOCITY_Y = -650;
    private static final float GRAVITY = 600;
    private static final int INITIAL_ENERGY = 100;
    private int energy;
    private static final Vector2 AVATAR_DIMENSIONS = new Vector2(50, 70);
    private final UserInputListener inputListener;
    private EnergyChangeCallback energyChangeCallback;
    public Avatar(Vector2 pos, UserInputListener inputListener,
                  ImageReader imageReader) {
        super(pos, AVATAR_DIMENSIONS,
                imageReader.readImage("pepse/assets/idle_0.png",
                        true));
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.energy = INITIAL_ENERGY;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT) && energy > 0) {
            xVel -= VELOCITY_X;
            energy--;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT) && energy > 0) {
            xVel += VELOCITY_X;
            energy--;
        }
        transform().setVelocityX(xVel);
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0 && energy > 10) {
            transform().setVelocityY(VELOCITY_Y);
            energy -= 10;
        }
        if(getVelocity().y() == 0 && getVelocity().x() == 0 && energy < 100) {
            energy++;
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
     * Setting the energy change of the callback
     */
    public void setEnergyChangeCallback(EnergyChangeCallback callback) {
        this.energyChangeCallback = callback;
    }
}

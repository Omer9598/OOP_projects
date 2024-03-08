package pepse.world.trees;

import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.JumpObserver;

import java.awt.*;
import java.util.Random;

/**
 * A class to create the leaves in the game
 */
public class Leaf extends Block implements JumpObserver {
    private static final int LEAVES_ANGLE = 12;
    /**
     * The tag of the leaf
     */
    public static final String LEAF_TAG = "leaf";
    private static final Color LEAVES_COLOR = new Color(50, 200, 30);
    private static final Vector2 LEAVES_DIMENSIONS =
            new Vector2(Block.SIZE, Block.SIZE);
    private static final Random random = new Random();

    public Leaf(Vector2 topLeftCorner) {
        super(topLeftCorner, new RectangleRenderable(ColorSupplier
                .approximateColor(LEAVES_COLOR)));
        this.setTag(LEAF_TAG);
        this.renderer().setRenderableAngle(LEAVES_ANGLE);
        this.setDimensions(LEAVES_DIMENSIONS);

        // Setting the leaves wind movement in some delay
        new ScheduledTask(this, random.nextInt(4),
                true, this::LeavesWindMovement);
    }

    /**
     * This function will create the leaves movement in the wind and their
     * dimensions
     */
    private void LeavesWindMovement() {
        new Transition<>(this,
                this.renderer()::setRenderableAngle, -7f, 7f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, 1.2f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

        new Transition<>(this,
                x -> this.setDimensions(new Vector2(
                        (1 + x / 100) * Block.SIZE, Block.SIZE)),
                -6f, 6f, Transition.LINEAR_INTERPOLATOR_FLOAT,
                0.8f, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

//    /**
//     * This function will add the leaves life cycle, including the fall of the
//     * leaf, its fadeout and return to its original location
//     * When called, the leaf will start to fall, and at the end of the cycle
//     * the leaf will be back in its original location on the tree
//     */
//    private void leavesLifeCycle() {
//        // Wait random amount of time before starting to fall and fadeout
//        new ScheduledTask(this, random.nextInt(100),
//                false, this::fallAndFadeOut);
//    }
//
//    /**
//     * This function will make the leaf fall and fade out - to be called after
//     * a random amount of time
//     */
//    private void fallAndFadeOut() {
//        this.transform().setVelocityY(FALL_VELOCITY);
//        // Setting the horizontal movement
//        new Transition<>(this,
//                x -> this.transform().setVelocityX(x),
//                -20f, 20f,
//                Transition.CUBIC_INTERPOLATOR_FLOAT, 2f,
//                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
//        this.renderer().fadeOut(FADE_OUT_TIME, this::leafCycleEnd);
//    }
//
//    /**
//     * This function will wait a random amount of time until resetting a leaf
//     */
//    private void leafCycleEnd() {
//        // Wait a random amount of time until resetting the leaf
//        new ScheduledTask(this, random.nextInt(10),
//                false, this::resetLeaf);
//    }
//
//    /**
//     * This function will reset a leaf
//     */
//    private void resetLeaf() {
//        this.setCenter(originalCenter);
//        this.renderer().setOpaqueness(1f);
//        this.setVelocity(Vector2.ZERO);
//        // Removing the horizontal transition
//        removeHorizontalMovement();
//        leavesLifeCycle();
//    }
//
//    /**
//     * This function will set the horizontal movement to 0 - creating a new
//     * Transition
//     */
//    private void removeMovement() {
//        new Transition<>(this,
//                x -> this.transform().setVelocityX(x),
//                0f, 0f,
//                Transition.CUBIC_INTERPOLATOR_FLOAT, 100f,
//                Transition.TransitionType.TRANSITION_ONCE, null);
//        new Transition<>(this,
//                this.renderer()::setRenderableAngle, 0f, 0f,
//                Transition.LINEAR_INTERPOLATOR_FLOAT, 100f,
//                Transition.TransitionType.TRANSITION_ONCE,
//                null);
//    }
//
//    @Override
//    public void onCollisionEnter(GameObject other, Collision collision) {
//        super.onCollisionEnter(other, collision);
//        // Removing the horizontal transition
//        removeHorizontalMovement();
//        this.setVelocity(Vector2.ZERO);
//    }

    /**
     * Rotate the leaves by 90 degrees
     */
    @Override
    public void onJump() {
        new Transition<>(this,
                this.renderer()::setRenderableAngle, 0f, 90f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, 1.5f,
                Transition.TransitionType.TRANSITION_ONCE,
                null);
    }
}

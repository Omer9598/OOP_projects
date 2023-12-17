package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.world.Block;

import java.util.Random;

public class Leaf extends GameObject {
    private static final int LEAVES_ANGLE = 13;
    private final Vector2 originalCenter;
    private static final Vector2 LEAVES_DIMENSIONS = new Vector2(20, 20);
    private static final Random random = new Random();
    private static final int FADE_OUT_TIME = 10;
    private static final float FALL_VELOCITY = 35f;
    private Transition<Float> horizontalTransition;

    public Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, LEAVES_DIMENSIONS, renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        this.originalCenter = this.getCenter();
        this.setTag("leaf");
        this.renderer().setRenderableAngle(LEAVES_ANGLE);
        this.setDimensions(LEAVES_DIMENSIONS);

        // Setting the leaves wind movement in some delay
        new ScheduledTask(this, random.nextInt(4),
                true, this::LeavesWindMovement);
        // Setting the leaves life cycle
        new ScheduledTask(this, random.nextInt(100),
                false, this::leavesLifeCycle);
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
                x -> this.setDimensions(new Vector2((1 + x / 100) * Block.SIZE, Block.SIZE)),
                -6f, 6f, Transition.LINEAR_INTERPOLATOR_FLOAT,
                0.6f, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
    }

    /**
     * This function will add the leaves life cycle, including the fall of the
     * leaf, its fadeout and return to its original location
     * When called, the leaf will start to fall, and at the end of the cycle
     * the leaf will be back in its original location on the tree
     */
    private void leavesLifeCycle() {
        // Wait random amount of time before starting to fall and fadeout
        new ScheduledTask(this, random.nextInt(10),
                false, this::fallAndFadeOut);
    }

    /**
     * This function will make the leaf fall and fade out - to be called after
     * a random amount of time
     */
    private void fallAndFadeOut() {
        this.transform().setVelocityY(FALL_VELOCITY);
        // Setting the horizontal movement
        horizontalTransition = new Transition<>(this,
                x -> this.transform().setVelocityX(x),
                -20f, 20f,
                Transition.CUBIC_INTERPOLATOR_FLOAT, 2f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        this.renderer().fadeOut(FADE_OUT_TIME, this::leafCycleEnd);
    }

    /**
     * This function will wait a random amount of time until resetting a leaf
     */
    private void leafCycleEnd() {
        // Wait a random amount of time until resetting the leaf
        new ScheduledTask(this, random.nextInt(10),
                false, this::resetLeaf);
    }

    /**
     * This function will reset a leaf
     */
    private void resetLeaf() {
        this.setCenter(originalCenter);
        this.renderer().setOpaqueness(1f);
        this.setVelocity(Vector2.ZERO);
        removeComponent(horizontalTransition);
        leavesLifeCycle();
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        removeComponent(horizontalTransition);
        this.transform().setVelocity(Vector2.ZERO);
    }

    // todo - use shouldCollideWith function to fix the bug?

    @Override
    public boolean shouldCollideWith(GameObject other) {
        super.shouldCollideWith(other);
        return other instanceof Block;
    }
}

package bricker.main;

import bricker.brick_strategies.CollisionStrategy;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Brick;
import bricker.gameobjects.UserPaddle;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;


public class BrickerGameManager extends GameManager {

    private final int BORDERS = 10;
    private final float BALL_SPEED = 200;
    private final float BRICK_HEIGHT = 15;
    private final float BRICKS_IN_A_ROW = 8;
    private final float BRICKS_GAP = 3;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        // Calling the constructor of mother class
        super(windowTitle, windowDimensions);
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        Vector2 windowDimensions = windowController.getWindowDimensions();
        // creating the ball
        createBall(imageReader, windowDimensions, soundReader);
        // creating the paddles
        createPaddles(imageReader, windowDimensions, inputListener);
        // create the left, right and upper walls
        createWall(Vector2.ZERO, new Vector2(BORDERS,
                windowDimensions.y()));
        createWall(new Vector2(windowDimensions.x(), 0),
                   new Vector2(BORDERS, windowDimensions.y()));
        createWall(Vector2.ZERO, new Vector2(windowDimensions.x(),
                BORDERS));
        // create background
        createBackground(imageReader, windowDimensions);
        // create bricks
        createBricks(windowDimensions, imageReader);
    }

    /**
     * This function will create the bricks of the game with the recommended
     * number of bricks - 5 rows, 8 bricks in each row
     */
    private void createBricks(Vector2 windowDimensions, ImageReader imageReader) {
        // finding the width of each brick
        float brickWidth = ((windowDimensions.x() - 2 * BORDERS) /
                BRICKS_IN_A_ROW) - BRICKS_GAP - 3;
        float bricksBottom = 5 * (BRICKS_GAP + BRICK_HEIGHT) + 2 * BORDERS;

        // creating the bricks
        for (float rowPixel = 2 * BORDERS + BRICKS_GAP;
             rowPixel < bricksBottom;
             rowPixel += BRICK_HEIGHT + BRICKS_GAP) {
            for (float colPixel = 2 * BORDERS + BRICKS_GAP;
                 colPixel < windowDimensions.x() - 4 * BORDERS;
                 colPixel += brickWidth + BRICKS_GAP) {
                GameObject brick = new Brick(new Vector2(colPixel, rowPixel),
                        new Vector2(brickWidth, BRICK_HEIGHT),
                        imageReader.readImage("assets/Brick.png", false),
                        new CollisionStrategy(gameObjects()));
                gameObjects().addGameObject(brick);
            }
        }
    }

    /**
     * This function will create the background
     */
    private void createBackground(ImageReader imageReader,
                                  Vector2 windowDimensions) {
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /** This function will create a wall, according to the coordinates
     * given
     */
    private void createWall(Vector2 anchorPosition, Vector2 widthAndHeight) {
        // the wall is invisible so the renderer is null
        GameObject wall = new GameObject(anchorPosition, widthAndHeight, null);
        gameObjects().addGameObject(wall);
    }

    /** This function will create the paddles and add it to gameObjects*/
    private void createPaddles(ImageReader imageReader,
                               Vector2 windowDimensions,
                               UserInputListener userInputListener) {
        Renderable paddleImage = imageReader.readImage("assets/paddle.png",
                true);

        // create user userPaddle
        GameObject user_Paddle = new UserPaddle(
                Vector2.ZERO,
                new Vector2(100, 15),
                paddleImage,
                userInputListener,
                windowDimensions);
        gameObjects().addGameObject(user_Paddle);
        user_Paddle.setCenter(new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - 30));
    }

    /** This function will create the game ball and add it to the gameObjects */
    private void createBall(ImageReader imageReader, Vector2 windowDimensions,
                            SoundReader soundReader) {
        // using the imageReader class from danogl
        Renderable ballImage = imageReader.readImage("assets/ball.png",
                true);
        // creating the Ball (inheriting from gameObject) and adding it
        Sound collisionSound = soundReader.readSound(
                "assets/blop_cut_silenced.wav");
        Ball ball = new Ball(Vector2.ZERO, new Vector2(20, 20),
                ballImage, collisionSound);
        ball.setCenter(windowDimensions.mult(0.5F));
        // setting the ball's velocity - start in a random direction
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random random = new Random();
        if (random.nextBoolean()) {
            ballVelX *= -1;
        }
        if (random.nextBoolean()) {
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        this.gameObjects().addGameObject(ball);
    }

    public static void main(String[] args) {
        new BrickerGameManager("Bricker",
                new Vector2(700, 500)).run();
    }
}


//        // create AI user_Paddle
//        GameObject aiPaddle = new AiPaddle(Vector2.ZERO,
//                new Vector2(100, 15), paddleImage, objectToFollow);
//        gameObjects().addGameObject(aiPaddle);
//        aiPaddle.setCenter(new Vector2(windowDimensions.x() / 2, 30));
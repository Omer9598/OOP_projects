package bricker.main;

import bricker.gameobjects.AiPaddle;
import bricker.gameobjects.Ball;
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

    private final int BORDER_WIDTH = 5;
    private final float BALL_SPEED = 300;
    private Ball ball;

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
        createPaddles(imageReader, windowDimensions, inputListener, ball);
        // create the left and right walls
        createWall(Vector2.ZERO, new Vector2(BORDER_WIDTH,
                windowDimensions.y()));
        createWall(new Vector2(windowDimensions.x(), 0),
                   new Vector2(BORDER_WIDTH, windowDimensions.y()));
        // create background
        createBackground(imageReader, windowDimensions);
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
                               UserInputListener userInputListener,
                               GameObject objectToFollow) {
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

        // create AI user_Paddle
        GameObject aiPaddle = new AiPaddle(Vector2.ZERO,
                new Vector2(100, 15), paddleImage, objectToFollow);
        gameObjects().addGameObject(aiPaddle);
        aiPaddle.setCenter(new Vector2(windowDimensions.x() / 2, 30));
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
        ball = new Ball(Vector2.ZERO, new Vector2(20, 20),
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

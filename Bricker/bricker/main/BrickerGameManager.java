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
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;


public class BrickerGameManager extends GameManager {

    private final int BORDERS = 12;
    private final float BALL_SPEED = 250;
    private Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;
    private final Counter brickCounter;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        // Calling the constructor of mother class
        super(windowTitle, windowDimensions);
        brickCounter = new Counter(0);
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        windowDimensions = windowController.getWindowDimensions();
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
     * This function will init the number of hearts in a single game.
     * When a player loses a heart, the ball jumps back to the center of the
     * screen.
     * When the player is out of hearts, the game stops
     */
//    private void createHearts() {
//
//    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkForGameEnd();
        }

    /**
     * This function will check if a game has ended (win or lose), and ask
     * the player if he wants to play again
     */
    private void checkForGameEnd() {
        String prompt = "";
        // Checking if the game ended
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            // The player lost
            prompt = "You lose!";
        }
        if (brickCounter.value() == 0) {
            // the player won
            prompt = "you win!";
        }
        // game ended - asking to play again
        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            }
            else {
                windowController.closeWindow();
            }
        }
    }



    /**
     * This function will create the bricks of the game with the recommended
     * number of bricks - 5 rows, 8 bricks in each row
     */
    private void createBricks(Vector2 windowDimensions, ImageReader imageReader)
    {
        // finding the width of each brick
        float bricksGap = 3;
        float brickInRow = 8;
        float brickWidth = ((windowDimensions.x() - 2 * BORDERS) /
                brickInRow) - bricksGap - 3;
        float brickHeight = 15;
        float bricksBottom = 5 * (bricksGap + brickHeight) + 2 * BORDERS;

        // creating the bricks
        for (float rowPixel = 2 * BORDERS + bricksGap;
             rowPixel < bricksBottom;
             rowPixel += brickHeight + bricksGap) {
            for (float colPixel = 2 * BORDERS + bricksGap;
                 colPixel < windowDimensions.x() - 4 * BORDERS;
                 colPixel += brickWidth + bricksGap) {
                GameObject brick = new Brick(new Vector2(colPixel, rowPixel),
                        new Vector2(brickWidth, brickHeight),
                        imageReader.readImage("assets/Brick.png", false),
                        new CollisionStrategy(gameObjects(), brickCounter));
                // adding the bricks to a static layer
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                brickCounter.increaseBy(1);
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


//        // create AI user_Paddle
//        GameObject aiPaddle = new AiPaddle(Vector2.ZERO,
//                new Vector2(100, 15), paddleImage, objectToFollow);
//        gameObjects().addGameObject(aiPaddle);
//        aiPaddle.setCenter(new Vector2(windowDimensions.x() / 2, 30));
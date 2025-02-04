package bricker.main;

import bricker.brick_strategies.BrickStrategyFactory;
import bricker.gameobjects.*;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * A class for the game manager
 */
public class BrickerGameManager extends GameManager {
    private static final int BORDERS = 12;
    private static final String BRICK_PATH = "assets/brick.png";
    private static final String HEART_PATH = "assets/heart.png";
    private static final String PADDLE_PATH = "assets/paddle.png";
    private static final String BALL_PATH = "assets/ball.png";
    private Ball ball;
    /**
     * The main ball's dimensions to differentiate it from mockBalls
     */
    public final Vector2 BALL_DIMENSIONS = new Vector2(20, 20);
    private Vector2 windowDimensions;
    private WindowController windowController;
    private final Counter brickCounter;
    private final Counter livesCounter;
    private BrickStrategyFactory brickStrategyFactory;
    private final Vector2 heartDimensions = new Vector2(30f, 30f);

    /**
     * The class constructor
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        // Calling the constructor of mother class
        super(windowTitle, windowDimensions);
        this.brickCounter = new Counter(0);
        this.livesCounter = new Counter(0);
    }

    /**
     * Starting the game
     *
     * @param imageReader      Contains a single method: readImage, which
     *                         reads an image from disk.
     *                         See its documentation for help.
     * @param soundReader      Contains a single method: readSound, which
     *                         reads a wav file from
     *                         disk. See its documentation for help.
     * @param inputListener    Contains a single method: isKeyPressed,
     *                         which returns whether
     *                         a given key is currently pressed by the user
     *                         or not. See its
     *                         documentation.
     * @param windowController Contains an array of helpful,
     *                         self-explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        this.windowController = windowController;
        windowController.setTargetFramerate(80);
        this.windowDimensions = windowController.getWindowDimensions();
        // creating the ball
        createBall(imageReader, soundReader);
        // creating the paddle
        createPaddles(imageReader, inputListener);
        // create the left, right and upper walls
        createWall(Vector2.ZERO, new Vector2(BORDERS, windowDimensions.y()));
        createWall(new Vector2(windowDimensions.x(), 0),
                new Vector2(BORDERS, windowDimensions.y()));
        createWall(Vector2.ZERO, new Vector2(windowDimensions.x(), BORDERS));
        createBackground(imageReader);
        // Creating the brickFactory to be used in createBricks function
        createBrickStrategyFactory(imageReader, soundReader, inputListener,
                windowDimensions, this);
        createBricks(imageReader);
        createGraphicLives(imageReader);
        createNumericLife();
    }

    /**
     * This function will create the lives counter symbol
     */
    private void createNumericLife() {
        Vector2 numericLivesTopLeftCorner =
                new Vector2(windowDimensions.x() - 100,
                        windowDimensions.y() - 30);
        Vector2 numericLivesDimensions = new Vector2(20, 20);
        // creating the graphic numeric lives counter
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(
                numericLivesTopLeftCorner, numericLivesDimensions,
                livesCounter, gameObjects());
        gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);
    }

    /**
     * This function will create graphic lives symbols (hearts)
     */
    private void createGraphicLives(ImageReader imageReader) {
        Vector2 heartsTopLeftCorner = new Vector2(2,
                windowDimensions.y() - 30);
        // creating graphic hearts
        int numOfLives = 3;
        GraphicLifeCounter hearts = new GraphicLifeCounter(heartsTopLeftCorner,
                heartDimensions,
                imageReader.readImage(HEART_PATH, true),
                livesCounter, gameObjects(), numOfLives);
        gameObjects().addGameObject(hearts, Layer.BACKGROUND);
    }

    /**
     * Update to check if the game ended
     * @param deltaTime The time, in seconds, that passed since the
     *                  last invocation
     *                  of this method (i.e., since the last frame).
     *                  This is useful for either accumulating the total
     *                  time that passed since some
     *                  event, or for physics integration (i.e.,
     *                  multiply this by
     *                  the acceleration to get an estimate of the added
     *                  velocity or
     *                  by the velocity to get an estimate of the difference
     *                  in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float ballHeight = ball.getCenter().y();
        if (ballHeight > windowDimensions.y()) {
            livesCounter.decrement();
            checkForGameEnd();
            // Updating the ball's position to the middle of the screen
            startBall();
        } else {
            checkForGameEnd();
        }
        // checking the camera status and update if needed
        if (camera() != null && ball.getCollisionCounter() == 0) {
            setCamera(null);
        }
        // Deleting all the game objects that are out of the screen
        for (GameObject gameObject: gameObjects()) {
            if (gameObject.getCenter().y() > windowDimensions.y()) {
                gameObjects().removeGameObject(gameObject);
            }
        }
    }

    /**
     * This function will check if a game has ended (win or lose), and ask
     * the player if he wants to play again
     */
    private void checkForGameEnd() {
        String prompt = "";
        // Checking if the game ended
        if (livesCounter.value() == 0) {
            // The player lost
            prompt = "You lose!";
        }
        if (brickCounter.value() <= 0) {
            // the player won
            prompt = "You win!";
        }
        // game ended - asking to play again
        if (!prompt.isEmpty()) {
            prompt += " Play again?";
            if (windowController.openYesNoDialog(prompt)) {
                livesCounter.reset();
                brickCounter.reset();
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }


    /**
     * This function will create the bricks of the game with the recommended
     * number of bricks - 5 rows, 8 bricks in each row
     */
    private void createBricks(ImageReader imageReader) {
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
                        imageReader.readImage(BRICK_PATH, false),
                        brickStrategyFactory.getStrategy());
                // adding the bricks to a static layer
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
                brickCounter.increaseBy(1);
            }
        }
    }

    /**
     * This function will create the brickStrategyFactory, providing all the
     * needed parameters
     */
    private void createBrickStrategyFactory(ImageReader imageReader,
                                            SoundReader soundReader,
                                            UserInputListener userInputListener,
                                            Vector2 windowDimensions,
                                            BrickerGameManager brickerGameManager) {
        brickStrategyFactory = new BrickStrategyFactory(gameObjects(),
                brickCounter, imageReader, soundReader, userInputListener,
                windowDimensions, ball, brickerGameManager, livesCounter,
                heartDimensions);
    }

    /**
     * This function will create the background
     */
    private void createBackground(ImageReader imageReader) {
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions,
                imageReader.readImage("assets/DARK_BG2_small.jpeg", false));
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    /**
     * This function will create a wall, according to the coordinates
     * given
     */
    private void createWall(Vector2 anchorPosition, Vector2 widthAndHeight) {
        // the wall is invisible so the renderer is null
        GameObject wall = new GameObject(anchorPosition, widthAndHeight, null);
        gameObjects().addGameObject(wall);
    }

    /**
     * This function will create the paddles and add it to gameObjects
     */
    private void createPaddles(ImageReader imageReader,
                               UserInputListener userInputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_PATH,
                true);
        float gapPaddleToWindow = 40;
        // create user userPaddle
        GameObject userPaddle = new Paddle(
                Vector2.ZERO,
                new Vector2(100, 15),
                paddleImage,
                userInputListener,
                windowDimensions);
        gameObjects().addGameObject(userPaddle);
        userPaddle.setCenter(new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - gapPaddleToWindow));
    }

    /**
     * This function will create the game ball and add it to the gameObjects
     */
    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        // using the imageReader class from danogl
        Renderable ballImage = imageReader.readImage(BALL_PATH,
                true);
        // creating the Ball (inheriting from gameObject) and adding it
        Sound collisionSound = soundReader.readSound(
                "assets/blop_cut_silenced.wav");
        ball = new Ball(Vector2.ZERO, BALL_DIMENSIONS,
                ballImage, collisionSound);
        startBall();
    }

    /**
     * This function will center the ball when starting a new game and
     * when a heart is lost
     */
    private void startBall() {
        ball.setCenter(windowDimensions.mult(0.5F));
        ball.setBallRandomDirection();
        this.gameObjects().addGameObject(ball);
    }

    /**
     * Returns the window dimensions
     */
    public Vector2 getWindowDimensions() {
        return windowDimensions;
    }

    /**
     * Main method to run the program
     */
    public static void main(String[] args) {
        new BrickerGameManager("Bricker",
                new Vector2(700, 500)).run();
    }
}

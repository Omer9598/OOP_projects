/**
 * A class that creates the game
 */
public class Game {
    private final Player playerX;
    private final Player playerO;
    private final Renderer renderer;
    private final int BOARD_SIZE;
    private final int WIN_STREAK;
    private boolean GAME_END = false;
    private Board BOARD;
    private Mark WINNER = null;
    private int NUMBER_OF_MARKS = 0;

    /**
     * A constructor that initializes default values
     */
    public Game(Player playerX, Player playerO, Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        // default values for the size and win streak
        this.BOARD_SIZE = 3;
        this.WIN_STREAK = 3;
    }

    /**
     * A constructor that initializes the game with the parameters given
     */
    public Game(Player playerX, Player playerO, int size, int winStreak,
                Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        this.BOARD_SIZE = size;
        this.WIN_STREAK = winStreak;
    }

    /**
     * This method will run a single tic-tac-toe game
     */
    public Mark run() {
        // creating the game board
        this.BOARD = new Board(BOARD_SIZE);
        int counter = 0;
        while (!GAME_END) {
            Mark[][] preMarkBoard = boardCopy();
            this.NUMBER_OF_MARKS++;
            this.renderer.renderBoard(this.BOARD);
            if (counter % 2 == 0) {
                this.playerX.playTurn(this.BOARD, Mark.X);
                int[] markCoordinates = getLastMark(preMarkBoard,
                        this.BOARD.getBoard());
                checkWinner(Mark.X, markCoordinates[0], markCoordinates[1]);
            } else {
                this.playerO.playTurn(this.BOARD, Mark.O);
                int[] markCoordinates = getLastMark(preMarkBoard,
                        this.BOARD.getBoard());
                checkWinner(Mark.O, markCoordinates[0], markCoordinates[1]);
            }
            counter++;
        }
        // showing the board for the final time in this run
        this.renderer.renderBoard(this.BOARD);
        return this.WINNER;
    }

    /**
     * Return the win streak of this game
     */
    public int getWinStreak() {
        return WIN_STREAK;
    }

    /**
     * Return the board size of the game
     */
    public int getBoardSize() {
        return BOARD_SIZE;
    }

    /**
     * This function will find the coordinates of the new mark
     */
    private int[] getLastMark(Mark[][] prevBoard, Mark[][] curBoard) {
        for (int row = 0; row < this.BOARD_SIZE; row++) {
            for (int col = 0; col < this.BOARD_SIZE; col++) {
                if (prevBoard[row][col] != curBoard[row][col]) {
                    return new int[]{row, col};
                }
            }
        }
        // no changes found
        return new int[]{0, 0};
    }

    /**
     * This function will copy a given board to a new instance
     */
    private Mark[][] boardCopy() {
        Mark[][] copiedBoard = new Mark[this.BOARD_SIZE][this.BOARD_SIZE];
        for (int row = 0; row < this.BOARD_SIZE; row++) {
            for (int col = 0; col < this.BOARD_SIZE; col++) {
                copiedBoard[row][col] = this.BOARD.getMark(row, col);
            }
        }
        return copiedBoard;
    }


    /**
     * A method to check if one of the players won, updates the winner field
     */
    private void checkWinner(Mark mark, int row, int col) {
        this.GAME_END = true;
        // check horizontal or vertical win
        horizontalOrVerticalWin(mark, row, col, mark);
        // check for diagonal win
        diagonalWin(mark, row, col, mark);
        // check if one of the players won
        if (this.WINNER != null) {
            this.WINNER = mark;
            return;
        }
        // check draw
        if (this.NUMBER_OF_MARKS == BOARD_SIZE * BOARD_SIZE) {
            this.WINNER = Mark.BLANK;
            return;
        }
        // if we got this far, no player won yet
        this.GAME_END = false;
    }

    /** Helper function to count the number of marks in a given direction */
    private int countMarkInDirection(int row, int col, int rowDelta,
                                     int colDelta, Mark mark) {
        int count = 0;
        while (row < BOARD_SIZE && row >= 0 && col < BOARD_SIZE && col >= 0 &&
                this.BOARD.getMark(row, col) == mark) {
            count++;
            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

    /** Helper function to check if a player won by horizontally/ vertically */
    private void horizontalOrVerticalWin(Mark mark, int row, int col,
                                         Mark winner) {
        // horizontal
        int horizontalMarks = 0;
        // checking the left side
        horizontalMarks += countMarkInDirection(row, col, 0,
                -1, mark);
        // checking the right side
        horizontalMarks += countMarkInDirection(row, col, 0,
                1, mark);

        // vertical
        int verticalMarks = 0;
        // checking upwards
        verticalMarks += countMarkInDirection(row, col, -1,
                0, mark);
        // checking downwards
        verticalMarks += countMarkInDirection(row, col, 1,
                0, mark);

        // we counted the new marked cube already
        horizontalMarks -= 1;
        verticalMarks -= 1;

        if (horizontalMarks == WIN_STREAK || verticalMarks == WIN_STREAK) {
            this.WINNER = winner;
        }
    }

    /** Helper function to check if there is a diagonal win */
    private void diagonalWin(Mark mark, int row, int col, Mark winner) {
        // diagonal
        int diagonalMarks = 0;
        // right-up
        diagonalMarks += countMarkInDirection(row, col, -1,
                1, mark);
        // left-down
        diagonalMarks += countMarkInDirection(row, col, 1,
                -1, mark);

        // anti diagonal
        int antiDiagonalMarks = 0;
        // up-left
        antiDiagonalMarks += countMarkInDirection(row, col, -1,
                -1, mark);
        // right-down
        antiDiagonalMarks += countMarkInDirection(row, col, 1,
                1, mark);

        // counted the new marked cube
        antiDiagonalMarks -= 1;
        diagonalMarks -= 1;

        // checking if the player won
        if (diagonalMarks == WIN_STREAK || antiDiagonalMarks == WIN_STREAK)
        {
            this.WINNER = winner;
        }
    }
}

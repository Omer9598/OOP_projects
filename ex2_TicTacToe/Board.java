/**
 * A class to create the board game
 */
public class Board {
    // private class variables
    private Mark[][] BOARD;
    private final int SIZE;
    /** Default constructor */
    public Board () {SIZE = 3;}

    /**
     * class constructor  - initiates the board with blanks, board size will be
     * size * size
     */
    public Board(int size) {
        this.SIZE = size;

        // init board
        BOARD = new Mark[SIZE][SIZE];
        for (int row = 0; row < BOARD.length; row++) {
            for (int col = 0; col < BOARD[row].length; col++) {
                BOARD[row][col] = Mark.BLANK;
            }
        }
    }

    /**
     * Return the size of the board
     */
    public int getSize() {
        return SIZE;
    }

    /**
     * Return the board
     */
    Mark[][] getBoard() {
        return BOARD;
    }

    /**
     * A method to mark the given mark on the given coordinates on the table
     * returns - true if the mark completed successfully, false otherwise
     * (invalid coordinates - out of bounds or already marked on the board)
     */
    public boolean putMark(Mark mark, int row, int col) {
        if (checkCoordinates(row, col)) {
            if (this.BOARD[row][col] != Mark.BLANK) {
                return false;
            }
            // updating the board
            BOARD[row][col] = mark;
            return true;
        }
        return false;
    }

    /**
     * returns the mark in the coordinates given
     */
    public Mark getMark(int row, int col) {
        if (checkCoordinates(row, col)) {
            return BOARD[row][col];
        }
        // the coordinates are invalid
        return null;
    }

    // ******** private methods ********

    /**
     * check if the given coordinates are valid
     */
    private boolean checkCoordinates(int row, int col) {
        if (row >= SIZE || row < 0) {
            return false;
        }
        return col < SIZE && col >= 0;
    }
}

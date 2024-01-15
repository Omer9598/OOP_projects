import java.util.Random;

/**
 * A class that implements a random strategy player
 */
public class WhateverPlayer implements Player {

    /**
     * Implementing the random strategy of the player
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int row = -1, col = -1;
        Random random = new Random();
        while (!board.putMark(mark, row, col)) {
            row = random.nextInt(board.getSize());
            col = random.nextInt(board.getSize());
        }
    }
}

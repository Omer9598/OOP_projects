import java.util.Random;

public class WhateverPlayer implements Player {

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

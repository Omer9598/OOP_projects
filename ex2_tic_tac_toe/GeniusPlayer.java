public class GeniusPlayer implements Player {
    @Override
    public void playTurn(Board board, Mark mark) {
        boolean marked = false;
        // start from the second column
        for (int i = 1; i < board.getSize(); i++) {
            if (marked) {
                break;
            }
            for (int j = 0; j < board.getSize(); j++) {
                if (board.putMark(mark, j, i)) {
                    marked = true;
                    break;
                }
            }
        }
    }
}

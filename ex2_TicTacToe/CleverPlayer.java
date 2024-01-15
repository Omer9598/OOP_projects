/**
 * A class of a player that is slightly better than whatever player
 */
public class CleverPlayer implements Player {

    /**
     * Implement a better strategy compared to whatever player
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        boolean marked = false;
        for (int i = 0; i < board.getSize(); i++) {
            if (marked) {
                break;
            }
            for (int j = 0; j < board.getSize(); j++) {
                if (board.putMark(mark, i, j)) {
                    marked = true;
                    break;
                }
            }
        }
    }
}

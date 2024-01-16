/**
 * A class that represents the human player
 */
public class HumanPlayer implements Player {

    private final String INVALID_POSITION = "Invalid mark position," +
            " please choose a different position.\n" +
            "Invalid coordinates, type again:";
    private final String NON_BLANK = "Mark position is already occupied.\n" +
            "Invalid coordinates, type again:";
    private final String PLAY_TURN = "Player %s, type coordinates: ";

    /**
     * Default constructor of the class
     */
    public HumanPlayer() {}

    @Override
    /**
     This method will do a single turn of the player.
     assuming the input is int, if invalid int is given, the method will
     continue asking the player to pick a valid place to mark
     */
    public void playTurn(Board board, Mark mark) {
        System.out.printf((PLAY_TURN) + "%n", mark);
        // Assume the input is int
        int num = KeyboardInput.readInt();
        boolean validInput = false;
        while (!validInput) {
            int col = num % 10;
            int row = num / 10;
            if (board.putMark(mark, row, col)) {
                validInput = true;
                continue;
            }
            handleInvalidInput(row, col, board);
            num = KeyboardInput.readInt();
        }
    }


    private void handleInvalidInput(int row, int col, Board board) {
        // Invalid input
        if (board.getMark(row, col) == null) {
            System.out.println(INVALID_POSITION);
        }
        // Already marked position
        if (board.getMark(row, col) != null &&
                board.getMark(row, col) != Mark.BLANK) {
            System.out.println(NON_BLANK);
        }
    }
}

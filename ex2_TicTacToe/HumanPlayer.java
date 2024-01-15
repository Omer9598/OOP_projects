/**
 * A class that represents the human player
 */
public class HumanPlayer implements Player {
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
        System.out.println("player" + mark + ", type coordinates: ");

        // assume the input is int
        int num = KeyboardInput.readInt();

        boolean valid_input = false;

        while (!valid_input) {
            int col = num % 10;
            int row = num / 10;
            if (board.putMark(mark, row, col)) {
                valid_input = true;
                continue;
            }
            handle_invalid_input(row, col, board);
            num = KeyboardInput.readInt();
        }
    }


    private void handle_invalid_input(int row, int col, Board board) {
        // Invalid input
        if (board.getMark(row, col) == null) {
            System.out.println("Invalid mark position," +
                    " please choose a different position.");
        }
        // Already marked position
        if (board.getMark(row, col) != null &&
                board.getMark(row, col) != Mark.BLANK) {
            System.out.println("Mark position is already occupied.");
        }
    }
}

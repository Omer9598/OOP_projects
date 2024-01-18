/**
 * A class that represents the human player
 */
public class HumanPlayer implements Player {
    public final static String INVALID_COORDINATE = "Invalid mark position," +
            " please choose a different position.\n" +
            "Invalid coordinates, type again: ";
    public final static String OCCUPIED_COORDINATE = "Mark position is" +
            " already occupied.\n" +
            "Invalid coordinates, type again: ";

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
        System.out.print(playerRequestInputString(mark.toString()));
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
            System.out.print(INVALID_COORDINATE);
        }
        // Already marked position
        if (board.getMark(row, col) != null &&
                board.getMark(row, col) != Mark.BLANK) {
            System.out.print(OCCUPIED_COORDINATE);
        }
    }

    /**
     * Use this method to generate the text that HumanPlayer should send
     *
     * @param markSymbol according to the Mark the player uses in the current turn.
     * @return String to be printed to the player.
     */
    public static String playerRequestInputString(String markSymbol) {
        return "Player " + markSymbol + ", type coordinates: ";

    }
}

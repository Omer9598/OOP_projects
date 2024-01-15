import java.util.Scanner;

public class HumanPlayer implements Player {
    public HumanPlayer() {
    }

    @Override
    /*
      This method will do a single turn of the player.
      assuming the input is int, if invalid int is given, the method will
      continue asking the player to pick a valid place to mark
     */
    public void playTurn(Board board, Mark mark) {
        // getting input from the user
        Scanner in = new Scanner(System.in);

        System.out.println("player" + mark + ", type coordinates: ");

        // assume the input is int
        int num = in.nextInt();

        boolean valid_input = false;

        while (!valid_input) {
            int col = num % 10;
            int row = num / 10;
            if (board.putMark(mark, row, col)) {
                valid_input = true;
                continue;
            }
            handle_invalid_input(row, col, board);
            num = in.nextInt();
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

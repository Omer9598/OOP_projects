public class Tournament {
    // for the main function
    private static final int ROUNDS_INDEX = 0;
    private static final int BOARD_SIZE_INDEX = 1;
    private static final int STREAK_NUM_INDEX = 2;
    private static final int RENDER_INDEX = 3;
    private static final int PLAYER_1_INDEX = 4;
    private static final int PLAYER_2_INDEX = 5;
    private static final String ERR_MESSAGE_PLAYER = "Choose a player," +
            " and start" +
            " again.\nThe players: [human, clever, whatever, genius]\n";
    private static final String ERR_MESSAGE_RENDERER = "Unknown renderer." +
            " Please choose one of the following [console, none]";
    private static final String STATS_MESSAGE =
            "######### Results #########\n" +
                    "Player 1, %s won: %d rounds\n" +
                    "Player 2, %s won: %d rounds\n" +
                    "Ties: %d\n";

    private final Player[] players;
    private final Renderer renderer;
    private final int rounds;

    private int player1Wins;
    private int player2Wins;
    private int drawsCount;

    /**
     * A constructor of the class - getting the players' names
     */
    public Tournament(int rounds, Renderer renderer, Player player1,
                      Player player2) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.players = new Player[]{player1, player2};


        this.player1Wins = 0;
        this.player2Wins = 0;
        this.drawsCount = 0;
    }

    /**
     * A method to start a single tournament
     * returns an array of: {number of player1 wins, number of player2 wins,
     * number of draws}
     */
    public void playTournament(int size, int winStreak, String[] playerNames) {
        this.player1Wins = 0;
        this.player2Wins = 0;
        this.drawsCount = 0;

        // Start playing "rounds" times
        for (int i = 0; i < rounds; i++) {
            this.playRound(size, winStreak, this.players, i);
        }
        // Printing the tournament result
        System.out.printf(STATS_MESSAGE, playerNames[0], this.player1Wins,
                playerNames[1], this.player2Wins, this.drawsCount);
    }

    private void playRound(int size, int winStreak, Player[] playersArr, int i) {
        boolean player1IsX = i % 2 == 0;
        Player xPlayer = playersArr[i % 2];
        Player oPlayer = playersArr[1 - i % 2];

        Game game = new Game(xPlayer, oPlayer, size, winStreak,
                this.renderer);
        Mark result = game.run();

        // Determine which player won
        determineWinner(player1IsX, result);
    }

    private void determineWinner(boolean player1IsX, Mark result) {
        switch (result) {
            case BLANK:
                drawsCount++;
                break;
            case X:
                if (player1IsX) {
                    player1Wins++;
                } else {
                    player2Wins++;
                }
                break;
            case O:
                if (player1IsX) {
                    player2Wins++;
                } else {
                    player1Wins++;
                }
                break;
        }
    }

    /**
     * A helper function to check if the arguments provided are valid
     * Return true if the one of the arguments is not valid according to the
     * instructions
     */
    private boolean checkInvalidArgs(int rounds, Renderer renderer,
                                     Player player_1, Player player_2,
                                     int winStreak, int size) {
        if (rounds < 0) {
            return true;
        }
        if (winStreak > size) {
            return true;
        }
        if (renderer == null) {
            System.err.println(ERR_MESSAGE_RENDERER);
            return true;
        }
        if (player_1 == null || player_2 == null) {
            System.err.println(ERR_MESSAGE_PLAYER);
            return true;
        }
        // Else - all arguments are valid
        return false;
    }

    /**
     * The main method of the program
     */
    public static void main(String[] args) {
        // parsing the arguments
        int rounds = Integer.parseInt(args[ROUNDS_INDEX]);
        int board_size = Integer.parseInt(args[BOARD_SIZE_INDEX]);
        int win_streak = Integer.parseInt(args[STREAK_NUM_INDEX]);
        String render_type = args[RENDER_INDEX];
        String player_1_type = args[PLAYER_1_INDEX];
        String player_2_type = args[PLAYER_2_INDEX];

        // Creating the factories to build the players and renderer
        PlayerFactory playerFactory = new PlayerFactory();
        RendererFactory rendererFactory = new RendererFactory();

        // Building the players with their factories
        Renderer renderer = rendererFactory.buildRenderer(render_type,
                board_size);
        Player player_1 = playerFactory.buildPlayer(player_1_type);
        Player player_2 = playerFactory.buildPlayer(player_2_type);

        Tournament tournament = new Tournament(rounds, renderer,
                player_1, player_2);
        // Checking valid arguments to play the tournament
        if (tournament.checkInvalidArgs(rounds, renderer, player_1, player_2,
                win_streak, board_size)) {
            return;
        }
        tournament.playTournament(board_size, win_streak,
                new String[]{player_1_type, player_2_type});
    }
}

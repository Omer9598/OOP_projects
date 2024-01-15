/**
 * Interface for the different players of the game
 */
interface Player {
    /**
     * A method to do the player's next move in the game
     */
    void playTurn(Board board, Mark mark);
}

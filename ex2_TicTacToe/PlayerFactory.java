/**
 * A factory to build the players
 */
public class PlayerFactory {

    /**
     * Default constructor
     */
    public PlayerFactory () {}

    /** Building the player according to the type given */
    public Player buildPlayer(String type) {
        return switch (type.toLowerCase()) {
            case "human" -> new HumanPlayer();
            case "whatever" -> new WhateverPlayer();
            case "clever" -> new CleverPlayer();
            case "genius" -> new GeniusPlayer();
            default -> null;
        };
    }
}

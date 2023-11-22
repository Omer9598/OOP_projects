public class PlayerFactory {

    /** Building the player according to the type given */
    public Player buildPlayer(String player_type) {
        return switch (player_type.toLowerCase()) {
            case "human" -> new HumanPlayer();
            case "whatever" -> new WhateverPlayer();
            case "clever" -> new CleverPlayer();
            case "genius" -> new GeniusPlayer();
            default -> null;
        };
    }
}

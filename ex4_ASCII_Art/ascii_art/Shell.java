package ascii_art;

import image.Image;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Shell {

    private static final String CMD_EXIT = "exit";
    private static final String CHARS_COMMAND = "chars";
    private static final String ADD_COMMAND = "add";
    private static final String REMOVE_COMMAND = "remove";
//    private final Image image;
    private final Set<Character> charSet = new HashSet<>();

    public Shell(Image img) {
//        this.image = img;
        // default charSet
        for (int i = 0; i < 10; i++) {
            Character character = (char) ('0' + i);
            charSet.add(character);
        }
    }

    public void run() throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String cmd = scanner.nextLine().trim();
        String[] words = cmd.split("\\s+");
        while (!words[0].equalsIgnoreCase(CMD_EXIT)) {
            // checking if the input is one of the commands
            switch (words[0]) {
                case CHARS_COMMAND -> showChars();
                case ADD_COMMAND, REMOVE_COMMAND -> AddRemoveCommand(words);
            }
            // No legal command found - asking for an input again
            System.out.print(">>> ");
            cmd = scanner.nextLine().trim();
            words = cmd.split("\\s+");
        }
    }

    /**
     * This function will handle the add and remove commands:
     * Throw an exception if its invalid, or add/ remove the given characters
     * to, or from charSet
     * @param words Array containing the words given in the console
     */
    private void AddRemoveCommand(String[] words) throws Exception {
        if(words.length == 2) {
            char[] parseCharRange = parseCharRange(words[1]);
            if(words[0].equals(ADD_COMMAND)) {
                addChars(parseCharRange);
                return;
            }
            if(words[0].equals(REMOVE_COMMAND)) {
                removeChars(parseCharRange);
                return;
            }
        }
        // words.length < 2 or words[1] is not a valid command
        throw new Exception("Did not add due to incorrect format");
    }

    /**
     * This function will remove the range of chars given, in the same logic as
     * removeChars function
     */
    private void removeChars(char[] range) {
        if(range != null) {
            char startChar = range[0];
            char endChar = range[1];
            // Forward range
            for(char currentChar = startChar; currentChar <= endChar;
                currentChar++) {
                charSet.remove(currentChar);
            }
            // Reversed range
            for(char currentChar = endChar; currentChar <= startChar;
                currentChar++) {
                charSet.remove(currentChar);
            }
        }
    }

    /**
     * This function will add the char range into the charSet, according to the
     * range given from parseCharRange function
     */
    private void addChars(char[] range) {
        if(range != null) {
            char startChar = range[0];
            char endChar = range[1];
            // Forward range
            for(char currentChar = startChar; currentChar <= endChar;
                currentChar++) {
                charSet.add(currentChar);
            }
            // Reversed range
            for(char currentChar = endChar; currentChar <= startChar;
                currentChar++) {
                charSet.add(currentChar);
            }
        }
    }

    /**
     * This function will parse the word given after "add" command
     * @return one of 4 options:
     * 1. for a single char - {char, char}
     * 2. for "all" - {' ', '~'}
     * 3. for "space" - {' ', ' '}
     * 4. for any word different from the above, return null
     */
    private static char[] parseCharRange(String word) {
        char[] returnValue = null;
        switch (word) {
            case "all" -> returnValue = new char[] {' ', '~'};
            case "space" -> returnValue = new char[] {' ', ' '};
            default -> {
                if(word.length() == 1) {
                    char singleChar = word.charAt(0);
                    returnValue = new char[] {singleChar, singleChar};
                }
                if(word.length() == 3) {
                    returnValue = new char[] {word.charAt(0), word.charAt(2)};
                }
            }
        }
        // if none of the conditions entered, return null
        return returnValue;
    }


    /**
     * This function will show all the current chars in the charSet
     */
    private void showChars() {
        charSet.stream().sorted().forEach(c -> System.out.print(c + " "));
        System.out.println();
    }
}

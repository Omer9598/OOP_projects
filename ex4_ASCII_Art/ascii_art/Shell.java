package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Shell {

    private static final String CMD_EXIT = "exit";
    private static final String CHARS_COMMAND = "chars";
    private static final String ADD_COMMAND = "add";
    private static final String REMOVE_COMMAND = "remove";
    private static final String RESOLUTION_COMMAND = "res";
    private static final String RENDER_COMMAND = "render";
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final char[] INITIAL_CHARS_RANGE = new char[] {'0', '9'};
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private final BrightnessImgCharMatcher charMatcher;
    private final AsciiOutput output;
    private final Set<Character> charSet = new HashSet<>();

    public Shell(Image img) {
        this.minCharsInRow = Math.max(1, img.getWidth() / img.getHeight());
        this.maxCharsInRow = img.getWidth() / MIN_PIXELS_PER_CHAR;
        this.charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW,
                maxCharsInRow), minCharsInRow);
        this.charMatcher = new BrightnessImgCharMatcher(img, FONT_NAME);
        this.output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
        // default charSet
        addChars(INITIAL_CHARS_RANGE);
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
                case ADD_COMMAND, REMOVE_COMMAND -> AddRemoveCommands(words);
                case RESOLUTION_COMMAND -> resChange(words);
                case RENDER_COMMAND -> render();
                default -> throw new Exception("Did not executed due to" +
                        " incorrect command");
            }
            // Moving to the next command
            System.out.print(">>> ");
            cmd = scanner.nextLine().trim();
            words = cmd.split("\\s+");
        }
    }

    /**
     * This function will create the HTML file
     */
    private void render() {
        // Creating the ascii array
        Character[] charArray = charSet.toArray(new Character[0]);
        char[][] asciiArray = charMatcher.chooseChars(charsInRow, charArray);
        // Creating the html file
        output.output(asciiArray);
    }

    /**
     * This function will change the resolution according to the command given:
     * up - multiply rhe resolution by 2
     * down - divide by 2
     */
    private void resChange(String[] words) throws Exception {
        // Checking valid command after "res"
        if(words.length != 2 ||
                (!words[1].equals("up") && !words[1].equals("down"))) {
            throw new Exception("Did not executed due to incorrect command");
        }
        if(words[1].equals("up")) {
            // checking we are not exceeding the maxCharsIn
            if(charsInRow == maxCharsInRow) {
                throw new
                        Exception("Did not change due to exceeding boundaries");
            }
            // Changing the resolution
            charsInRow = charsInRow * 2;
        }
        if(words[1].equals("down")) {
            if(charsInRow == minCharsInRow) {
                throw new
                        Exception("Did not change due to exceeding boundaries");
            }
            charsInRow = charsInRow / 2;
        }
        // Change made successfully, print the new resolution:
        System.out.printf("Width set to %d%n", charsInRow);
        }

    /**
     * This function will handle the add and remove commands:
     * Throw an exception if its invalid, or add/ remove the given characters
     * to, or from charSet
     * @param words Array containing the words given in the console
     */
    private void AddRemoveCommands(String[] words) throws Exception {
        if(words.length == 2) {
            char[] parseCharRange = parseCharRange(words[1]);
            if(parseCharRange == null) {
                throw new Exception("Did not add due to incorrect format");
            }
            if (words[0].equals(ADD_COMMAND)) {
                addChars(parseCharRange);
                return;
            }
            if (words[0].equals(REMOVE_COMMAND)) {
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

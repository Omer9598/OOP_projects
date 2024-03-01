package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Shell {

    private static final String CMD_EXIT = "exit";
    private static final String CHARS_COMMAND = "chars";
    private static final String ADD_COMMAND = "add";
    private static final String REMOVE_COMMAND = "remove";
    private static final String RESOLUTION_COMMAND = "res";
    private static final String OUTPUT_COMMAND = "output";
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final String INCORRECT_COMMAND = "Did not execute due to" +
            " incorrect command.";
    private static final String INCORRECT_FORMAT = "Did not add due to" +
            " incorrect format.";
    private static final String EXCEED_BOUNDARIES = "Did not change" +
            " resolution due to exceeding boundaries.";
    private static final String RES_MESSAGE = "Resolution set to %d%n";
    private static final String RES_UP = "up";
    private static final String RES_DOWN = "down";
    private static final char[] INITIAL_CHARS_RANGE = new char[] {'0', '9'};
    private static final int INITIAL_CHARS_IN_ROW = 128;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final String INCORRECT_RESOLUTION_COMMAND = "Did not" +
            " change resolution due to incorrect format.";
    private static final String HTML_OUTPUT = "html";
    private static final String CONSOLE_OUTPUT = "console";
    private static final String INVALID_OUTPUT = "Did not change output" +
            " method due to incorrect format.";
    private static final String ASCII_ART_COMMAND = "asciiArt";
    private static final String CHARSET_EMPTY = "Did not execute. Charset" +
            " is empty.";
    private static final String IMAGE_COMMAND = "image";
    private static final String IMAGE_NOT_LOADED = "Did not execute due " +
            "to problem with image file.";
    private Image image;
    private int minCharsInRow;
    private int maxCharsInRow;
    private int charsInRow;
    private BrightnessImgCharMatcher charMatcher;
    private final HtmlAsciiOutput htmlAsciiOutput;
    private final ConsoleAsciiOutput consoleAsciiOutput;
    private AsciiOutput asciiOutput;
    private final Set<Character> charSet = new HashSet<>();

    /**
     * Constructor to the shell
     */
    public Shell() {
        this.htmlAsciiOutput = new HtmlAsciiOutput(OUTPUT_FILENAME,
                FONT_NAME);
        this.consoleAsciiOutput = new ConsoleAsciiOutput();
        // Default parameters
        this.asciiOutput = consoleAsciiOutput;
        addChars(INITIAL_CHARS_RANGE);
        // todo - check what is the default image if there is one
        this.image = null;
    }

    /**
     * This function will run the asciiArt
     */
    public void run() throws Exception {
        System.out.print(">>> ");
        String cmd = KeyboardInput.readLine();
        String[] words = cmd.split("\\s+");
        while (!words[0].equalsIgnoreCase(CMD_EXIT)) {
            // checking if the input is one of the commands
            switch (words[0]) {
                case CHARS_COMMAND -> showChars();
                case ADD_COMMAND, REMOVE_COMMAND -> AddRemoveCommands(words);
                case RESOLUTION_COMMAND -> resChange(words);
                case IMAGE_COMMAND -> image_change(words[1]);
                case OUTPUT_COMMAND -> output(words[1]);
                case ASCII_ART_COMMAND -> asciiArt();
                default -> throw new Exception(INCORRECT_COMMAND);
            }
            // Moving to the next command
            System.out.print(">>> ");
            cmd = KeyboardInput.readLine();
            words = cmd.split("\\s+");
        }
    }

    private void image_change(String filePath) throws IOException {
        image = Image.fromFile(filePath);
        if(image == null) {
            throw new IOException(IMAGE_NOT_LOADED);
        }
        this.minCharsInRow = Math.max(1, image.getWidth()
                / image.getHeight());
        this.maxCharsInRow = image.getWidth() / MIN_PIXELS_PER_CHAR;
        this.charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW,
                maxCharsInRow), minCharsInRow);
        this.charMatcher = new BrightnessImgCharMatcher(image, FONT_NAME);
    }

    private void asciiArt() throws Exception {
        // Creating the ascii array
        if(charSet.isEmpty()) {
            throw new Exception(CHARSET_EMPTY);
        }
        Character[] charArray = charSet.toArray(new Character[0]);
        char[][] asciiArray = charMatcher.chooseChars(charsInRow, charArray);
        asciiOutput.out(asciiArray);
    }

    /**
     * This function will change the output of the asciiArt image
     */
    private void output(String render) throws Exception {
        if(render.equals(HTML_OUTPUT)) {
            asciiOutput = htmlAsciiOutput;
            return;
        }
        if(render.equals(CONSOLE_OUTPUT)) {
            asciiOutput = consoleAsciiOutput;
            return;
        }
        throw new Exception(INVALID_OUTPUT);
    }

    /**
     * This function will change the resolution according to the command given:
     * up - multiply rhe resolution by 2
     * down - divide by 2
     */
    private void resChange(String[] words) throws Exception {
        // Checking valid command after "res"
        if(words.length != 2 ||
                (!words[1].equals(RES_UP) && !words[1].equals(RES_DOWN))) {
            throw new Exception(INCORRECT_RESOLUTION_COMMAND);
        }
        if(words[1].equals(RES_UP)) {
            // checking we are not exceeding the maxCharsIn
            if(charsInRow == maxCharsInRow) {
                throw new Exception(EXCEED_BOUNDARIES);
            }
            // Changing the resolution
            charsInRow = charsInRow * 2;
        }
        if(words[1].equals(RES_DOWN)) {
            if(charsInRow == minCharsInRow) {
                throw new Exception(EXCEED_BOUNDARIES);
            }
            charsInRow = charsInRow / 2;
        }
        // Change made successfully, print the new resolution:
        System.out.printf(RES_MESSAGE, charsInRow);
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
                throw new Exception(INCORRECT_FORMAT);
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
        throw new Exception(INCORRECT_FORMAT);
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
                if(word.length() == 3 && word.charAt(1) == '-') {
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

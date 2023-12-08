package ascii_art;

import image.Image;
import java.util.logging.Logger;

public class Driver {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("USAGE: java asciiArt ");
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe("Failed to open image file "
                    + args[0]);
            return;
        }
        new Shell(img).run();
    }
}






// creating Html file of the image
//        Image img = Image.fromFile("ex4_ASCII_Art/board.jpeg");
//        Character[] charSet = {'a','b','c','d'};
//        BrightnessImgCharMatcher charMatcher = new BrightnessImgCharMatcher(img, "Ariel");
//        var chars = charMatcher.chooseChars(2,
//                new Character[]{'m', 'o'});
//        System.out.println(Arrays.deepToString(chars));
//
//        AsciiOutput asciiOutput = new HtmlAsciiOutput("output.html", "Ariel");
//        char[][] chars = charMatcher.chooseChars(4, charSet);
//        asciiOutput.output(chars);

//        Character[] charSet = {'a','b','c','d'};
//        Character[] charSet = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','y','x','u','v',' ','\''};
//        Image img = Image.fromFile("ex4_ASCII_Art/dino.png");
//        BrightnessImgCharMatcher charMatcher = new BrightnessImgCharMatcher(img, "Courier New");
//        HtmlAsciiOutput asciiOutput = new HtmlAsciiOutput("out1.html", "Courier New");
//        var charsInARow = img.getWidth() / 2;
//        char[][] chars = charMatcher.chooseChars(charsInARow, charSet);
//        asciiOutput.output(chars);

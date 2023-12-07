package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import image.Image;

import java.util.Arrays;

public class Driver {

    public static void main(String[] args) {
        Image image = Image.fromFile("ex4_ASCII_Art/board.jpeg");
        BrightnessImgCharMatcher charMatcher =
                new BrightnessImgCharMatcher(image, "Ariel");
        Character[] charsArr = new Character[] {'m', 'o'};
        var chars = charMatcher.chooseChars(2, charsArr);
        System.out.println(Arrays.deepToString(chars));
    }
}



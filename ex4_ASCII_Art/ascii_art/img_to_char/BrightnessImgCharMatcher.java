package ascii_art.img_to_char;

import java.util.Arrays;

public class BrightnessImgCharMatcher {

    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        return null;
    }

    /**
     * This function will get an array of characters, and return an array of
     * doubles, such that the i'th double value will be the brightness value
     * of the i'th character
     */
    private static double[] brightnessOfCharArr(char[] charArr) {
        double[] resultArr = new double[charArr.length];
        for (int i = 0; i < charArr.length; i++) {
            // Exchange character to 2D boolean array and count true values
            boolean[][] boolArr = CharRenderer.getImg(charArr[i], 16,
                    "Ariel");
            int boolArrLength = boolArr[0].length;
            resultArr[i] = countTrueValues(boolArr) /
                    (boolArrLength * boolArrLength);
        }
        return resultArr;
    }

    /**
     * This function will count the number of true values in a 2D array given
     */
    private static double countTrueValues(boolean[][] arr) {
        int counter = 0;
        for (boolean[] booleans : arr) {
            for (boolean aBoolean : booleans) {
                if (aBoolean) {
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * Just a test function for sanity checks
     */
    public static void testFunction() {
        char[] chars = {'A', 'B', 'C', 'D'};
        double[] checkResult = brightnessOfCharArr(chars);
        double num = 5;
        }
}



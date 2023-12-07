package ascii_art.img_to_char;

import java.util.Arrays;
import java.util.OptionalDouble;

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
     * This function will get an array of character's brightness levels, and
     * return the linear stretch of the array
     */
    private static double[] linearStretch(double[] brightnessLvlArr) {
        if (brightnessLvlArr == null) {
            return null;
        }
        // Assume the brightest character has higher value then the darkest one
        double minVal = Arrays.stream(brightnessLvlArr).min().getAsDouble();
        double maxVal = Arrays.stream(brightnessLvlArr).max().getAsDouble();
        double[] stretchedArr = new double[brightnessLvlArr.length];

        for (int i = 0; i < brightnessLvlArr.length; i++) {
            double curCharBrightness = brightnessLvlArr[i];
            stretchedArr[i] = (curCharBrightness - minVal) / (maxVal - minVal);
        }
        return stretchedArr;
    }

    /**
     * Just a test function for sanity checks
     */
    public static void testFunction() {
        char[] chars = {'A', 'B', 'C', 'D'};
        double[] checkResult = brightnessOfCharArr(chars);
        checkResult = linearStretch(checkResult);
        double num = 5;
        }
}



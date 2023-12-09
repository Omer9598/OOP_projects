package ascii_art.img_to_char;

import image.Image;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class BrightnessImgCharMatcher {

    private final Image image;
    private final String font;
    private final HashMap<Image, Double> cache = new HashMap<>();

    public BrightnessImgCharMatcher(Image image, String font) {
        this.image = image;
        this.font = font;
    }

    /**
     * This function will get an array of characters, and return an array of
     * doubles, such that the i'th double value will be the brightness value
     * of the i'th character
     */
    private double[] brightnessOfCharArr(Character[] charArr) {
        double[] resultArr = new double[charArr.length];
        for (int i = 0; i < charArr.length; i++) {
            // Exchange character to 2D boolean array and count true values
            boolean[][] boolArr = CharRenderer.getImg(charArr[i], 16,
                    font);
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
     * This function will calculate the average brightness of the given image
     * @return A double representing the brightness of the image
     */
    private double averageBrightness(Image image) {
        // Check if the image is in the cache already
        if(cache.containsKey(image)) {
            return cache.get(image);
        }
        double brightnessSum = 0;
        double pixelsSum = image.getWidth() * image.getWidth();
        for (Color color : image.pixels()) {
            // Changing the pixels to black-and-white
            double greyValue = (color.getRed() * 0.2126 + color.getGreen() *
                    0.7152 + color.getBlue() * 0.0722) / 255.0;
            brightnessSum += greyValue;
        }
        cache.put(image, brightnessSum / pixelsSum);
        return brightnessSum / pixelsSum;
    }

    /**
     * This function will convert an image to Ascii art:
     * 1. dividing to sub-images
     * 2. calculating brightness level to each sub-image
     * 3. finding the closest char to the brightness level
     * 4. saving the char at the right place in asciiArtArr
     * @return asciiArtArr of relevant chars
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        // The number of pixels in each subImage
        int pixels = image.getWidth() / numCharsInRow;
        char[][] asciiArt = new char[image.getHeight() / pixels]
                [image.getWidth() / pixels];
        // Stretching the charSet
        double[] charSetBrightness = brightnessOfCharArr(charSet);
        double[] stretchedCharSet = linearStretch(charSetBrightness);
        int i = 0;
        int j = 0;
        // Running on the divided image
        for (Image subImage : image.squareSubImagesOfSize(pixels)) {
            if (j == numCharsInRow) {
                j = 0;
                i++;
            }
            double averageBrightness = averageBrightness(subImage);
            int charIndex = findClosestValue(averageBrightness,
                    stretchedCharSet);
            asciiArt[i][j] = charSet[charIndex];
            j++;
        }
        return asciiArt;
    }

    /**
     * This function will return the index of the closest value in the doubles
     * array given
     */
    private static int findClosestValue(double target, double[] arr) {
        double minDifference = Math.abs(target - arr[0]);
        int closestIndex = 0;

        for (int i = 1; i < arr.length; i++) {
            double curDifference = Math.abs(target - arr[i]);
            if (curDifference < minDifference) {
                // updating index and difference
                minDifference = curDifference;
                closestIndex = i;
            }
        }
        return closestIndex;
    }
}

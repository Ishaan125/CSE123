// Ishaan Srivastava 
// 11/5/25
// CSE 123 
// P2: Mondrian Art
// TA: Aidan Suen

import java.util.*;
import java.awt.*;

// This class contains methods that fill a pixel array with colors in a 
// Mondrian style, either basic or complex.
// Complex Mondrian art has color brightness variations based on x-position.
public class Mondrian {

    // Behavior: Fills the given pixel array with a basic Mondrian artwork.
    // Exceptions: Throws an IllegalArgumentException if pixels is null or smaller than 300x300.
    // Parameters: Color[][] pixels - a 2D array representing the pixel colors of the image.
    public void paintBasicMondrian(Color[][] pixels) {
        if (pixels == null || pixels.length < 300 || pixels[0].length < 300) {
            throw new IllegalArgumentException();
        }

        helperPaint(pixels, 0, pixels[0].length, 0, pixels.length, pixels[0].length, pixels.length, false);
    }

    // Behavior: Fills the given pixel array with a complex Mondrian artwork.
    //           (Complex Mondrian tends to have darker shades in the left third, normal shades
    //           in the center third, and brighter shades in the right third.)
    // Exceptions: Throws an IllegalArgumentException if pixels is null or smaller than 300x300.
    // Parameters: Color[][] pixels - a 2D array representing the pixel colors of the image.
    public void paintComplexMondrian(Color[][] pixels) {
        if (pixels == null || pixels.length < 300 || pixels[0].length < 300) {
            throw new IllegalArgumentException();
        }

        helperPaint(pixels, 0, pixels[0].length, 0, pixels.length, pixels[0].length, pixels.length, true);
    }

    // Behavior: A helper method that paints random smaller rectangles on a canvas and
    //           fills them with colors based on whether it's a complex or basic Mondrian.
    //           Basic: fills rectangles randomly with red, yellow, cyan, or white.
    //           Complex: fills rectangles with color brightness adjusted based on x position
    //           (darker in left third, normal in center third, brighter in right third).
    // Parameters: Color[][] pixels - the pixel array to fill
    //             int x1, int x2 - the x coordinate bounds of the current rectangle
    //             int y1, int y2 - the y coordinate bounds of the current rectangle
    //             int totalX, int totalY - the total dimensions of the pixel array
    //             boolean isComplex - true if generating a complex Mondrian, false for basic
    private void helperPaint(Color[][] pixels, int x1, int x2, int y1, int y2, int totalX, 
                             int totalY, boolean isComplex) {
        // Base case: If the rectangle is small enough, fill it with color
        if ((x2 - x1 < totalX / 4) && (y2 - y1 < totalY / 4)) {
            if (isComplex) {
                fill(pixels, x1, x2, y1, y2, true);
            } 
            else {
                fill(pixels, x1, x2, y1, y2, false);
            }
        }

        // Recursive case: Divide the rectangle further
        else {
            Random rand = new Random();

            // Both dimensions are large enough to split
            if ((x2 - x1 >= totalX / 4) && (y2 - y1 >= totalY / 4)) {
                int randomX = rand.nextInt((x2 - 10) - (x1 + 10) + 1) + (x1 + 10);
                int randomY = rand.nextInt((y2 - 10) - (y1 + 10) + 1) + (y1 + 10);
                // top-left, top-right, bottom-left, bottom-right
                helperPaint(pixels, x1, randomX, y1, randomY, totalX, totalY, isComplex);
                helperPaint(pixels, randomX, x2, y1, randomY, totalX, totalY, isComplex);
                helperPaint(pixels, x1, randomX, randomY, y2, totalX, totalY, isComplex);
                helperPaint(pixels, randomX, x2, randomY, y2, totalX, totalY, isComplex);
            }

            // Only width is large enough to split
            else if (x2 - x1 >= totalX / 4) {
                int randomX = rand.nextInt((x2 - 10) - (x1 + 10) + 1) + (x1 + 10);
                helperPaint(pixels, x1, randomX, y1, y2, totalX, totalY, isComplex);
                helperPaint(pixels, randomX, x2, y1, y2, totalX, totalY, isComplex);
            }

            // Only height is large enough to split
            else if (y2 - y1 >= totalY / 4) {
                int randomY = rand.nextInt((y2 - 10) - (y1 + 10) + 1) + (y1 + 10);
                helperPaint(pixels, x1, x2, y1, randomY, totalX, totalY, isComplex);
                helperPaint(pixels, x1, x2, randomY, y2, totalX, totalY, isComplex);
            }
        }
    }

    // Behavior: Helper method that fills a specified rectangle in the pixel array with a randomly
    //           chosen color from red, yellow, cyan, and white. There is a 1 pixel border
    //           left unfilled around the rectangle and border to create the Mondrian lines.
    //           If it's a complex Mondrian, it adjusts the color brightness based on the
    //           rectangle's x position: darker shades in the left third, normal in the center
    //           third, and brighter in the right third. (Not guaranteed for every rectangle.)
    // Parameters: Color[][] pixels - the pixel array to fill
    //             int x1, int x2 - the x coordinate bounds of the rectangle
    //             int y1, int y2 - the y coordinate bounds of the rectangle
    //             boolean isComplex - true if generating a complex Mondrian, false for basic
    private void fill(Color[][] pixels, int x1, int x2, int y1, int y2, boolean isComplex) {
        int width = pixels[0].length;
        int height = pixels.length;

        // Makes sure that the coordinates are within the bounds of the canvas
        x1 = Math.max(0, x1);
        y1 = Math.max(0, y1);
        x2 = Math.min(width, x2);
        y2 = Math.min(height, y2);

        // Choose a random color
        Random rand = new Random();
        Color[] colors = {Color.RED, Color.YELLOW, Color.CYAN, Color.WHITE};
        Color color = colors[rand.nextInt(colors.length)];

        // Adjust color brightness for complex Mondrian based on x position
        if (isComplex) {
            // Darker shades tend to be in the left third
            if (x1 <= width / 3) {
                Color[] colorsDarker = {color, color.darker(), color.darker().darker()};
                color = colorsDarker[rand.nextInt(colorsDarker.length)];
            }
            // Brighter shades tend to be in the right third
            else if (x1 >= 2 * width / 3) {
                Color[] colorsLighter = {color, color.brighter(), color.brighter().brighter()};
                color = colorsLighter[rand.nextInt(colorsLighter.length)];
            }
            // Center third remains normal
        }

        // Fill the specified rectangle with the chosen color
        for (int y = y1; y < y2; y++) {
            for (int x = x1; x < x2; x++) {
                // Leave a 1 pixel border unfilled
                if ((x != x2 - 1 && y != y2 - 1) && (x != 0 && x != width && y != 0 && y != height)) {
                    pixels[y][x] = color;
                }
            }
        }
    }
}
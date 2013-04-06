package org.javaswift.joss.tutorial;

import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Utility class to resize images.
 *
 * @author <a href="mailto:oscar.westra@42.nl">Oscar Westra van Holthe - Kind</a>
 */
public class ImageUtils {

    /**
     * Method that returns a scaled instance of the provided {@code BufferedImage} with preserved aspect ratio. Either the width or the height of the returned
     * image will equal the corresponding parameter.
     *
     * @param originalImage the image to be scaled
     * @param maxWidth      the maximum width of the scaled image, in pixels
     * @param maxHeight     the maximum height of the scaled image, in pixels
     * @return a scaled version of the original {@code BufferedImage}
     */
    public static BufferedImage scaleImage(BufferedImage originalImage, int maxWidth, int maxHeight) {

        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        double scaleX = (double) maxWidth / originalWidth;
        double scaleY = (double) maxHeight / originalHeight;

        int targetWidth = maxWidth;
        int targetHeight = maxHeight;
        if (scaleX > scaleY) {
            targetWidth = (int) (originalWidth * scaleY);
        } else {
            targetHeight = (int) (originalHeight * scaleX);
        }

        return resizeImage(originalImage, targetWidth, targetHeight);
    }

    /**
     * Method that returns a scaled instance of the provided {@code BufferedImage} with exact dimensions.
     *
     * @param originalImage the image to be scaled
     * @param targetWidth   the width of the scaled image, in pixels
     * @param targetHeight  the height of the scaled image, in pixels
     * @return a scaled version of the original {@code BufferedImage}
     */
    private static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {

        //int type = originalImage.getTransparency() == OPAQUE ? TYPE_INT_RGB : TYPE_INT_ARGB;
        int type = originalImage.getType();

        BufferedImage result = originalImage;
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        do {
            width = Math.max(width / 2, targetWidth);
            height = Math.max(height / 2, targetHeight);

            BufferedImage tmp = new BufferedImage(width, height, type);
            Graphics2D graphics = tmp.createGraphics();
            graphics.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
            graphics.drawImage(result, 0, 0, width, height, null);
            graphics.dispose();

            result = tmp;
        } while (width > targetWidth || height > targetHeight);

        return result;
    }

    /**
     * Utility class: do not instantiate.
     */
    private ImageUtils() {
        // Nothing to do.
    }
}

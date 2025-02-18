package com.ange.spaceadventurefachreferat.entity.collision;

import javafx.scene.image.Image;

public class RectangleHitbox {

    private int width;
    private int height;
    private int minX;
    private int minY;

    public RectangleHitbox(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setMinX(final int minX) {
        this.minX = minX;
    }

    public void setMinY(final int minY) {
        this.minY = minY;
    }

    public boolean collidesWith(RectangleHitbox other, int centerX1, int centerY1, int centerX2, int centerY2) {
        // Calculate edges for this hitbox
        int leftA = centerX1 - width / 2 + minX;
        int rightA = centerX1 + width / 2 + minX;
        int topA = centerY1 - height / 2 + minY;
        int bottomA = centerY1 + height / 2 + minY;

        // Calculate edges for the other hitbox
        int leftB = centerX2 - other.getWidth() / 2 + other.getMinX();
        int rightB = centerX2 + other.getWidth() / 2 + other.getMinX();
        int topB = centerY2 - other.getHeight() / 2 + other.getMinY();
        int bottomB = centerY2 + other.getHeight() / 2 + other.getMinY();

        // Check for overlap
        return leftA < rightB &&
                rightA > leftB &&
                topA < bottomB &&
                bottomA > topB;
    }

    public static RectangleHitbox autoGenerateHitbox(Image image) {
        return autoGenerateHitbox(image, false);
    }

    public static RectangleHitbox autoGenerateHitbox(Image image, boolean actualSize) {
        if (!actualSize) {
            int width = (int) image.getWidth();
            int height = (int) image.getHeight();
            return new RectangleHitbox((int) image.getWidth(), (int) image.getHeight());
        }

        // Get the dimensions of the image
        double width = image.getWidth();
        double height = image.getHeight();

        // Variables to store the bounds of non-transparent pixels
        double minX = width, minY = height, maxX = 0, maxY = 0;

        // Iterate over each pixel to find the bounds of non-transparent pixels
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getPixelReader().getArgb(x, y);
                int alpha = (pixel >> 24) & 0xff; // Extract alpha component

                if (alpha != 0) { // If the pixel is not transparent
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }

        // Ensure minX, minY, maxX, and maxY are within bounds
        minX = Math.max(0, minX);
        minY = Math.max(0, minY);
        maxX = Math.min(width - 1, maxX);
        maxY = Math.min(height - 1, maxY);

        double actualWidth = maxX - minX + 1;
        double actualHeight = maxY - minY + 1;

        System.out.println("Actual width: " + actualWidth + ", Actual height: " + actualHeight);

        return new RectangleHitbox((int) actualWidth, (int) actualHeight);
    }
}

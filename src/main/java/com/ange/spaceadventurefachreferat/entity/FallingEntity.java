package com.ange.spaceadventurefachreferat.entity;

import com.ange.spaceadventurefachreferat.graphic.GameScene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

public class FallingEntity extends Entity {
    @Override
    public void loadGraphics(final int width, final int height) {
        // Just use a simple rectangle for now
        // Create player 64x64 (Capsule)
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        // Define capsule parameters
        int radius = width / 2;

        // Draw the capsule
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y < radius) {
                    // Top semi-circle
                    if (Math.pow(x - radius, 2) + Math.pow(y - radius, 2) <= Math.pow(radius, 2)) {
                        pixelWriter.setColor(x, y, Color.BLUE);
                    }
                } else if (y >= height - radius) {
                    // Bottom semi-circle
                    if (Math.pow(x - radius, 2) + Math.pow(y - (height - radius), 2) <= Math.pow(radius, 2)) {
                        pixelWriter.setColor(x, y, Color.BLUE);
                    }
                } else {
                    // Rectangle part
                    pixelWriter.setColor(x, y, Color.BLUE);
                }
            }
        }

        this.setImage(image);
    }

    @Override
    public void move(final int screenWidth, final int screenHeight, final Consumer<Void> whenReachedBorder) {
        // Move down
        this.setPos(this.getPos().getX(), this.getPos().getY() + 1);
    }

    @Override
    public GameScene getScene() {
        return null;
    }
}

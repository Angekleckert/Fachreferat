package com.ange.spaceadventurefachreferat.entity.impl;

import com.ange.spaceadventurefachreferat.entity.Entity;
import com.ange.spaceadventurefachreferat.entity.pos.Position;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class StarEntity extends Entity {

    @Override
    public void loadGraphics(int width, int height) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        // Draw a more realistic star shape
        int centerX = width / 2;
        int centerY = height / 2;
        int outerRadius = Math.min(width, height) / 2;
        int innerRadius = outerRadius / 2;

        // Calculate the points of the star
        double[] xPoints = new double[10];
        double[] yPoints = new double[10];
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 5 * i;
            int radius = (i % 2 == 0) ? outerRadius : innerRadius;
            xPoints[i] = centerX + radius * Math.cos(angle);
            yPoints[i] = centerY - radius * Math.sin(angle);
        }

        // Draw the star
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isPointInPolygon(x, y, xPoints, yPoints)) {
                    pixelWriter.setColor(x, y, Color.WHITE);
                } else {
                    pixelWriter.setColor(x, y, Color.TRANSPARENT);
                }
            }
        }

        // Use the image in your entity
        this.setImage(image);
    }

    private boolean isPointInPolygon(int x, int y, double[] xPoints, double[] yPoints) {
        boolean result = false;
        for (int i = 0, j = xPoints.length - 1; i < xPoints.length; j = i++) {
            if ((yPoints[i] > y) != (yPoints[j] > y) &&
                    (x < (xPoints[j] - xPoints[i]) * (y - yPoints[i]) / (yPoints[j] - yPoints[i]) + xPoints[i])) {
                result = !result;
            }
        }
        return result;
    }

    @Override
    public void move(int screenWidth, int screenHeight, Consumer<Void> whenReachedBorder) {
        // Move down the screen (y-axis), when the star reaches the bottom, call the whenReachedBorder consumer, star should move down clean
        Position pos = this.getPos();
        pos.addY(this.getSpeed());
        if (pos.getY() > screenHeight) {
            pos.setY(0);
            whenReachedBorder.accept(null);
        }
    }

    @Override
    public GameScene getScene() {
        return null;
    }

    public static class StarManager {

        protected List<StarEntity> starEntities;

        public void createStars(int amount, double width, double height) {
            List<StarEntity> stars = new ArrayList<>();

            for (int i = 0; i < amount; i++) {
                StarEntity star = new StarEntity();
                int size = (int) (Math.random() * 10) + 3;
                star.setSpeed((float) (Math.random() * 5) + 1);
                star.loadGraphics(size, size);
                star.setPos((int) (Math.random() * width), (int) (Math.random() * height));
                stars.add(star);
            }

            this.starEntities = stars;
        }

        public void doStarMovement(GraphicsContext gc, double width, double height) {
            gc.clearRect(0, 0, width, height);

            // Set background color
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, width, height);

            // Draw stars
            for (StarEntity star : this.starEntities) {
                star.move((int) width, (int) height, (Void) -> {
                });
                gc.drawImage(star.getImage(), star.getPos().getX(), star.getPos().getY());
            }

        }
    }
}

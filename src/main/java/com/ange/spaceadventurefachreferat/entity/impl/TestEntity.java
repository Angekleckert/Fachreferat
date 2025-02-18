package com.ange.spaceadventurefachreferat.entity.impl;

import com.ange.spaceadventurefachreferat.entity.Collideable;
import com.ange.spaceadventurefachreferat.entity.Entity;
import com.ange.spaceadventurefachreferat.entity.collision.RectangleHitbox;
import com.ange.spaceadventurefachreferat.entity.pos.Position;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.function.Consumer;

public class TestEntity extends Entity implements Collideable {

    @Override
    public boolean collidesWith(final Collideable other) {
        return false;
    }

    @Override
    public void onCollision(final Collideable other) {
        System.out.println("Collision with " + other.getClass().getSimpleName());
    }

    @Override
    public Position getPosition() {
        return this.getPos();
    }

    @Override
    public RectangleHitbox getHitbox() {
        return RectangleHitbox.autoGenerateHitbox(this.getImage());
    }

    @Override
    public int getCollisionDamage() {
        return 0;
    }

    @Override
    public void loadGraphics(final int width, final int height) {
        // Draw circle with radius 9
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        int centerX = width / 2;
        int centerY = height / 2;
        int radius = 20;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2) <= Math.pow(radius, 2)) {
                    pixelWriter.setColor(x, y, javafx.scene.paint.Color.WHITE);
                } else {
                    pixelWriter.setColor(x, y, javafx.scene.paint.Color.TRANSPARENT);
                }
            }
        }

        this.setImage(image);
    }

    @Override
    public void move(final int screenWidth, final int screenHeight, final Consumer<Void> whenReachedBorder) {
        // Move entity
        Position pos = this.getPos();
       // pos.setY(pos.getY() + this.getSpeed());
    }

    @Override
    public GameScene getScene() {
        return null;
    }
}

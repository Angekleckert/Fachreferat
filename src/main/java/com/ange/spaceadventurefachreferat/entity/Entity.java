package com.ange.spaceadventurefachreferat.entity;

import com.ange.spaceadventurefachreferat.entity.pos.Position;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import javafx.scene.image.Image;

import java.util.function.Consumer;

public abstract class Entity {

    private Position pos;
    private Image image;

    private float speed;

    public abstract void loadGraphics(int width, int height);

    public abstract void move(int screenWidth, int screenHeight, Consumer<Void> whenReachedBorder);

    public abstract GameScene getScene();

    public void setPos(float x, float y) {
        if (pos == null) {
            pos = new Position();
        }

        pos.set(x, y);
    }

    public Position getPos() {
        return pos;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }


}

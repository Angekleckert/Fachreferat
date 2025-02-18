package com.ange.spaceadventurefachreferat.entity.pos;

public class Position {

    private float x;
    private float y;
    private float rotation;

    public void set(float x, float y) {
        this.x += x;
        this.y += y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public void addX(float x) {
        this.x += x;
    }

    public void addY(float y) {
        this.y += y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void subX(float x) {
        this.x -= x;
    }

    public void subY(float y) {
        this.y -= y;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getRotation() {
        return this.rotation;
    }

    public double distanceTo(final Position positionB) {
        return Math.sqrt(Math.pow(this.x - positionB.x, 2) + Math.pow(this.y - positionB.y, 2));
    }
}

package com.ange.spaceadventurefachreferat.entity;

import javafx.scene.image.Image;

public abstract class ShootingEntity extends Entity {

    private Image bulletImage;
    private int bulletSpeed;
    private int bulletDamage;

    public void setBulletImage(Image bulletImage) {
        this.bulletImage = bulletImage;
    }

    public Image getBulletImage() {
        return bulletImage;
    }

    public void setBulletSpeed(int bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }

    public int getBulletSpeed() {
        return bulletSpeed;
    }

    public void setBulletDamage(int bulletDamage) {
        this.bulletDamage = bulletDamage;
    }

    public int getBulletDamage() {
        return bulletDamage;
    }

    public abstract void shoot();

    public abstract void loadBulletGraphics(int width, int height);
}

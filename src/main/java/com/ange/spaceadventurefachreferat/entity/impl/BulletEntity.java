package com.ange.spaceadventurefachreferat.entity.impl;

import com.ange.spaceadventurefachreferat.entity.Collideable;
import com.ange.spaceadventurefachreferat.entity.Entity;
import com.ange.spaceadventurefachreferat.entity.ShootingEntity;
import com.ange.spaceadventurefachreferat.entity.collision.RectangleHitbox;
import com.ange.spaceadventurefachreferat.entity.pos.Position;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.function.Consumer;

public class BulletEntity extends Entity implements Collideable {

    private BulletType bulletType;
    private GameScene scene;
    private ShootingEntity shooter;

    public BulletEntity(GameScene scene, BulletType bulletType, ShootingEntity shooter) {
        this.bulletType = bulletType;
        this.scene = scene;
        this.shooter = shooter;
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    @Override
    public void loadGraphics(final int width, final int height) {
        Image image = null;
        if (bulletType == BulletType.PLAYER_BULLET) {
            image = new Image(this.getClass().getResourceAsStream("/graphics/player/shoot.png"));
            image = new Image(this.getClass().getResourceAsStream("/graphics/player/shoot.png"), image.getWidth() * 0.25, image.getHeight() * 0.25, false, false);
        } else if (bulletType == BulletType.ENEMY_BULLET) {
            image = new Image(this.getClass().getResourceAsStream("/graphics/alien/bullet.png"));
            image = new Image(this.getClass().getResourceAsStream("/graphics/alien/bullet.png"), image.getWidth() * 0.25, image.getHeight() * 0.25, false, false);
        }
        this.setImage(image);
    }

    @Override
    public void move(final int screenWidth, final int screenHeight, final Consumer<Void> whenReachedBorder) {
        // Up for player bullets, down for enemy bullets
        float direction = (bulletType == BulletType.PLAYER_BULLET) ? -1 : 1;

        // Move bullet
        Position pos = this.getPos();
        pos.setY(pos.getY() + direction * this.getSpeed());

        // Check if bullet reached border
        if (pos.getY() < 0 || pos.getY() > screenHeight) {
            // Remove bullet from scene
            this.scene.removeEntity(this);
        }
    }

    @Override
    public GameScene getScene() {
        return this.scene;
    }

    @Override
    public boolean collidesWith(final Collideable other) {
        return false;
    }

    @Override
    public void onCollision(final Collideable other) {
        if (other instanceof final ShootingEntity shootingEntity) {
            if (shootingEntity == this.shooter) {
                return;
            }
        }
        if (other instanceof final BulletEntity bulletEntity) {
            if (bulletEntity.getBulletType() == this.bulletType) {
                return;
            }
        }
        System.out.println("Collision with " + other.getClass().getSimpleName());
        this.scene.removeEntity(this);
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
        return 10;
    }

    public ShootingEntity getShooter() {
        return shooter;
    }

    public enum BulletType {
        PLAYER_BULLET,
        ENEMY_BULLET
    }
}

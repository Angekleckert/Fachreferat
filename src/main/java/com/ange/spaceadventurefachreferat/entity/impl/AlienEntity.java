package com.ange.spaceadventurefachreferat.entity.impl;

import com.ange.spaceadventurefachreferat.entity.Collideable;
import com.ange.spaceadventurefachreferat.entity.Damageable;
import com.ange.spaceadventurefachreferat.entity.ShootingEntity;
import com.ange.spaceadventurefachreferat.entity.collision.RectangleHitbox;
import com.ange.spaceadventurefachreferat.entity.pos.Position;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import com.ange.spaceadventurefachreferat.graphic.scenes.GameOverScene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class AlienEntity extends ShootingEntity implements Damageable, Collideable {

    private final Image[] images = new Image[3];
    private int HEALTH = 60;
    private final GameScene scene;
    private int animationTicks = 10;
    private boolean died = false;
    private long shootCooldown = 0;

    public AlienEntity(GameScene scene) {
        this.scene = scene;
    }

    @Override
    public void takeDamage(final int damage) {
        HEALTH -= damage;

        if (HEALTH <= 0) {
            this.setImage(images[2]);
            this.died = true;
        }
    }


    @Override
    public void heal(final int heal) {
        HEALTH += heal;
    }

    @Override
    public int getHealth() {
        return HEALTH;
    }

    @Override
    public void setHealth(final int health) {
        HEALTH = health;
    }

    @Override
    public void loadGraphics(final int width, final int height) {
        // Load meteorite images from resources
        try {
            images[0] = new Image(getClass().getResourceAsStream("/graphics/alien/alien1_0.png"));
            images[1] = new Image(getClass().getResourceAsStream("/graphics/alien/alien1_1.png"));
            images[2] = new Image(getClass().getResourceAsStream("/graphics/alien/alien1_2.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setImage(images[0]);
    }

    private long moveNextDown = 0;
    private long moveDown = 0;
    private boolean isMovingDown = false;
    private long nextShoot = 0;
    private int moveFactor = 1;

    @Override
    public void move(final int screenWidth, final int screenHeight, final Consumer<Void> whenReachedBorder) {
        // Move on x axis
        Position pos = this.getPos();
        pos.setX(pos.getX() + (moveFactor * this.getSpeed()));
        if (pos.getX() > screenWidth) {
            moveFactor = -1;
        }

        if (pos.getX() < 0) {
            moveFactor = 1;
        }

        // Move on y axis
        if (moveNextDown < System.currentTimeMillis()) {
            this.isMovingDown = true;
            moveNextDown = System.currentTimeMillis() + 1000;
        }

        if (this.isMovingDown) {
            if (moveDown < System.currentTimeMillis()) {
                pos.setY(pos.getY() + 0.5f);
                moveDown = 0;
            }
        }

        if (nextShoot < System.currentTimeMillis()) {
            this.shoot();
            nextShoot = System.currentTimeMillis() + 1000;
        }

        // Remove if reaches bottom border
        if (pos.getY() > screenHeight) {
            this.scene.removeEntity(this);
        }

        if (this.died) {
            this.animationTicks--;
            if (this.animationTicks <= 0) {
                this.scene.removeEntity(this);
            }
        }
    }

    @Override
    public GameScene getScene() {
        return this.scene;
    }

    @Override
    public void shoot() {
        BulletEntity bullet = new BulletEntity(this.scene, BulletEntity.BulletType.ENEMY_BULLET, this);
        bullet.loadGraphics(10, 10);
        bullet.setPos((getPos().getX() + getHitbox().getMinX() + ((float) getHitbox().getWidth() / 2)), (float) (getPos().getY() + (getImage().getWidth() / 2)));
        bullet.setSpeed(5f);
        this.scene.addEntity(bullet);
    }

    @Override
    public void loadBulletGraphics(final int width, final int height) {
        WritableImage image = new WritableImage(width, height);
        PixelWriter pixelWriter = image.getPixelWriter();

        // Define bullet parameters
        int radius = width / 2;

        // Draw the bullet
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y < radius) {
                    // Top semi-circle
                    if (Math.pow(x - radius, 2) + Math.pow(y - radius, 2) <= Math.pow(radius, 2)) {
                        pixelWriter.setColor(x, y, Color.RED);
                    }
                } else if (y >= height - radius) {
                    // Bottom semi-circle
                    if (Math.pow(x - radius, 2) + Math.pow(y - (height - radius), 2) <= Math.pow(radius, 2)) {
                        pixelWriter.setColor(x, y, Color.RED);
                    }
                } else {
                    // Rectangle part
                    pixelWriter.setColor(x, y, Color.RED);
                }
            }
        }

        this.setBulletImage(image);
    }

    @Override
    public boolean collidesWith(final Collideable other) {
        return false;
    }

    @Override
    public void onCollision(final Collideable other) {
        if (HEALTH > 40) {
            this.setImage(images[0]);
        } else if (HEALTH > 20) {
            this.setImage(images[1]);
        }

        if (other instanceof final BulletEntity bullet) {
            if (bullet.getShooter() == this)
                return;
        }

        if (other instanceof MeteoriteEntity) {
            return;
        }

        this.takeDamage(other.getCollisionDamage());

        if (this.died && other instanceof BulletEntity bulletEntity) {
            if (bulletEntity.getShooter() instanceof PlayerEntity playerEntity) {
                playerEntity.setScore(playerEntity.getScore() + 10);
            }
        }
    }

    @Override
    public Position getPosition() {
        return this.getPos();
    }

    @Override
    public RectangleHitbox getHitbox() {
        RectangleHitbox hitbox = new RectangleHitbox(65, 75);
        hitbox.setMinX(30);
        hitbox.setMinY(30);
        return hitbox;
    }

    @Override
    public int getCollisionDamage() {
        return 10;
    }

    @Override
    public int getMaxHealth() {
        return 60;
    }
}

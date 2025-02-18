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

import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Consumer;

public class PlayerEntity extends ShootingEntity implements Damageable, Collideable {

    private int HEALTH = 100;
    private final GameScene scene;
    private boolean immune = false;
    private int immuneTicks = 16;
    private long shootCooldown = 0;

    private int score = 0;

    public PlayerEntity(GameScene scene) {
        this.scene = scene;
    }

    @Override
    public void takeDamage(final int damage) {
        if (immune)
            return;
        HEALTH -= damage;
        immune = true;
        if (HEALTH <= 0) {
            // Game over
            this.scene.onSceneDestroyed();
            GameOverScene gameOverScene = new GameOverScene(this.scene.getStage(), scene.getSpaceAdventure(), score);
            gameOverScene.onSceneCreated();
            scene.getSpaceAdventure().getRenderManager().setScene(gameOverScene);
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
        // Load player image from resources
        try {
            Path resourceDir = Paths.get("src/main/resources");
            printDirectoryTree(resourceDir, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image image = new Image(this.getClass().getResourceAsStream("/graphics/player/rocket.png"));
        // Scale image to 0.5 of its original size
        image = new Image(this.getClass().getResourceAsStream("/graphics/player/rocket.png"), image.getWidth() * 0.8, image.getHeight() * 0.8, false, false);
        this.setImage(image);
    }


    private void printDirectoryTree(Path dir, int level) throws IOException {
        Files.walk(dir, 1)
                .filter(path -> !path.equals(dir))
                .forEach(path -> {
                    printIndent(level);
                    System.out.println(path.getFileName());
                    if (Files.isDirectory(path)) {
                        try {
                            printDirectoryTree(path, level + 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void printIndent(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
    }

    @Override
    public void move(final int screenWidth, final int screenHeight, final Consumer<Void> whenReachedBorder) {
        if (immune) {
            immuneTicks--;
            if (immuneTicks <= 0) {
                immune = false;
                immuneTicks = 8;
            }
        }
    }

    @Override
    public GameScene getScene() {
        return this.scene;
    }

    @Override
    public void shoot() {
        System.out.println("SHOTTTT");
        System.out.println(shootCooldown - System.currentTimeMillis());
        if (shootCooldown > System.currentTimeMillis()) {
            System.out.println("Cooldown");
            return;
        }
        shootCooldown = System.currentTimeMillis() + 300;
        BulletEntity bullet = new BulletEntity(this.scene, BulletEntity.BulletType.PLAYER_BULLET, this);
        bullet.loadGraphics(10, 10);
        bullet.setPos((getPos().getX() + getHitbox().getMinX() + ((float) getHitbox().getWidth() / 2)), getPos().getY());
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
        if (other instanceof final BulletEntity bullet) {
            if (bullet.getShooter() == this)
                return;
        }

        this.takeDamage(other.getCollisionDamage());
    }

    @Override
    public Position getPosition() {
        return this.getPos();
    }

    @Override
    public RectangleHitbox getHitbox() {
        RectangleHitbox hitbox = new RectangleHitbox(70, 82);
        hitbox.setMinX(15);
        hitbox.setMinY(5);
        return hitbox;
    }

    @Override
    public int getCollisionDamage() {
        return 10;
    }

    public boolean isImmune() {
        return immune;
    }

    public int getImmuneTicks() {
        return immuneTicks;
    }

    public int getScore() {
        return score;
    }

    public void setScore(final int score) {
        this.score = score;
    }

    @Override
    public int getMaxHealth() {
        return 100;
    }
}

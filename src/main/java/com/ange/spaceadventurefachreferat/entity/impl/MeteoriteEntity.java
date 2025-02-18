package com.ange.spaceadventurefachreferat.entity.impl;

import com.ange.spaceadventurefachreferat.entity.Collideable;
import com.ange.spaceadventurefachreferat.entity.Damageable;
import com.ange.spaceadventurefachreferat.entity.Entity;
import com.ange.spaceadventurefachreferat.entity.collision.RectangleHitbox;
import com.ange.spaceadventurefachreferat.entity.pos.Position;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import javafx.scene.image.Image;

import java.util.function.Consumer;

public class MeteoriteEntity extends Entity implements Damageable, Collideable {

    private final Image[] images = new Image[4];
    private int health = 100;
    private int animationTicks = 10;
    private boolean died = false;

    private final GameScene scene;

    public MeteoriteEntity(GameScene scene) {
        this.scene = scene;
    }

    @Override
    public boolean collidesWith(final Collideable other) {
        return false;
    }

    @Override
    public void onCollision(final Collideable other) {
        if (other instanceof AlienEntity) {
            return;
        }

        if (this.died) {
            return;
        }
        this.takeDamage(this.getCollisionDamage());

        if (this.health > 75) {
            this.setImage(images[0]);
        } else if (this.health > 50) {
            this.setImage(images[1]);
        } else if (this.health > 25) {
            this.setImage(images[2]);
        }

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
        RectangleHitbox hitbox = new RectangleHitbox(70, 80);
        hitbox.setMinX(30);
        hitbox.setMinY(13);
        return hitbox;
    }

    @Override
    public int getCollisionDamage() {
        return 20;
    }

    @Override
    public void loadGraphics(final int width, final int height) {
        // Load meteorite images from resources
        try {
            images[0] = new Image(getClass().getResourceAsStream("/graphics/meteroite/meteroite_0.png"));
            images[1] = new Image(getClass().getResourceAsStream("/graphics/meteroite/meteroite_1.png"));
            images[2] = new Image(getClass().getResourceAsStream("/graphics/meteroite/meteroite_2.png"));
            images[3] = new Image(getClass().getResourceAsStream("/graphics/meteroite/meteroite_3.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setImage(images[0]);
    }

    @Override
    public void move(final int screenWidth, final int screenHeight, final Consumer<Void> whenReachedBorder) {
        // Move down
        Position pos = this.getPos();
        pos.addY(this.getSpeed());
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
    public void takeDamage(final int damage) {
        this.health -= damage;

        if (this.health <= 0) {
            this.setImage(images[3]);
            this.died = true;
        }
    }

    @Override
    public void heal(final int heal) {
        this.health += heal;
    }

    @Override
    public int getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(final int health) {
        this.health = health;
    }

    @Override
    public int getMaxHealth() {
        return 100;
    }
}

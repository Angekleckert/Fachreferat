package com.ange.spaceadventurefachreferat.graphic.scenes;

import com.ange.spaceadventurefachreferat.SpaceAdventure;
import com.ange.spaceadventurefachreferat.enemy.AlienSpawner;
import com.ange.spaceadventurefachreferat.enemy.MeteoriteSpawner;
import com.ange.spaceadventurefachreferat.entity.*;
import com.ange.spaceadventurefachreferat.entity.collision.RectangleHitbox;
import com.ange.spaceadventurefachreferat.entity.impl.*;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PlayScene implements GameScene {

    private Stage stage;
    private Canvas canvas;
    private boolean isRunning = true;
    private final SpaceAdventure INSTANCE;

    private java.util.Collections Collections;
    // Entity List
    private final List<Entity> entities = new CopyOnWriteArrayList<>();

    // Spawner
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);
    private MeteoriteSpawner meteoriteSpawner;
    private AlienSpawner alienSpawner;


    // Movement boolean
    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    // Hitbox boolean
    private final boolean drawHitbox = false;

    private final StarEntity.StarManager starManager = new StarEntity.StarManager();

    public PlayScene(Stage stage, SpaceAdventure spaceAdventure) {
        this.stage = stage;
        this.INSTANCE = spaceAdventure;
    }

    private TestEntity testEntityA;
    private TestEntity testEntityB;
    private PlayerEntity playerEntity;

    @Override
    public void onSceneCreated() {
        this.starManager.createStars(50, this.stage.getWidth(), this.stage.getHeight());
        // Create canvas for Star animation
        this.canvas = new Canvas(this.stage.getWidth(), this.stage.getHeight());

        // Group containing the canvas and the BorderPane
        Scene scene = getScene();
        this.playerEntity = new PlayerEntity(this);
        playerEntity.loadGraphics(64, 64);
        playerEntity.setPos((float) (this.getCanvas().getWidth() / 2) - 50, (float) (this.canvas.getHeight() - 200));
        playerEntity.setSpeed(2f);

        this.addEntity(playerEntity);

        stage.setScene(scene);
        this.INSTANCE.getRenderManager().setScene(this);

        // Create Spawner
        this.meteoriteSpawner = new MeteoriteSpawner(this, executorService);
        this.alienSpawner = new AlienSpawner(this, executorService);

        System.out.println("Scene switched to PlayScene");
        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0, 0, this.stage.getWidth(), this.stage.getHeight());
    }

    private Scene getScene() {
        Group group = new Group(this.canvas);

        // create a scene
        Scene scene = new Scene(group, 200, 200);

        // Set key press event
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case ESCAPE:
                    if (this.isRunning) {
                        this.pause();
                    } else {
                        this.resume();
                    }
                    break;
                case W:
                    this.moveUp = true;
                    break;
                case S:
                    this.moveDown = true;
                    break;
                case A:
                    this.moveLeft = true;
                    break;
                case D:
                    this.moveRight = true;
                    break;
            }
        });

        // Set key release event
        scene.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case W:
                    this.moveUp = false;
                    break;
                case S:
                    this.moveDown = false;
                    break;
                case A:
                    this.moveLeft = false;
                    break;
                case D:
                    this.moveRight = false;
                    break;
                case SPACE:
                    this.playerEntity.shoot();
                    break;
            }
        });

        return scene;
    }

    @Override
    public void onSceneDestroyed() {
        this.isRunning = false;
        this.executorService.shutdown();
        this.entities.clear();
        this.INSTANCE.getCollisionManager().clearCollideables();
        System.out.println("Scene destroyed");
    }

    @Override
    public void pause() {
        this.isRunning = false;
        this.meteoriteSpawner.setPaused(true);
        this.alienSpawner.setPaused(true);
    }

    @Override
    public void resume() {
        this.isRunning = true;
        this.meteoriteSpawner.setPaused(false);
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void update() {
        if (this.canvas != null && this.isRunning) {
            this.starManager.doStarMovement(canvas.getGraphicsContext2D(), this.stage.getWidth(), this.stage.getHeight());

            // Update entities
            this.INSTANCE.getCollisionManager().checkCollisions();

            if (drawHitbox) {
                // Draw Hitboxes
                this.INSTANCE.getCollisionManager().getCollideables().forEach(collideable -> {
                    RectangleHitbox hitbox = collideable.getHitbox();
                    canvas.getGraphicsContext2D().setStroke(Color.RED);
                    canvas.getGraphicsContext2D().strokeRect(collideable.getPosition().getX() + hitbox.getMinX(), collideable.getPosition().getY() + hitbox.getMinY(), hitbox.getWidth(), hitbox.getHeight());
                });
            }

            this.moveEntity(playerEntity);
        }
    }

    @Override
    public List<Entity> getEntities() {
        return this.entities;
    }

    @Override
    public Canvas getCanvas() {
        return this.canvas;
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public SpaceAdventure getSpaceAdventure() {
        return this.INSTANCE;
    }

    @Override
    public void addEntity(final Entity entity) {
        this.entities.add(entity);
        if (entity instanceof Collideable) {
            this.INSTANCE.getCollisionManager().addCollideable((Collideable) entity);
        }
    }

    @Override
    public void removeEntity(final Entity entity) {
        this.entities.remove(entity);
        if (entity instanceof Collideable) {
            this.INSTANCE.getCollisionManager().removeCollideable((Collideable) entity);
        }
    }

    private void moveEntity(PlayerEntity entity) {
        if (this.moveUp) {
            // Make sure player does not leave the screen
            if (entity.getPos().getY() <= 2) {
                return;
            }
            entity.getPos().addY(-5);
        }
        if (this.moveDown) {
            // Make sure player does not leave the screen
            if (entity.getPos().getY() >= this.canvas.getHeight() - 80) {
                return;
            }
            entity.getPos().addY(5);
        }
        if (this.moveLeft) {
            // Make sure player does not leave the screen
            if (entity.getPos().getX() <= -30) {
                return;
            }

            entity.getPos().addX(-5);
        }
        if (this.moveRight) {
            // Make sure player does not leave the screen
            if (entity.getPos().getX() >= this.canvas.getWidth() - 80) {
                return;
            }
            entity.getPos().addX(5);
        }
    }
}

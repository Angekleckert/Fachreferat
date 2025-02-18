package com.ange.spaceadventurefachreferat.graphic;

import com.ange.spaceadventurefachreferat.enemy.EnemySpawner;
import com.ange.spaceadventurefachreferat.entity.Collideable;
import com.ange.spaceadventurefachreferat.entity.Damageable;
import com.ange.spaceadventurefachreferat.entity.Entity;
import com.ange.spaceadventurefachreferat.entity.impl.PlayerEntity;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {

    private long lastUpdate = 0;
    private GameScene scene;
    private List<EnemySpawner<?>> enemySpawners = new ArrayList<>();

    public void startRenderLoop(GraphicsContext gc, int fps, int width, int height) {
        final long frameTime = 1_000_000_000 / fps;

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdate >= frameTime) {
                    render();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    int ballHeight = 0;
    boolean ballDirection = true;

    public void render() {
        if (scene != null && scene.isRunning()) {
            scene.update();

            // Summon enemies
            for (EnemySpawner<?> enemySpawner : enemySpawners) {
                enemySpawner.update();
            }

            if (scene.getEntities() != null) {
                Canvas canvas = scene.getCanvas();
                System.out.println("Rendering " + scene.getEntities().size() + " entities");
                for (Entity entity : scene.getEntities()) {
                    if (entity != null) {
                        // Draw the entity
                        if (entity instanceof PlayerEntity playerEntity) {
                            // Make player flicker when immune
                            if (playerEntity.isImmune()) {
                                if (playerEntity.getImmuneTicks() % 3 == 0) {
                                    canvas.getGraphicsContext2D().drawImage(playerEntity.getImage(), playerEntity.getPos().getX(), playerEntity.getPos().getY());
                                }
                            } else {
                                canvas.getGraphicsContext2D().drawImage(playerEntity.getImage(), playerEntity.getPos().getX(), playerEntity.getPos().getY());
                            }
                        } else {
                            canvas.getGraphicsContext2D().drawImage(entity.getImage(), entity.getPos().getX(), entity.getPos().getY());
                        }

                        // Move the entity
                        entity.move((int) canvas.getWidth(), (int) canvas.getHeight(), (v) -> {
                            System.out.println("Border reached");
                        });

                        // Draw life bar if entity is damageable and not player
                        if (entity instanceof Collideable collideable && entity instanceof final Damageable damageable && !(entity instanceof PlayerEntity)) {
                            double maxHealth = damageable.getMaxHealth();
                            double currentHealth = damageable.getHealth();
                            double barWidth = 100;
                            double barHeight = 5;
                            double healthBarWidth = (currentHealth / maxHealth) * barWidth;
                            double x = entity.getPos().getX() + (double) collideable.getHitbox().getMinX() / 2;
                            double y = entity.getPos().getY() - 10;

                            // Draw border with rounded corners
                            canvas.getGraphicsContext2D().setStroke(javafx.scene.paint.Color.BLACK);
                            canvas.getGraphicsContext2D().strokeRoundRect(x - 1, y - 1, barWidth + 2, barHeight + 2, 5, 5);

                            // Draw background with shadow
                            canvas.getGraphicsContext2D().setFill(javafx.scene.paint.Color.RED);
                            canvas.getGraphicsContext2D().fillRoundRect(x, y, barWidth, barHeight, 5, 5);
                            canvas.getGraphicsContext2D().setEffect(new javafx.scene.effect.DropShadow(5, javafx.scene.paint.Color.GRAY));

                            // Draw health bar with gradient
                            javafx.scene.paint.LinearGradient gradient = new javafx.scene.paint.LinearGradient(
                                    0, 0, 1, 0, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                                    new javafx.scene.paint.Stop(0, javafx.scene.paint.Color.GREEN),
                                    new javafx.scene.paint.Stop(1, javafx.scene.paint.Color.YELLOW)
                            );
                            canvas.getGraphicsContext2D().setFill(gradient);
                            canvas.getGraphicsContext2D().fillRoundRect(x, y, healthBarWidth, barHeight, 5, 5);

                            // Remove shadow effect
                            canvas.getGraphicsContext2D().setEffect(null);
                        }

                        // Draw health bar for player in top right corner but with sother look
                        if (entity instanceof PlayerEntity playerEntity) {
                            double barWidth = 100;
                            double barHeight = 10;
                            double x = canvas.getWidth() - barWidth - 10 - 20;
                            double y = 10;

                            // Draw border with rounded corners
                            canvas.getGraphicsContext2D().setStroke(javafx.scene.paint.Color.BLACK);
                            canvas.getGraphicsContext2D().strokeRoundRect(x - 1, y - 1, barWidth + 2, barHeight + 2, 5, 5);

                            // Draw background with shadow
                            canvas.getGraphicsContext2D().setFill(javafx.scene.paint.Color.RED);
                            canvas.getGraphicsContext2D().fillRoundRect(x, y, barWidth, barHeight, 5, 5);
                            canvas.getGraphicsContext2D().setEffect(new javafx.scene.effect.DropShadow(5, javafx.scene.paint.Color.GRAY));

                            // Draw health bar with gradient
                            javafx.scene.paint.LinearGradient healthGradient = new javafx.scene.paint.LinearGradient(
                                    0, 0, 1, 0, true, javafx.scene.paint.CycleMethod.NO_CYCLE,
                                    new javafx.scene.paint.Stop(0, javafx.scene.paint.Color.GREEN),
                                    new javafx.scene.paint.Stop(1, javafx.scene.paint.Color.YELLOW)
                            );
                            canvas.getGraphicsContext2D().setFill(healthGradient);
                            canvas.getGraphicsContext2D().fillRoundRect(x, y, playerEntity.getHealth(), barHeight, 5, 5);

                            // Remove shadow effect
                            canvas.getGraphicsContext2D().setEffect(null);

                            // Draw score in top left corner
                            double scoreX = 10;
                            double scoreY = 30;
                            double scoreWidth = 120;
                            double scoreHeight = 30;

                            canvas.getGraphicsContext2D().setFill(javafx.scene.paint.Color.rgb(64, 64, 64, 0.5)); // Dark gray with 50% transparency
                            canvas.getGraphicsContext2D().fillRoundRect(scoreX, scoreY - scoreHeight + 5, scoreWidth, scoreHeight, 10, 10);

                            canvas.getGraphicsContext2D().setStroke(javafx.scene.paint.Color.BLACK);
                            canvas.getGraphicsContext2D().strokeRoundRect(scoreX, scoreY - scoreHeight + 5, scoreWidth, scoreHeight, 10, 10);

                            canvas.getGraphicsContext2D().setFill(javafx.scene.paint.Color.WHITE);
                            canvas.getGraphicsContext2D().setFont(new javafx.scene.text.Font("Arial", 20));
                            canvas.getGraphicsContext2D().fillText("Score: " + playerEntity.getScore(), scoreX + 10, scoreY - 5);
                        }
                    }
                }
            }
        }
    }

    public void setScene(final GameScene scene) {
        this.scene = scene;
    }

    public GameScene getScene() {
        return this.scene;
    }

    public void addEnemySpawner(final EnemySpawner<?> enemySpawner) {
        this.enemySpawners.add(enemySpawner);
    }
}

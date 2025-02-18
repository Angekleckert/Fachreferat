package com.ange.spaceadventurefachreferat.enemy;

import com.ange.spaceadventurefachreferat.SpaceAdventure;
import com.ange.spaceadventurefachreferat.entity.Entity;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import com.ange.spaceadventurefachreferat.graphic.scenes.PlayScene;

import java.lang.reflect.InvocationTargetException;

public class EnemySpawner<T extends Entity> {

    private T enemy;
    private int spawnRate;
    private int spawnCounter;
    private final SpaceAdventure spaceAdventure;

    public EnemySpawner(T enemy, int spawnRate, SpaceAdventure spaceAdventure) {
        this.enemy = enemy;
        this.spawnRate = spawnRate;
        this.spawnCounter = 0;
        this.spaceAdventure = spaceAdventure;
        this.spaceAdventure.getRenderManager().addEnemySpawner(this);
    }

    public void update() {
        spawnCounter++;
        if (spawnCounter >= spawnRate) {
            spawnCounter = 0;
            spawnEnemy();
        }
    }

    private void spawnEnemy() {
        GameScene currentScene = spaceAdventure.getRenderManager().getScene();
        if (currentScene instanceof PlayScene) {
            // Create a new instance of the enemy
            Entity enemy;
            try {
                enemy = (Entity) this.enemy.getClass().getConstructors()[0].newInstance();
                enemy.loadGraphics(64, 64);
                enemy.setPos((int) (Math.random() * 720), 0);
                currentScene.addEntity(enemy);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public T getEnemy() {
        return enemy;
    }

    public void setEnemy(T enemy) {
        this.enemy = enemy;
    }

    public int getSpawnRate() {
        return spawnRate;
    }

    public void setSpawnRate(int spawnRate) {
        this.spawnRate = spawnRate;
    }

    public int getSpawnCounter() {
        return spawnCounter;
    }

    public void setSpawnCounter(int spawnCounter) {
        this.spawnCounter = spawnCounter;
    }
}

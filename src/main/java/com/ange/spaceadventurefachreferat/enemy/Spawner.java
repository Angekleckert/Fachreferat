package com.ange.spaceadventurefachreferat.enemy;

import com.ange.spaceadventurefachreferat.graphic.scenes.PlayScene;
import javafx.scene.Scene;

public abstract class Spawner {

    private PlayScene scene;
    private boolean isPaused = false;

    public Spawner(PlayScene scene) {
        this.scene = scene;
    }

    public abstract void spawnEnemy();

    public PlayScene getScene() {
        return scene;
    }

    public void setScene(PlayScene scene) {
        this.scene = scene;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }
}

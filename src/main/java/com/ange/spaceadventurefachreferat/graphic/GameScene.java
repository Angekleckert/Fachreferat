package com.ange.spaceadventurefachreferat.graphic;

import com.ange.spaceadventurefachreferat.SpaceAdventure;
import com.ange.spaceadventurefachreferat.entity.Entity;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.List;

public interface GameScene {

    void onSceneCreated();

    void onSceneDestroyed();

    void pause();

    void resume();

    boolean isRunning();

    void update();

    List<Entity> getEntities();

    Canvas getCanvas();

    Stage getStage();

    SpaceAdventure getSpaceAdventure();

    void addEntity(Entity entity);

    void removeEntity(Entity entity);

}

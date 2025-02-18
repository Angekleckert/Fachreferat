package com.ange.spaceadventurefachreferat;

import com.ange.spaceadventurefachreferat.entity.collision.CollisionManager;
import com.ange.spaceadventurefachreferat.entity.collision.impl.CollisionManagerImpl;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import com.ange.spaceadventurefachreferat.graphic.RenderManager;
import com.ange.spaceadventurefachreferat.graphic.scenes.StartScreenScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SpaceAdventure extends Application {

    private final int WIDTH = 720;
    private final int HEIGHT = 480;
    private RenderManager renderManager;
    private CollisionManager collisionManager;

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("SpaceAdventure Fachreferat (Almin)");
        Canvas canvas = new Canvas(320, 240);

        stage.setResizable(false);

        // Start render loop with 60 PFS
        this.renderManager = new RenderManager();
        renderManager.startRenderLoop(canvas.getGraphicsContext2D(), 60, WIDTH, HEIGHT);

        // Create collision manager
        this.collisionManager = new CollisionManagerImpl();

        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);

        GameScene startScreen = new StartScreenScene(stage, this);
        startScreen.onSceneCreated();

        renderManager.setScene(startScreen);
      //stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public RenderManager getRenderManager() {
        return this.renderManager;
    }

    public CollisionManager getCollisionManager() {
        return this.collisionManager;
    }
}
package com.ange.spaceadventurefachreferat.graphic.scenes;

import com.ange.spaceadventurefachreferat.SpaceAdventure;
import com.ange.spaceadventurefachreferat.entity.Entity;
import com.ange.spaceadventurefachreferat.entity.impl.StarEntity;
import com.ange.spaceadventurefachreferat.graphic.GameScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

public class StartScreenScene implements GameScene {

    private Stage stage;
    private Canvas canvas;
    private boolean isRunning = true;
    private final SpaceAdventure INSTANCE;

    public StartScreenScene(Stage stage, SpaceAdventure spaceAdventure) {
        this.stage = stage;
        this.INSTANCE = spaceAdventure;
    }

    private final StarEntity.StarManager starManager = new StarEntity.StarManager();

    @Override
    public void onSceneCreated() {
        this.starManager.createStars(50, this.stage.getWidth(), this.stage.getHeight());
        // Create canvas for Star animation
        this.canvas = new Canvas(this.stage.getWidth(), this.stage.getHeight());

        // Create beautiful start screen
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(this.stage.getWidth(), this.stage.getHeight());

        // Title
        Text title = new Text("Space Adventure");
        title.setFont(new Font("Arial", 30));
        title.setFill(Color.WHITE);
        BorderPane.setAlignment(title, Pos.CENTER);
        title.getStyleClass().add("text"); // Apply the CSS class to the title

        // Add title to a VBox to adjust its position
        VBox titleBox = new VBox(title);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(20, 0, 0, 0)); // Add some padding at the top
        titleBox.getStylesheets().add(getClass().getResource("/styles/title.css").toExternalForm());

        borderPane.setTop(titleBox);

        // Center Pane with Button
        StackPane centerPane = new StackPane();
        centerPane.setAlignment(Pos.CENTER); // Ensure the StackPane is centered

        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            this.pause();

            // Switch to the play scene
            GameScene playScene = new PlayScene(this.stage, this.INSTANCE);
            playScene.onSceneCreated();
        });
        startButton.getStylesheets().add(getClass().getResource("/styles/button.css").toExternalForm());

        centerPane.getChildren().add(startButton);
        borderPane.setCenter(centerPane);

        // Group containing the canvas and the BorderPane
        Group group = new Group(this.canvas, borderPane);

        // create a scene
        Scene scene = new Scene(group, 200, 200);
        stage.setScene(scene);
        System.out.println("Scene Created");
        stage.show();

        canvas.getGraphicsContext2D().setFill(Color.BLACK);
        canvas.getGraphicsContext2D().fillRect(0, 0, this.stage.getWidth(), this.stage.getHeight());
    }

    @Override
    public void onSceneDestroyed() {

    }

    @Override
    public void pause() {
        this.isRunning = false;
    }

    @Override
    public void resume() {
        this.isRunning = true;
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void update() {
        if (isRunning && canvas != null) {
            // Do star animation
            this.starManager.doStarMovement(canvas.getGraphicsContext2D(), this.stage.getWidth(), this.stage.getHeight());
        }
    }

    @Override
    public List<Entity> getEntities() {
        return null;
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
        throw new UnsupportedOperationException("Cannot add entities to the start screen");
    }

    @Override
    public void removeEntity(final Entity entity) {
        throw new UnsupportedOperationException("Cannot remove entities from the start screen");
    }
}

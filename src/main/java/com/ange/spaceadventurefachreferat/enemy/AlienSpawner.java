package com.ange.spaceadventurefachreferat.enemy;

import com.ange.spaceadventurefachreferat.entity.impl.AlienEntity;
import com.ange.spaceadventurefachreferat.entity.impl.MeteoriteEntity;
import com.ange.spaceadventurefachreferat.graphic.scenes.PlayScene;

import java.util.SplittableRandom;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlienSpawner extends Spawner {
    private ScheduledExecutorService executorService;

    public AlienSpawner(final PlayScene scene, final ScheduledExecutorService executorService) {
        super(scene);
        this.executorService = executorService;

        this.spawnEnemy();
    }

    @Override
    public void spawnEnemy() {
        SplittableRandom random = new SplittableRandom();
        this.executorService.scheduleAtFixedRate(() -> {
            AlienEntity alienEntity = new AlienEntity(getScene());

            alienEntity.setPos(random.nextInt(0, (int) getScene().getCanvas().getWidth()), -100);
            alienEntity.setSpeed(random.nextFloat(2, 4)); // Random speed from 5 to 15
            alienEntity.loadGraphics(128, 128);
            this.getScene().addEntity(alienEntity);
        }, 0, 25, TimeUnit.SECONDS);
    }
}

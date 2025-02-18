package com.ange.spaceadventurefachreferat.enemy;

import com.ange.spaceadventurefachreferat.entity.impl.MeteoriteEntity;
import com.ange.spaceadventurefachreferat.graphic.scenes.PlayScene;

import java.util.SplittableRandom;
import java.util.concurrent.*;

public class MeteoriteSpawner extends Spawner {
    private ScheduledExecutorService executorService;

    public MeteoriteSpawner(final PlayScene scene, final ScheduledExecutorService executorService) {
        super(scene);
        this.executorService = executorService;

        this.spawnEnemy();
    }

    @Override
    public void spawnEnemy() {
        SplittableRandom random = new SplittableRandom();
        this.executorService.scheduleAtFixedRate(() -> {
            MeteoriteEntity meteoriteEntity0 = new MeteoriteEntity(getScene());

            meteoriteEntity0.setPos(random.nextInt(0, (int) getScene().getCanvas().getWidth()), -100);
            meteoriteEntity0.setSpeed(random.nextFloat(2, 4)); // Random speed from 5 to 15
            meteoriteEntity0.loadGraphics(128, 128);
            this.getScene().addEntity(meteoriteEntity0);
        }, 0, 2, TimeUnit.SECONDS);
    }
}

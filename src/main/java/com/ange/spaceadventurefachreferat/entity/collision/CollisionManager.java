package com.ange.spaceadventurefachreferat.entity.collision;

import com.ange.spaceadventurefachreferat.entity.Collideable;

import java.util.List;

public interface CollisionManager {

    void checkCollisions();

    void addCollideable(Collideable collideable);

    void removeCollideable(Collideable collideable);

    List<Collideable> getCollideables();

    void clearCollideables();

}

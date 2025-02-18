package com.ange.spaceadventurefachreferat.entity.collision.impl;

import com.ange.spaceadventurefachreferat.entity.Collideable;
import com.ange.spaceadventurefachreferat.entity.collision.CollisionManager;
import com.ange.spaceadventurefachreferat.entity.collision.RectangleHitbox;
import com.ange.spaceadventurefachreferat.entity.impl.AlienEntity;
import com.ange.spaceadventurefachreferat.entity.pos.Position;
import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CollisionManagerImpl implements CollisionManager {

    private final Map<Collideable, Long> collideables = new ConcurrentHashMap<>();

    @Override
    public void checkCollisions() {
        for (Collideable collideable : this.collideables.keySet()) {
            for (Collideable other : this.collideables.keySet()) {
                Position positionA = collideable.getPosition();
                Position positionB = other.getPosition();

                RectangleHitbox hitboxA = collideable.getHitbox();
                RectangleHitbox hitboxB = other.getHitbox();

                if (collideable != other && hitboxA.collidesWith(hitboxB, (int) positionA.getX() + hitboxA.getWidth() / 2, (int) positionA.getY() + hitboxA.getHeight() / 2, (int) positionB.getX() + hitboxB.getWidth() / 2, (int) positionB.getY() + hitboxB.getHeight() / 2)) {
                    collideable.onCollision(other);
                    other.onCollision(collideable);
                }
            }
        }
    }

    @Override
    public void addCollideable(final Collideable collideable) {
        this.collideables.put(collideable, 0L);
    }

    @Override
    public void removeCollideable(final Collideable collideable) {
        this.collideables.remove(collideable);
    }

    @Override
    public List<Collideable> getCollideables() {
        return this.collideables.keySet().stream().toList();
    }

    @Override
    public void clearCollideables() {
        this.collideables.clear();
    }
}

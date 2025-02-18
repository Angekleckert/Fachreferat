package com.ange.spaceadventurefachreferat.entity;

import com.ange.spaceadventurefachreferat.entity.collision.RectangleHitbox;
import com.ange.spaceadventurefachreferat.entity.pos.Position;

public interface Collideable {

    boolean collidesWith(Collideable other);

    void onCollision(Collideable other);

    Position getPosition();

    RectangleHitbox getHitbox();

    int getCollisionDamage();
}

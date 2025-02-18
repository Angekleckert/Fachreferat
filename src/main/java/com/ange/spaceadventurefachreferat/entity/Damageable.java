package com.ange.spaceadventurefachreferat.entity;

public interface Damageable {
    void takeDamage(int damage);

    void heal(int heal);

    int getHealth();

    void setHealth(int health);

    int getMaxHealth();

}

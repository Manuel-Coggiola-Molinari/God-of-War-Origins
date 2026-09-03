package com.goworigins.player;

import com.goworigins.animation.PlayerAnimation;

public class Player {

    private float x;
    private float y;

    private float speed;
    private PlayerAnimation animation;

    private int health = 100;
    private boolean damaged = false;

    public Player(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;

        animation = new PlayerAnimation();
    }

    public void move(float deltaX, float deltaY) {
        x += deltaX;
        y += deltaY;

        animation.updateDirection(deltaX, deltaY);
    }
    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSpeed() {
        return speed;
    }

    public PlayerAnimation.Direction getDirection() {
        return animation.getDirection();
    }

    public void update(float delta) {
        animation.update(delta);
    }

    public void takeDamage(int damage) {

        health -= damage;

        if (health < 0)
        { health = 0; }

        damaged = true;
    }
    public boolean consumeDamage() {

        if (damaged) {
            damaged = false;
            return true; }

        return false;
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }
}

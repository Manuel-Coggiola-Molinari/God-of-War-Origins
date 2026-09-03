package com.goworigins.enemy;

import com.goworigins.animation.EnemyAnimationState;
import com.goworigins.animation.EnemyAnimations;

public class Enemy {

    private float x;
    private float y;

    private float width;
    private float height;

    private int health;

    private EnemyAnimations animations;

    public Enemy(float x, float y, int health) {
        this.x = x;
        this.y = y;

        this.width = 50;
        this.height = 50;

        this.health = health;

        animations = new EnemyAnimations();
    }

    public void takeDamage(int damage) {

        health -= damage;

        if (health < 0) {
            health = 0;
        }

        if (health > 0) {

            animations.setState(
                EnemyAnimationState.DAMAGE
            );
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getHealth() { return health; }

    public void move(float deltaX, float deltaY) {
        x += deltaX;
        y += deltaY; }

    public float getCenterX() { return x + width / 2f; }

    public float getCenterY() { return y + height / 2f; }

    public EnemyAnimations getAnimations() {
        return animations;
    }

    public void updateAnimations(float delta) {
        animations.update(delta);
    }
}

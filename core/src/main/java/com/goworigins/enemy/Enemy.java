package com.goworigins.enemy;

public class Enemy {

    private float x;
    private float y;

    private final float width;
    private final float height;

    private int health;
    private final EnemyType type;

    private boolean facingLeft = false;

    public Enemy(float x, float y, int health) {
        this(x, y, health, EnemyType.COMMON);
    }

    public Enemy(float x, float y, int health, EnemyType type) {
        this.x = x;
        this.y = y;
        this.width = 50;
        this.height = 50;
        this.health = health;
        this.type = type;
    }

    public void takeDamage(int damage) {
        if (!isAlive()) {
            return;
        }

        health -= damage;

        if (health < 0) {
            health = 0;
        }
    }

    public boolean isAlive() {
        return health > 0;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public int getHealth() { return health; }
    public EnemyType getType() { return type; }

    public void setFacingLeft(boolean facingLeft) {
        this.facingLeft = facingLeft;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public void move(float deltaX, float deltaY) {
        x += deltaX;
        y += deltaY;
    }

    public float getCenterX() {
        return x + width / 2f;
    }

    public float getCenterY() {
        return y + height / 2f;
    }
}

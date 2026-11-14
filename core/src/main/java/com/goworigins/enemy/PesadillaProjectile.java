package com.goworigins.enemy;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PesadillaProjectile {

    private float x;
    private float y;

    private final float size = 12;
    private final float speed = 220;
    private final int damage = 10;

    private final Vector2 direction;

    private boolean active = true;
    private boolean impacting = false;

    private float lifetime = 4;
    private float impactTime = 0;

    private final float impactDuration = 0.20f;

    public PesadillaProjectile(
        float x,
        float y,
        float targetX,
        float targetY
    ) {
        this.x = x;
        this.y = y;

        direction = new Vector2(targetX - x, targetY - y);

        if (direction.len2() > 0) {
            direction.nor();
        }
    }

    public void update(float delta) {

        if (active) {

            x += direction.x * speed * delta;
            y += direction.y * speed * delta;

            lifetime -= delta;

            if (lifetime <= 0) {
                startImpact();
            }

        } else if (impacting) {

            impactTime += delta;

            if (impactTime >= impactDuration) {
                impacting = false;
            }
        }
    }

    public boolean hitsPlayer(float playerX, float playerY) {

        Rectangle projectileBounds =
            new Rectangle(x, y, size, size);

        Rectangle playerBounds =
            new Rectangle(playerX, playerY, 50, 50);

        return active && projectileBounds.overlaps(playerBounds);
    }

    public void startImpact() {
        if (!active) return;

        active = false;
        impacting = true;
        impactTime = 0;
    }

    public int getDamage() {
        return damage;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSize() {
        return size;
    }

    public boolean isActive() {
        return active || impacting;
    }

    public boolean isMoving() {
        return active;
    }

    public boolean isImpacting() {
        return impacting;
    }

    public float getImpactTime() {
        return impactTime;
    }

    public void deactivate() {
        active = false;
        impacting = false;
    }
}

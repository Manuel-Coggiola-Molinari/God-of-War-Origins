package com.goworigins.building;

import com.badlogic.gdx.math.Rectangle;

public class Building {

    private float x;
    private float y;

    private final float width = 400;
    private final float height = 400;

    private final BuildingType type;
    private final Rectangle hitbox;

    public Building(float x, float y, BuildingType type) {
        this.x = x;
        this.y = y;
        this.type = type;

        hitbox = new Rectangle(
            x,
            y,
            width,
            height
        );
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

    public BuildingType getType() {
        return type;
    }
    public Rectangle getHitbox() {
        return hitbox;
    }
}

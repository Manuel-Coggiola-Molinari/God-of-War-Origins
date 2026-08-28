package com.goworigins.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimation {

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
    public enum State {
        IDLE,
        WALKING
    }

    private Direction direction;
    private State state;
    private Animation<TextureRegion> idleUp;
    private Animation<TextureRegion> idleDown;
    private Animation<TextureRegion> idleLeft;
    private Animation<TextureRegion> idleRight;

    private Animation<TextureRegion> walkUp;
    private Animation<TextureRegion> walkDown;
    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkRight;
    private float animationTime;

    public PlayerAnimation() {
        direction = Direction.DOWN;
        state = State.IDLE;
    }

    public void updateDirection(float movementX, float movementY) {

        if (movementX == 0 && movementY == 0) {
            state = State.IDLE;
            return;
        }

        state = State.WALKING;

        if (Math.abs(movementX) > Math.abs(movementY)) {
            if (movementX > 0) {
                direction = Direction.RIGHT;
            } else {
                direction = Direction.LEFT;
            }
        } else {
            if (movementY > 0) {
                direction = Direction.UP;
            } else {
                direction = Direction.DOWN;
            }
        }
    }

    public Direction getDirection() {
        return direction;
    }
    public State getState() {
        return state;
    }
    public Animation<TextureRegion> getCurrentAnimation() {

        if (state == State.IDLE) {
            switch (direction) {
                case UP:
                    return idleUp;
                case DOWN:
                    return idleDown;
                case LEFT:
                    return idleLeft;
                case RIGHT:
                    return idleRight;
            }
        } else {
            switch (direction) {
                case UP:
                    return walkUp;
                case DOWN:
                    return walkDown;
                case LEFT:
                    return walkLeft;
                case RIGHT:
                    return walkRight;
            }
        }

        return null;
    }    public void update(float delta) {
        if (state == State.WALKING) {
            animationTime += delta;
        } else {
            animationTime = 0;
        }
    }
}

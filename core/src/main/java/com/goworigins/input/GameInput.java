package com.goworigins.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class GameInput extends InputAdapter {

    private boolean up;
    private boolean down;
    private boolean left;
    private boolean right;
    private boolean attackPressed;
    private boolean buildPressed;

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
                up = true;
                break;

            case Input.Keys.S:
                down = true;
                break;

            case Input.Keys.A:
                left = true;
                break;

            case Input.Keys.D:
                right = true;
                break;

            case Input.Keys.E:
                buildPressed = true;
                break;
        }
        return true;
    }

    public boolean consumeBuild() {
        if (buildPressed) {
            buildPressed = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {
            case Input.Keys.W:
                up = false;
                break;

            case Input.Keys.S:
                down = false;
                break;

            case Input.Keys.A:
                left = false;
                break;

            case Input.Keys.D:
                right = false;
                break;
        }

        return true;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            attackPressed = true;
        }
        return true;
    }

    public boolean consumeAttack() {
        if (attackPressed) {
            attackPressed = false;
            return true;
        }
        return false;
    }
}

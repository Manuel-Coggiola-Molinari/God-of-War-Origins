package com.goworigins.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goworigins.animation.EnemyAnimationState;
import com.goworigins.enemy.Enemy;

public class EnemyRenderer {

    private SpriteBatch batch;

    private boolean facingLeft = false;

    public EnemyRenderer(SpriteBatch batch) {
        this.batch = batch;
    }

    public void render(Enemy enemy) {

        TextureRegion frame = enemy.getAnimations()
            .getCurrentAnimation()
            .getCurrentFrame();

        if (frame == null) {
            return;
        }

        float width = 80;
        float height = 128;

        if (
            enemy.getAnimations().getCurrentState()
                == EnemyAnimationState.ATTACK
        ) {
            width = 120;
            height = 167;
        }

        TextureRegion drawFrame = new TextureRegion(frame);

        if (facingLeft) {
            drawFrame.flip(true, false);
        }

        float drawX = enemy.getX() + enemy.getWidth() / 2f - width / 2f;

        float drawY = enemy.getY() - (128 - 80);

        if (enemy.getAnimations().getCurrentState() == EnemyAnimationState.ATTACK) {
            drawY = enemy.getY() - (167 - 128);
        }

        batch.draw(
            drawFrame,
            drawX,
            drawY,
            width,
            height
        );
    }

    public void setFacing(float directionX) {

        if (directionX < 0) {
            facingLeft = true;
        }

        if (directionX > 0) {
            facingLeft = false;
        }
    }
}

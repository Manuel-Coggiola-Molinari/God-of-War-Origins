package com.goworigins.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goworigins.animation.EnemyAnimationState;
import com.goworigins.enemy.Draugr;

public class DraugrRenderer {

    private final SpriteBatch batch;

    public DraugrRenderer(SpriteBatch batch) {
        this.batch = batch;
    }

    public void render(Draugr draugr) {

        TextureRegion frame = draugr.getAnimations()
            .getCurrentAnimation()
            .getCurrentFrame();

        if (frame == null) {
            return;
        }

        float width = 80;
        float height = 128;

        if (draugr.getAnimations().getCurrentState()
            == EnemyAnimationState.ATTACK) {
            width = 120;
            height = 167;
        }

        TextureRegion drawFrame = new TextureRegion(frame);

        if (draugr.isFacingLeft()) {
            drawFrame.flip(true, false);
        }

        float drawX =
            draugr.getX() + draugr.getWidth() / 2f - width / 2f;

        float drawY = draugr.getY() - (128 - 80);

        if (draugr.getAnimations().getCurrentState()
            == EnemyAnimationState.ATTACK) {
            drawY = draugr.getY() - (167 - 128);
        }

        batch.draw(drawFrame, drawX, drawY, width, height);
    }
}

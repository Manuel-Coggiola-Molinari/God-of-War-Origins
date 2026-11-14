package com.goworigins.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.goworigins.animation.PesadillaAnimations;
import com.goworigins.enemy.Pesadilla;
import com.goworigins.enemy.PesadillaProjectile;

public class PesadillaRenderer {

    private static final float SCALE = 1f / 3f;

    private final SpriteBatch batch;

    public PesadillaRenderer(SpriteBatch batch) {
        this.batch = batch;
    }

    public void render(Pesadilla enemy) {

        TextureRegion frame = enemy.getAnimations().getCurrentFrame();

        if (frame == null) return;

        TextureRegion drawFrame = new TextureRegion(frame);

        if (enemy.isFacingLeft()) {
            drawFrame.flip(true, false);
        }

        float width = frame.getRegionWidth() * SCALE;
        float height = frame.getRegionHeight() * SCALE;

        float drawX = enemy.getCenterX() - width / 2f;
        float drawY = enemy.getY() +
            (enemy.getHeight() - height) / 2f;

        batch.draw(drawFrame, drawX, drawY, width, height);
    }

    public void renderProjectile(
        PesadillaProjectile projectile,
        PesadillaAnimations animations
    ) {

        TextureRegion frame;

        if (projectile.isMoving()) {

            frame = animations.getProjectileFrame();

        } else if (projectile.isImpacting()) {

            frame = animations.getImpactFrame(
                projectile.getImpactTime()
            );

        } else {
            return;
        }

        float width = frame.getRegionWidth() * SCALE;
        float height = frame.getRegionHeight() * SCALE;

        float centerX = projectile.getX() +
            projectile.getSize() / 2f;

        float centerY = projectile.getY() +
            projectile.getSize() / 2f;

        batch.draw(
            frame,
            centerX - width / 2f,
            centerY - height / 2f,
            width,
            height
        );
    }
}

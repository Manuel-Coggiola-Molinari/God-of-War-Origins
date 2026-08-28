package com.goworigins.render;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.goworigins.player.Player;
import com.goworigins.animation.AnimationManager;
import com.goworigins.animation.PlayerAnimations;
import com.goworigins.animation.AnimationState;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerRenderer {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private AnimationManager animationManager;
    private PlayerAnimations animations;

    private boolean facingLeft = false;

    public PlayerRenderer(
        ShapeRenderer shapeRenderer,
        SpriteBatch batch
    ) {
        this.shapeRenderer = shapeRenderer;
        this.batch = batch;
    }

    public void render(Player player) {

        TextureRegion frame = getCurrentFrame();

        if (frame != null) {

            /*
             * Tamaño normal del personaje.
             */
            float width = 100;
            float height = 100;

            /*
             * La animación de muerte puede ocupar un poco
             * más de espacio para que se vea correctamente.
             */
            if (
                animations.getCurrentState()
                    == AnimationState.DEATH
            ) {
                width = 120;
                height = 120;
            }

            /*
             * Creamos una copia del frame para poder
             * invertirlo sin modificar el frame original.
             */
            TextureRegion drawFrame =
                new TextureRegion(frame);

            if (facingLeft) {
                drawFrame.flip(true, false);
            }

            /*
             * El jugador tiene una posición de referencia
             * en su hitbox de 50x50.
             *
             * Calculamos el centro y dibujamos el sprite
             * desde ahí para que quede centrado.
             */
            float playerCenterX =
                player.getX() + 25;

            float playerCenterY =
                player.getY() + 25;

            float drawX =
                playerCenterX - width / 2f;

            float drawY =
                playerCenterY - height / 2f;

            batch.draw(
                drawFrame,
                drawX,
                drawY,
                width,
                height
            );

        } else {

            /*
             * Si todavía no existe una animación,
             * mostramos el cuadrado de prueba.
             */
            shapeRenderer.rect(
                player.getX(),
                player.getY(),
                50,
                50
            );
        }
    }

    public void setAnimation(
        AnimationManager animationManager
    ) {
        this.animationManager = animationManager;
    }

    public void update(float delta) {

        if (animations != null) {
            animations.update(delta);
        }
    }

    public void setAnimations(
        PlayerAnimations animations
    ) {
        this.animations = animations;
    }

    public void setFacing(float directionX) {

        if (directionX < 0) {
            facingLeft = true;
        }

        if (directionX > 0) {
            facingLeft = false;
        }
    }

    public TextureRegion getCurrentFrame() {

        if (
            animations == null ||
                animations.getCurrentAnimation() == null
        ) {
            return null;
        }

        return animations
            .getCurrentAnimation()
            .getCurrentFrame();
    }
}

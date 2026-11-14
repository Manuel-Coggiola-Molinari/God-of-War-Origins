package com.goworigins.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PesadillaAnimations {

    private final Texture idleTexture;
    private final Texture attackTexture;
    private final Texture damageTexture;
    private final Texture projectileTexture;
    private final Texture impactTexture;

    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> attackAnimation;
    private final Animation<TextureRegion> damageAnimation;
    private final Animation<TextureRegion> impactAnimation;

    private final TextureRegion projectileFrame;

    private float stateTime = 0;

    public enum State {
        IDLE,
        ATTACK,
        DAMAGE,
        DEATH
    }

    private State currentState = State.IDLE;

    public PesadillaAnimations() {

        String path = "sprites/Enemies/Pesadilla/";

        // Cargar spritesheets.
        idleTexture = new Texture(
            Gdx.files.internal(path + "idle.png")
        );

        attackTexture = new Texture(
            Gdx.files.internal(path + "ataque.png")
        );

        damageTexture = new Texture(
            Gdx.files.internal(path + "daño.png")
        );

        projectileTexture = new Texture(
            Gdx.files.internal(path + "bolaEnergia.png")
        );

        impactTexture = new Texture(
            Gdx.files.internal(path + "impacto.png")
        );

        // Dividir spritesheets en fotogramas.
        TextureRegion[] idleFrames =
            TextureRegion.split(idleTexture, 96, 144)[0];

        TextureRegion[] attackFrames =
            TextureRegion.split(attackTexture, 103, 90)[0];

        TextureRegion[] damageFrames =
            TextureRegion.split(damageTexture, 120, 90)[0];

        TextureRegion[] impactFrames =
            TextureRegion.split(impactTexture, 59, 64)[0];

        // Crear animaciones.
        idleAnimation = new Animation<>(
            0.15f,
            idleFrames
        );

        idleAnimation.setPlayMode(
            Animation.PlayMode.LOOP_PINGPONG
        );

        attackAnimation = new Animation<>(
            0.12f,
            attackFrames
        );

        attackAnimation.setPlayMode(
            Animation.PlayMode.NORMAL
        );

        damageAnimation = new Animation<>(
            0.15f,
            damageFrames
        );

        damageAnimation.setPlayMode(
            Animation.PlayMode.NORMAL
        );

        impactAnimation = new Animation<>(
            0.10f,
            impactFrames
        );

        impactAnimation.setPlayMode(
            Animation.PlayMode.NORMAL
        );

        // La bola de energía tiene un único fotograma.
        projectileFrame =
            TextureRegion.split(projectileTexture, 62, 40)[0][0];
    }

    public void setState(State newState) {

        if (currentState == State.DEATH) {
            return;
        }

        if (currentState != newState) {
            currentState = newState;
            stateTime = 0;
        }
    }

    public void update(float delta) {

        stateTime += delta;
    }

    public TextureRegion getCurrentFrame() {

        switch (currentState) {

            case IDLE:
                return idleAnimation.getKeyFrame(stateTime);

            case ATTACK:
                return attackAnimation.getKeyFrame(stateTime, false);

            case DAMAGE:
                return damageAnimation.getKeyFrame(stateTime, false);

            case DEATH:
                return damageAnimation.getKeyFrame(0);

            default:
                return idleAnimation.getKeyFrame(stateTime);
        }
    }

    public TextureRegion getImpactFrame(float time) {
        return impactAnimation.getKeyFrame(time, false);
    }

    public boolean isImpactFinished(float time) {
        return impactAnimation.isAnimationFinished(time);
    }

    public TextureRegion getProjectileFrame() {
        return projectileFrame;
    }

    public State getCurrentState() {
        return currentState;
    }

    public boolean isAttackFinished() {
        return attackAnimation.isAnimationFinished(stateTime);
    }

    public boolean isDamageFinished() {
        return damageAnimation.isAnimationFinished(stateTime);
    }

    public void dispose() {
        idleTexture.dispose();
        attackTexture.dispose();
        damageTexture.dispose();
        projectileTexture.dispose();
        impactTexture.dispose();
    }
}

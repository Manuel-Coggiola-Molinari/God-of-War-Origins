package com.goworigins.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

    private Music backgroundMusic;

    private Sound attackSound;
    private Sound buildSound;

    private Sound playerHitSound;
    private Sound enemyAttackSound;
    private Sound enemyHitSound;
    private Sound enemyDeathSound;

    private float volume = 1f;
    private boolean muted = false;

    public AudioManager(String musicPath) {

        backgroundMusic =
            Gdx.audio.newMusic(Gdx.files.internal(musicPath));

        attackSound =
            Gdx.audio.newSound(
                Gdx.files.internal("audio/sfx/characters/kratos/attack.wav")
            );

        buildSound =
            Gdx.audio.newSound(
                Gdx.files.internal("audio/sfx/builds/shelter.wav")
            );

        playerHitSound =
            Gdx.audio.newSound(
                Gdx.files.internal("audio/sfx/characters/kratos/damage.wav")
            );

        enemyAttackSound =
            Gdx.audio.newSound(
                Gdx.files.internal("audio/sfx/enemies/draugr/attack.wav")
            );

        enemyHitSound =
            Gdx.audio.newSound(
                Gdx.files.internal("audio/sfx/enemies/draugr/damage.wav")
            );

        enemyDeathSound =
            Gdx.audio.newSound(
                Gdx.files.internal("audio/sfx/enemies/draugr/death.wav")
            );

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(volume);
        backgroundMusic.play();
    }

    public void playAttackSound() {
        if (!muted) {
            attackSound.play(volume);
        }
    }

    public void playBuildSound() {
        if (!muted) {
            buildSound.play(volume);
        }
    }

    public void playPlayerHitSound() {
        if (!muted) {
            playerHitSound.play(volume);
        }
    }

    public void playEnemyAttackSound() {
        if (!muted) {
            enemyAttackSound.play(volume);
        }
    }

    public void playEnemyHitSound() {
        if (!muted) {
            enemyHitSound.play(volume);
        }
    }

    public void playEnemyDeathSound() {
        if (!muted) {
            enemyDeathSound.play(volume);
        }
    }

    public void setVolume(float volume) {

        this.volume = Math.max(
            0f,
            Math.min(volume, 1f)
        );

        if (!muted) {
            backgroundMusic.setVolume(this.volume);
        }
    }

    public void increaseVolume() {
        setVolume(volume + 0.1f);
    }

    public void decreaseVolume() {
        setVolume(volume - 0.1f);
    }

    public void toggleMute() {

        muted = !muted;

        if (muted) {
            backgroundMusic.setVolume(0f);
        } else {
            backgroundMusic.setVolume(volume);
        }
    }

    public boolean isMuted() {
        return muted;
    }

    public float getVolume() {
        return volume;
    }

    public void dispose() {

        backgroundMusic.dispose();

        attackSound.dispose();
        buildSound.dispose();

        playerHitSound.dispose();
        enemyAttackSound.dispose();
        enemyHitSound.dispose();
        enemyDeathSound.dispose();
    }
}

package com.goworigins.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager {

    private Music backgroundMusic;
    private Sound attackSound;

    private float volume = 1f;
    private boolean muted = false;

    public AudioManager() {

        backgroundMusic =
            Gdx.audio.newMusic(
                Gdx.files.internal("audio/music/background.mp3")
            );

        attackSound =
            Gdx.audio.newSound(
                Gdx.files.internal("audio/sfx/attack.wav")
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
    }
}

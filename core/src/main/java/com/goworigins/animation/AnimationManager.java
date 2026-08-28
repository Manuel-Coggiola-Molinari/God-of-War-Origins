package com.goworigins.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationManager {

    private final TextureRegion[] frames;

    private float frameDuration;
    private float stateTime = 0f;

    private boolean looping = true;

    public AnimationManager(
        String[] framePaths,
        float frameDuration
    ) {

        frames = new TextureRegion[framePaths.length];

        for (int i = 0; i < framePaths.length; i++) {

            Texture texture = new Texture(framePaths[i]);

            frames[i] = new TextureRegion(texture);
        }

        this.frameDuration = frameDuration;
    }

    public void update(float delta) {
        stateTime += delta;
    }

    public TextureRegion getCurrentFrame() {

        int frameNumber = (int)(stateTime / frameDuration);

        if (looping) {
            frameNumber %= frames.length;
        } else {

            frameNumber = Math.min(frameNumber, frames.length - 1);
        }
        return frames[frameNumber];
    }

    public boolean isFinished() {

        if (looping) {
            return false;
        }

        return stateTime >= frameDuration * frames.length;
    }

    public void reset() {
        stateTime = 0f;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }
}

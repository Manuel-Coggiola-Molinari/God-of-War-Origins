
package com.goworigins.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationManager {

    private final TextureRegion[] frames;
    private final Texture[] textures;

    private final float frameDuration;
    private float stateTime = 0f;

    private boolean looping = true;

    public AnimationManager(
        String[] framePaths,
        float frameDuration
    ) {

        if (framePaths == null || framePaths.length == 0) {
            throw new IllegalArgumentException("No hay frames para cargar.");
        }

        if (frameDuration <= 0) {
            throw new IllegalArgumentException("La duración de frame debe ser mayor a cero.");
        }

        frames = new TextureRegion[framePaths.length];
        textures = new Texture[framePaths.length];

        for (int i = 0; i < framePaths.length; i++) {

            textures[i] = new Texture(framePaths[i]);
            frames[i] = new TextureRegion(textures[i]);
        }

        this.frameDuration = frameDuration;
    }

    public AnimationManager(
        String spritesheetPath,
        int frameWidth,
        int frameHeight,
        float frameDuration
    ) {

        if (frameWidth <= 0 || frameHeight <= 0 || frameDuration <= 0) {
            throw new IllegalArgumentException("Dimensiones o duración inválidas.");
        }

        textures = new Texture[1];
        textures[0] = new Texture(spritesheetPath);

        TextureRegion[][] tmp = TextureRegion.split(
            textures[0],
            frameWidth,
            frameHeight
        );

        int rows = tmp.length;
        int columns = tmp[0].length;

        frames = new TextureRegion[rows * columns];

        int index = 0;

        for (int row = 0; row < rows; row++) {

            for (int column = 0; column < columns; column++) {

                frames[index++] = tmp[row][column];
            }
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

    public void dispose() {

        for (Texture texture : textures) {
            if (texture != null) {
                texture.dispose();
            }
        }
    }
}

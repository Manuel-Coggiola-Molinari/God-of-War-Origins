package com.goworigins.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverScreen implements Screen {

    private final Game game;

    private SpriteBatch batch;
    private BitmapFont titleFont;
    private BitmapFont subtitleFont;
    private GlyphLayout layout;

    private OrthographicCamera camera;
    private Viewport viewport;

    public GameOverScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);

        titleFont = new BitmapFont();
        titleFont.getData().setScale(3f);

        subtitleFont = new BitmapFont();
        subtitleFont.getData().setScale(1.3f);

        layout = new GlyphLayout();
    }

    @Override
    public void show() {
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MenuScreen(game));
            return;
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenUtils.clear(0.15f, 0.02f, 0.02f, 1f);
        viewport.apply(true);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        String title = "HAS MUERTO";
        layout.setText(titleFont, title);
        titleFont.draw(batch, title,
            (viewport.getWorldWidth() - layout.width) / 2f,
            viewport.getWorldHeight() / 2f + 40);

        String subtitle = "Presiona ENTER para volver al menu";
        layout.setText(subtitleFont, subtitle);
        subtitleFont.draw(batch, subtitle,
            (viewport.getWorldWidth() - layout.width) / 2f,
            viewport.getWorldHeight() / 2f - 20);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        batch.dispose();
        titleFont.dispose();
        subtitleFont.dispose();
    }
}

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

public class PauseScreen implements Screen {

    private final Game game;
    private final GameScreen gameScreen;

    private SpriteBatch batch;
    private BitmapFont titleFont;
    private BitmapFont optionFont;
    private GlyphLayout layout;

    private OrthographicCamera camera;
    private Viewport viewport;

    public PauseScreen(Game game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);

        titleFont = new BitmapFont();
        titleFont.getData().setScale(2.5f);

        optionFont = new BitmapFont();
        optionFont.getData().setScale(1.2f);

        layout = new GlyphLayout();
    }

    @Override
    public void show() {
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(gameScreen);
            return;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            gameScreen.dispose();
            game.setScreen(new MenuScreen(game));
            return;
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenUtils.clear(0.05f, 0.05f, 0.08f, 1f);
        viewport.apply(true);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        String title = "PAUSA";
        layout.setText(titleFont, title);
        titleFont.draw(batch, title,
            (viewport.getWorldWidth() - layout.width) / 2f,
            viewport.getWorldHeight() / 2f + 60);

        String resume = "ENTER / ESC para continuar";
        layout.setText(optionFont, resume);
        optionFont.draw(batch, resume,
            (viewport.getWorldWidth() - layout.width) / 2f,
            viewport.getWorldHeight() / 2f);

        String quit = "M para volver al menu";
        layout.setText(optionFont, quit);
        optionFont.draw(batch, quit,
            (viewport.getWorldWidth() - layout.width) / 2f,
            viewport.getWorldHeight() / 2f - 40);

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
        optionFont.dispose();
    }
}

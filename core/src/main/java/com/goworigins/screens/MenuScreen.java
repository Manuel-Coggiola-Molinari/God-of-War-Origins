package com.goworigins.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goworigins.audio.AudioManager;

public class MenuScreen implements Screen {

    private final Game game;
    private AudioManager audioManager;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private BitmapFont titleFont;
    private BitmapFont optionFont;
    private GlyphLayout layout;

    private Texture kratosTexture;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final String[] options = { "Jugar", "Salir" };
    private int selectedIndex = 0;

    // Colores del degradé de fondo
    private static final Color BG_BOTTOM = new Color(0.04f, 0.03f, 0.08f, 1f);
    private static final Color BG_TOP = new Color(0.12f, 0.10f, 0.20f, 1f);

    public MenuScreen(Game game) {
        this.game = game;

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);

        titleFont = new BitmapFont();
        titleFont.getData().setScale(3.2f);
        titleFont.getRegion().getTexture().setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        optionFont = new BitmapFont();
        optionFont.getData().setScale(1.7f);
        optionFont.getRegion().getTexture().setFilter(
            Texture.TextureFilter.Linear,
            Texture.TextureFilter.Linear
        );

        layout = new GlyphLayout();

        audioManager = new AudioManager("audio/music/menu.mp3");

        kratosTexture = new Texture("sprites/Characters/Kratos/idle.png");
        // Nearest para mantener el estilo pixel art nítido (sin desenfocar el sprite)
        kratosTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
    }

    @Override
    public void show() {
        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            selectedIndex = (selectedIndex + 1) % options.length;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W)) {
            selectedIndex = (selectedIndex - 1 + options.length) % options.length;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {

            if (selectedIndex == 0) {
                game.setScreen(new GameScreen(game));
            } else {
                Gdx.app.exit();
            }
            return;
        }

        // =====================================================
        // FONDO CON DEGRADÉ
        // =====================================================

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenUtils.clear(0.02f, 0.02f, 0.03f, 1f);
        viewport.apply(true);

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.rect(
            0, 0,
            viewport.getWorldWidth(), viewport.getWorldHeight(),
            BG_BOTTOM, BG_BOTTOM, BG_TOP, BG_TOP
        );

        // Panel semitransparente detrás de las opciones
        shapeRenderer.setColor(0, 0, 0, 0.45f);
        shapeRenderer.rect(50, 150, 320, 160);

        shapeRenderer.end();

        // =====================================================
        // SPRITE DE KRATOS
        // =====================================================

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        float kratosWidth = kratosTexture.getWidth() * 2.4f;
        float kratosHeight = kratosTexture.getHeight() * 2.4f;

        batch.draw(
            kratosTexture,
            viewport.getWorldWidth() - kratosWidth - 60,
            50,
            kratosWidth,
            kratosHeight
        );

        // =====================================================
        // TÍTULO (con sombra)
        // =====================================================

        String title = "GOD OF WAR: ORIGINS";
        layout.setText(titleFont, title);

        float titleX = (viewport.getWorldWidth() - layout.width) / 2f - 60;
        float titleY = viewport.getWorldHeight() / 2f + 180;

        titleFont.setColor(Color.BLACK);
        titleFont.draw(batch, title, titleX + 3, titleY - 3);

        titleFont.setColor(Color.WHITE);
        titleFont.draw(batch, title, titleX, titleY);

        // =====================================================
        // OPCIONES
        // =====================================================

        float optionY = 270;

        for (int i = 0; i < options.length; i++) {

            String label = (i == selectedIndex ? "> " : "   ") + options[i];

            layout.setText(optionFont, label);

            optionFont.setColor(i == selectedIndex ? Color.GOLD : Color.LIGHT_GRAY);

            optionFont.draw(batch, label, 80, optionY - (i * 55));
        }

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
        shapeRenderer.dispose();
        titleFont.dispose();
        optionFont.dispose();
        kratosTexture.dispose();
        audioManager.dispose();
    }
}

package com.goworigins.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goworigins.animation.AnimationState;
import com.goworigins.animation.PlayerAnimations;
import com.goworigins.building.Building;
import com.goworigins.building.BuildingSystem;
import com.goworigins.collision.CollisionManager;
import com.goworigins.combat.CombatSystem;
import com.goworigins.enemy.Enemy;
import com.goworigins.enemy.EnemyAI;
import com.goworigins.hud.GameHUD;
import com.goworigins.input.GameInput;
import com.goworigins.player.Player;
import com.goworigins.render.PlayerRenderer;
import com.badlogic.gdx.Input;

import java.util.List;

public class GameScreen implements Screen {

    private final Game game;

    private GameInput gameInput;
    private Player player;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Viewport viewport;

    private final float cameraSpeed = 300f;
    private final float cameraMargin = 150f;

    private PlayerRenderer playerRenderer;
    private SpriteBatch batch;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private CollisionManager collisionManager;

    private static final float MAP_WIDTH = 1600f;
    private static final float MAP_HEIGHT = 1600f;
    private static final float PLAYER_SIZE = 50f;

    private CombatSystem combatSystem;

    private Enemy enemy;
    private EnemyAI enemyAI;

    private float facingX = 0;
    private float facingY = -1;

    private GameHUD gameHUD;
    private OrthographicCamera hudCamera;

    private BuildingSystem buildingSystem;
    private PlayerAnimations playerAnimations;

    private float deathTimer = 0f;
    private static final float DEATH_SCREEN_DELAY = 1.5f;

    public GameScreen(Game game) {
        this.game = game;

        gameInput = new GameInput();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        gameHUD = new GameHUD(shapeRenderer);

        playerRenderer = new PlayerRenderer(shapeRenderer, batch);

        TmxMapLoader mapLoader = new TmxMapLoader();
        map = mapLoader.load("GoWOriginsMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        collisionManager = new CollisionManager(map);

        camera = new OrthographicCamera();
        viewport = new FitViewport(800, 600, camera);

        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, 800, 600);
        hudCamera.update();

        camera.position.set(viewport.getWorldWidth() / 2f, viewport.getWorldHeight() / 2f, 0);
        camera.update();

        player = new Player(400, 300, 200);
        combatSystem = new CombatSystem();
        buildingSystem = new BuildingSystem();

        enemy = new Enemy(500, 300, 100);
        enemyAI = new EnemyAI(collisionManager, buildingSystem);

        playerAnimations = new PlayerAnimations();
        playerRenderer.setAnimations(playerAnimations);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(gameInput);
    }

    @Override
    public void render(float delta) {

        // =====================================================
        // PAUSA
        // =====================================================

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseScreen(game, this));
            return;
        }

        // =====================================================
        // MOVIMIENTO
        // =====================================================

        float movementX = 0;
        float movementY = 0;

        if (player.isAlive()) {

            if (gameInput.isLeft()) {
                movementX -= player.getSpeed();
            }
            if (gameInput.isRight()) {
                movementX += player.getSpeed();
            }
            if (gameInput.isDown()) {
                movementY -= player.getSpeed();
            }
            if (gameInput.isUp()) {
                movementY += player.getSpeed();
            }
        }

        // =====================================================
        // ANIMACIÓN DE MOVIMIENTO
        // =====================================================

        if (player.isAlive()) {

            if (playerAnimations.getCurrentState() != AnimationState.ATTACK) {

                if (movementX != 0 || movementY != 0) {
                    playerAnimations.setState(AnimationState.WALK);
                } else {
                    playerAnimations.setState(AnimationState.IDLE);
                }
            }

        } else {
            playerAnimations.setState(AnimationState.DEATH);
        }

        // =====================================================
        // DIRECCIÓN
        // =====================================================

        float length = (float) Math.sqrt(movementX * movementX + movementY * movementY);

        if (length > 0) {
            movementX /= length;
            movementY /= length;

            facingX = movementX;
            facingY = movementY;

            playerRenderer.setFacing(facingX);
        }

        movementX *= player.getSpeed();
        movementY *= player.getSpeed();

        float deltaX = movementX * delta;
        float deltaY = movementY * delta;

        // =====================================================
        // MOVIMIENTO Y LÍMITES
        // =====================================================

        if (player.isAlive()) {

            float newX = player.getX() + deltaX;

            if (collisionManager.canMoveTo(newX, player.getY(), 50, 50)
                && buildingSystem.canMoveTo(newX, player.getY(), 50, 50)) {
                player.move(deltaX, 0);
            }

            float newY = player.getY() + deltaY;

            if (collisionManager.canMoveTo(player.getX(), newY, 50, 50)
                && buildingSystem.canMoveTo(player.getX(), newY, 50, 50)) {
                player.move(0, deltaY);
            }

            float clampedX = Math.max(0, Math.min(player.getX(), MAP_WIDTH - PLAYER_SIZE));
            float clampedY = Math.max(0, Math.min(player.getY(), MAP_HEIGHT - PLAYER_SIZE));

            player.setPosition(clampedX, clampedY);
        }

        // =====================================================
        // ACTUALIZAR JUGADOR Y ANIMACIONES
        // =====================================================

        player.update(delta);
        playerRenderer.update(delta);

        // =====================================================
        // CONSTRUCCIÓN
        // =====================================================

        if (player.isAlive() && gameInput.consumeBuild()) {
            buildingSystem.build(player, collisionManager);
        }

        // =====================================================
        // IA DEL ENEMIGO
        // =====================================================

        if (enemy.isAlive() && player.isAlive()) {
            enemyAI.update(enemy, player, delta);
        }

        // =====================================================
        // COMBATE
        // =====================================================

        combatSystem.update(delta);

        if (player.isAlive() && gameInput.consumeAttack()) {

            playerAnimations.setState(AnimationState.ATTACK);

            combatSystem.attack(player, enemy, facingX, facingY);

            System.out.println("Vida del enemigo: " + enemy.getHealth());
        }

        // =====================================================
        // CÁMARA
        // =====================================================

        float halfWidth = viewport.getWorldWidth() / 2f;
        float halfHeight = viewport.getWorldHeight() / 2f;

        float cameraLeft = camera.position.x - halfWidth;
        float cameraRight = camera.position.x + halfWidth;
        float cameraBottom = camera.position.y - halfHeight;
        float cameraTop = camera.position.y + halfHeight;

        float playerCenterX = player.getX() + 25;
        float playerCenterY = player.getY() + 25;

        if (playerCenterX < cameraLeft + cameraMargin) {
            camera.position.x -= cameraSpeed * delta;
        }
        if (playerCenterX > cameraRight - cameraMargin) {
            camera.position.x += cameraSpeed * delta;
        }
        if (playerCenterY < cameraBottom + cameraMargin) {
            camera.position.y -= cameraSpeed * delta;
        }
        if (playerCenterY > cameraTop - cameraMargin) {
            camera.position.y += cameraSpeed * delta;
        }

        camera.update();

        // =====================================================
        // LIMPIAR PANTALLA
        // =====================================================

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply(true);

        // =====================================================
        // MAPA
        // =====================================================

        mapRenderer.setView(camera);
        mapRenderer.render();

        // =====================================================
        // SHAPE RENDERER - MUNDO (grilla de referencia)
        // =====================================================

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);

        for (int x = -2000; x <= 2000; x += 100) {
            shapeRenderer.line(x, -2000, x, 2000);
        }
        for (int y = -2000; y <= 2000; y += 100) {
            shapeRenderer.line(-2000, y, 2000, y);
        }

        shapeRenderer.end();

        // =====================================================
        // ENEMIGO + ESTRUCTURAS
        // =====================================================

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (enemy.isAlive()) {
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.rect(enemy.getX(), enemy.getY(), enemy.getWidth(), enemy.getHeight());
            gameHUD.renderEnemyHealth(enemy);
        }

        List<Building> buildings = buildingSystem.getBuildings();

        for (Building building : buildings) {
            shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 1);
            shapeRenderer.rect(building.getX(), building.getY(), building.getWidth(), building.getHeight());
        }

        shapeRenderer.end();

        // =====================================================
        // JUGADOR - SPRITE
        // =====================================================

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        playerRenderer.render(player);
        batch.end();

        // =====================================================
        // HUD
        // =====================================================

        shapeRenderer.setProjectionMatrix(hudCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        gameHUD.render(player);
        shapeRenderer.end();

        // =====================================================
        // GAME OVER
        // =====================================================

        if (!player.isAlive()) {

            deathTimer += delta;

            if (deathTimer >= DEATH_SCREEN_DELAY) {
                dispose();
                game.setScreen(new GameOverScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudCamera.setToOrtho(false, 800, 600);
        hudCamera.update();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        map.dispose();
    }
}

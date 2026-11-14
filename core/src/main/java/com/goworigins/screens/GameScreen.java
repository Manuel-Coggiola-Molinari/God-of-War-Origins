package com.goworigins.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.goworigins.audio.AudioManager;
import com.goworigins.building.Building;
import com.goworigins.building.BuildingSystem;
import com.goworigins.enemy.*;
import com.goworigins.render.BuildingRenderer;
import com.goworigins.render.DraugrRenderer;
import com.goworigins.render.PlayerRenderer;
import com.goworigins.render.ResourceRenderer;
import com.goworigins.render.PesadillaRenderer;
import com.goworigins.collision.CollisionManager;
import com.goworigins.combat.CombatSystem;
import com.goworigins.hud.GameHUD;
import com.goworigins.input.GameInput;
import com.goworigins.player.Player;
import com.goworigins.resources.ResourceManager;
import com.goworigins.resources.ResourceNode;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

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
    private DraugrRenderer draugrRenderer;
    private PesadillaRenderer pesadillaRenderer;
    private ResourceRenderer resourceRenderer;
    private SpriteBatch batch;

    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;

    private CollisionManager collisionManager;

    private static final float MAP_WIDTH = 6144f;
    private static final float MAP_HEIGHT = 2816f;
    private static final float PLAYER_SIZE = 50f;

    private CombatSystem combatSystem;

    private final List<Enemy> enemies = new ArrayList<>();
    private final Map<Draugr, DraugrAI> draugrAIs = new HashMap<>();
    private final Map<Pesadilla, PesadillaAI> pesadillas = new HashMap<>();

    private float facingX = 0;
    private float facingY = -1;

    private GameHUD gameHUD;
    private OrthographicCamera hudCamera;

    private BuildingSystem buildingSystem;
    private ResourceManager resourceManager;
    private BuildingRenderer buildingRenderer;
    private PlayerAnimations playerAnimations;

    private AudioManager audioManager;

    private float deathTimer = 0f;
    private static final float DEATH_SCREEN_DELAY = 1.5f;


    public GameScreen(Game game) {

        this.game = game;

        // =====================================================
        // INPUT
        // =====================================================

        gameInput = new GameInput();

        // =====================================================
        // RENDERIZADO
        // =====================================================

        batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();

        gameHUD = new GameHUD(shapeRenderer);

        playerRenderer = new PlayerRenderer(
            shapeRenderer,
            batch
        );

        draugrRenderer = new DraugrRenderer(batch);
        pesadillaRenderer = new PesadillaRenderer(batch);

        // =====================================================
        // AUDIO
        // =====================================================

        audioManager = new AudioManager("audio/music/background.mp3");

        // =====================================================
        // MAPA
        // =====================================================

        TmxMapLoader mapLoader = new TmxMapLoader();

        map = mapLoader.load("GoWOriginsMap.tmx");

        mapRenderer = new OrthogonalTiledMapRenderer(map);

        collisionManager = new CollisionManager(map);

        // =====================================================
        // CÁMARA
        // =====================================================

        camera = new OrthographicCamera();

        viewport = new FitViewport(
            800,
            600,
            camera
        );

        hudCamera = new OrthographicCamera();

        hudCamera.setToOrtho(
            false,
            800,
            600
        );

        hudCamera.update();

        camera.position.set(
            viewport.getWorldWidth() / 2f,
            viewport.getWorldHeight() / 2f,
            0
        );

        camera.update();

        // =====================================================
        // JUGADOR
        // =====================================================

        player = new Player(
            400,
            300,
            200
        );

        playerAnimations = new PlayerAnimations();

        playerRenderer.setAnimations(
            playerAnimations
        );

        // =====================================================
        // SISTEMAS
        // =====================================================

        combatSystem = new CombatSystem();

        buildingSystem = new BuildingSystem();
        resourceRenderer = new ResourceRenderer();
        resourceManager = new ResourceManager();

        resourceManager.agregarNodo(new ResourceNode(ResourceNode.ResourceType.ARBOL, 500, 300, 64, 64, 10, 2));
        resourceManager.agregarNodo(new ResourceNode(ResourceNode.ResourceType.ROCA, 800, 300, 64, 64, 8, 2));
        resourceManager.agregarNodo(new ResourceNode(ResourceNode.ResourceType.MINERAL, 1100, 300, 64, 64, 6, 1));

        buildingRenderer = new BuildingRenderer(batch);
        // =====================================================
        // ENEMIGO
        // =====================================================

        spawnEnemies();
    }
    private void spawnEnemies() {

        int enemiesToSpawn = 10;

        int[] radii = {220, 250, 280, 290};

        for (int radius : radii) {

            if (enemies.size() >= enemiesToSpawn) {
                break;
            }

            for (int i = 0; i < 24 && enemies.size() < enemiesToSpawn; i++) {

                double angle = (2 * Math.PI * i) / 24;

                float spawnX = player.getX() + 25
                    + (float) Math.cos(angle) * radius - 25;

                float spawnY = player.getY() + 25
                    + (float) Math.sin(angle) * radius - 25;

                // Comprobar límites del mapa.
                if (
                    spawnX < 0 ||
                        spawnY < 0 ||
                        spawnX + 50 > MAP_WIDTH ||
                        spawnY + 50 > MAP_HEIGHT
                ) {
                    continue;
                }

                // Comprobar colisiones con el mapa y construcciones.
                if (
                    !collisionManager.canMoveTo(spawnX, spawnY, 50, 50) ||
                        !buildingSystem.canMoveTo(spawnX, spawnY, 50, 50)
                ) {
                    continue;
                }

                // Evitar que dos enemigos aparezcan superpuestos.
                boolean overlaps = false;

                for (Enemy existing : enemies) {

                    float dx = existing.getCenterX() - (spawnX + 25);
                    float dy = existing.getCenterY() - (spawnY + 25);

                    if (Math.sqrt(dx * dx + dy * dy) < 55) {
                        overlaps = true;
                        break;
                    }
                }

                if (overlaps) {
                    continue;
                }

                Draugr newEnemy = new Draugr(
                    spawnX,
                    spawnY,
                    100,
                    EnemyType.COMMON
                );

                enemies.add(newEnemy);

                draugrAIs.put(newEnemy, new DraugrAI(collisionManager, buildingSystem));
            }
        }

        // Generar una Pesadilla cerca del jugador.
        float pesadillaX = player.getX() + 300;
        float pesadillaY = player.getY() + 150;

        Pesadilla pesadilla = new Pesadilla(
            pesadillaX,
            pesadillaY,
            60
        );

        enemies.add(pesadilla);
        pesadillas.put(pesadilla, new PesadillaAI());

        System.out.println("Enemigos generados: " + enemies.size());
    }
    @Override
    public void show() {

        Gdx.input.setInputProcessor(
            gameInput
        );
    }

    @Override
    public void render(float delta) {

        // =====================================================
        // PAUSA
        // =====================================================

        if (
            Gdx.input.isKeyJustPressed(
                Input.Keys.ESCAPE
            )
        ) {

            game.setScreen(
                new PauseScreen(
                    game,
                    this
                )
            );

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

            if (
                playerAnimations.getCurrentState()
                    != AnimationState.ATTACK
                    &&
                    playerAnimations.getCurrentState()
                        != AnimationState.DAMAGE
            ) {

                if (
                    movementX != 0
                        ||
                        movementY != 0
                ) {

                    playerAnimations.setState(
                        AnimationState.WALK
                    );

                } else {

                    playerAnimations.setState(
                        AnimationState.IDLE
                    );
                }
            }

        } else {

            playerAnimations.setState(
                AnimationState.DEATH
            );
        }

        // =====================================================
        // DIRECCIÓN
        // =====================================================

        float length = (float) Math.sqrt(
            movementX * movementX
                +
                movementY * movementY
        );

        if (length > 0) {

            movementX /= length;
            movementY /= length;

            facingX = movementX;
            facingY = movementY;

            playerRenderer.setFacing(
                facingX
            );
        }

        movementX *= player.getSpeed();
        movementY *= player.getSpeed();

        float deltaX =
            movementX * delta;

        float deltaY =
            movementY * delta;

        // =====================================================
        // MOVIMIENTO HORIZONTAL Y VERTICAL
        // =====================================================

        if (player.isAlive()) {

            float newX =
                player.getX() + deltaX;

            if (
                collisionManager.canMoveTo(
                    newX,
                    player.getY(),
                    50,
                    50
                )
                    &&
                    buildingSystem.canMoveTo(
                        newX,
                        player.getY(),
                        50,
                        50
                    )
            ) {

                player.move(
                    deltaX,
                    0
                );
            }

            float newY =
                player.getY() + deltaY;

            if (
                collisionManager.canMoveTo(
                    player.getX(),
                    newY,
                    50,
                    50
                )
                    &&
                    buildingSystem.canMoveTo(
                        player.getX(),
                        newY,
                        50,
                        50
                    )
            ) {

                player.move(
                    0,
                    deltaY
                );
            }

            // =================================================
            // LÍMITES DEL MAPA
            // =================================================

            float clampedX =
                Math.max(
                    0,
                    Math.min(
                        player.getX(),
                        MAP_WIDTH - PLAYER_SIZE
                    )
                );

            float clampedY =
                Math.max(
                    0,
                    Math.min(
                        player.getY(),
                        MAP_HEIGHT - PLAYER_SIZE
                    )
                );

            player.setPosition(
                clampedX,
                clampedY
            );
        }

        // =====================================================
        // ACTUALIZAR JUGADOR Y ANIMACIONES
        // =====================================================

        player.update(delta);

        playerRenderer.update(delta);

        // =====================================================
        // CONSTRUCCIÓN
        // =====================================================

        if (
            player.isAlive()
                &&
                gameInput.consumeBuild()
        ) {

            boolean built = buildingSystem.build(player, collisionManager);

            if (built) {
                audioManager.playBuildSound();
            }
        }
        // =====================================================
        // SISTEMA DE COMBATE E IRA ESPARTANA
        // =====================================================

        combatSystem.update(delta);

        if (player.isAlive() && gameInput.consumeRage()) {
            combatSystem.activateRage();
        }

        player.setInvulnerable(combatSystem.isRageActive());

        // =====================================================
        // IA DE LOS ENEMIGOS
        // =====================================================

        for (Enemy currentEnemy : enemies) {

            if (currentEnemy instanceof Pesadilla) {

                Pesadilla pesadilla = (Pesadilla) currentEnemy;
                PesadillaAI ai = pesadillas.get(pesadilla);

                if (ai != null) {
                    ai.update(pesadilla, player, delta);
                }

            } else if (currentEnemy instanceof Draugr) {

                Draugr draugr = (Draugr) currentEnemy;
                DraugrAI ai = draugrAIs.get(draugr);

                if (player.isAlive() && ai != null) {
                    ai.update(draugr, player, delta);
                }

                draugr.updateAnimations(delta);
            }
        }
        // =====================================================
        // DAÑO DEL JUGADOR
        // =====================================================

        if (player.consumeDamage()) {

            playerAnimations.setState(
                AnimationState.DAMAGE
            );

            audioManager.playPlayerHitSound();
            audioManager.playEnemyAttackSound();
        }

        // =====================================================
        // SISTEMA DE COMBATE
        // =====================================================


        // =====================================================
        // AUDIO
        // =====================================================

        if (gameInput.consumeMute()) {

            audioManager.toggleMute();
        }

        if (gameInput.consumeVolumeUp()) {

            audioManager.increaseVolume();
        }

        if (gameInput.consumeVolumeDown()) {

            audioManager.decreaseVolume();
        }

        // =====================================================
        // ATAQUE
        // =====================================================
        if (
            player.isAlive()
                &&
                gameInput.consumeAttack()
        ) {

            playerAnimations.setState(
                AnimationState.ATTACK
            );

            audioManager.playAttackSound();

            Enemy hitEnemy = combatSystem.attack(
                player,
                enemies,
                player.getDirectionX(),
                player.getDirectionY()
            );

            ResourceNode collected = resourceManager.recolectarCercano(
                player,
                facingX,
                facingY
            );

            if (collected != null) {

                System.out.println(
                    "Recurso recolectado: " + collected.getType()
                );

                System.out.println(
                    "Cantidad restante: " + collected.getResources()
                );

                if (collected.agotado()) {
                    System.out.println("Recurso agotado");
                }
            }

            if (hitEnemy != null) {

                audioManager.playEnemyHitSound();

                if (!hitEnemy.isAlive()) {
                    audioManager.playEnemyDeathSound();
                }
            }
        }

        // =====================================================
        // CÁMARA
        // =====================================================

        float halfWidth =
            viewport.getWorldWidth() / 2f;

        float halfHeight =
            viewport.getWorldHeight() / 2f;

        float cameraLeft =
            camera.position.x - halfWidth;

        float cameraRight =
            camera.position.x + halfWidth;

        float cameraBottom =
            camera.position.y - halfHeight;

        float cameraTop =
            camera.position.y + halfHeight;

        float playerCenterX =
            player.getX() + 25;

        float playerCenterY =
            player.getY() + 25;

        if (
            playerCenterX
                <
                cameraLeft + cameraMargin
        ) {

            camera.position.x -=
                cameraSpeed * delta;
        }

        if (
            playerCenterX
                >
                cameraRight - cameraMargin
        ) {

            camera.position.x +=
                cameraSpeed * delta;
        }

        if (
            playerCenterY
                <
                cameraBottom + cameraMargin
        ) {

            camera.position.y -=
                cameraSpeed * delta;
        }

        if (
            playerCenterY
                >
                cameraTop - cameraMargin
        ) {

            camera.position.y +=
                cameraSpeed * delta;
        }
        camera.position.x = Math.max(
            halfWidth,
            Math.min(camera.position.x, MAP_WIDTH - halfWidth)
        );

        camera.position.y = Math.max(
            halfHeight,
            Math.min(camera.position.y, MAP_HEIGHT - halfHeight)
        );

        camera.update();

        // =====================================================
        // LIMPIAR PANTALLA
        // =====================================================
        viewport.apply(false);
        Gdx.gl.glViewport(
            0,
            0,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );

        ScreenUtils.clear(
            0.15f,
            0.15f,
            0.2f,
            1f
        );

        // =====================================================
        // MAPA
        // =====================================================

        mapRenderer.setView(camera);

        mapRenderer.render();

        // =====================================================
        // ENEMIGO + ESTRUCTURAS + RECURSOS
        // =====================================================

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        resourceRenderer.render(resourceManager, camera);

        batch.begin();
        // Dibujar las barras de vida de los enemigos.
        for (Enemy currentEnemy : enemies) {

            if (currentEnemy instanceof Pesadilla) {
                pesadillaRenderer.render((Pesadilla) currentEnemy);

            } else if (currentEnemy instanceof Draugr) {
                draugrRenderer.render((Draugr) currentEnemy);
            }
        }
        batch.end();

        batch.begin();

        for (Map.Entry<Pesadilla, PesadillaAI> entry : pesadillas.entrySet()) {

            Pesadilla pesadilla = entry.getKey();
            PesadillaAI pesadillaAI = entry.getValue();

            for (PesadillaProjectile projectile : pesadillaAI.getProjectiles()) {
                pesadillaRenderer.renderProjectile(
                    projectile,
                    pesadilla.getAnimations()
                );
            }
        }

        batch.end();

        List<Building> buildings = buildingSystem.getBuildings();

        shapeRenderer.end();

        // =====================================================
        // JUGADOR + ENEMIGO - SPRITES
        // =====================================================

        batch.setProjectionMatrix(
            camera.combined
        );

        batch.begin();

        for (Building building : buildings) {
            buildingRenderer.render(building);
        }

        playerRenderer.render(
            player
        );

        batch.end();

        // =====================================================
        // HUD
        // =====================================================

        shapeRenderer.setProjectionMatrix(
            hudCamera.combined
        );

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        gameHUD.render(player, combatSystem, delta);

        shapeRenderer.end();

        // =====================================================
        // GAME OVER
        // =====================================================

        if (!player.isAlive()) {

            deathTimer += delta;

            if (
                deathTimer >=
                    DEATH_SCREEN_DELAY
            ) {

                dispose();

                game.setScreen(
                    new GameOverScreen(game)
                );
            }
        }
    }

    @Override
    public void resize(
        int width,
        int height
    ) {

        viewport.update(
            width,
            height,
            true
        );

        hudCamera.setToOrtho(
            false,
            800,
            600
        );

        hudCamera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

        batch.dispose();

        shapeRenderer.dispose();

        map.dispose();
        buildingRenderer.dispose();
        audioManager.dispose();
        resourceRenderer.dispose();
        for (Enemy enemy : enemies) {
            if (enemy instanceof Pesadilla) {
                ((Pesadilla) enemy).dispose();
            }
        }
        pesadillas.clear();
        draugrAIs.clear();
    }
}

package com.goworigins;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.Gdx;
import com.goworigins.animation.EnemyAnimationState;
import com.goworigins.input.GameInput;
import com.goworigins.player.Player;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goworigins.render.EnemyRenderer;
import com.goworigins.render.PlayerRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.goworigins.collision.CollisionManager;
import com.goworigins.combat.CombatSystem;
import com.goworigins.enemy.Enemy;
import com.goworigins.enemy.EnemyAI;
import com.goworigins.hud.GameHUD;
import com.goworigins.building.Building;
import com.goworigins.building.BuildingSystem;
import com.goworigins.animation.AnimationState;
import com.goworigins.animation.PlayerAnimations;
import com.goworigins.audio.AudioManager;

import java.util.List;

public class GodOfWarOrigins extends ApplicationAdapter {

    private GameInput gameInput;
    private Player player;

    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Viewport viewport;

    private float cameraSpeed = 300f;
    private float cameraMargin = 150f;

    private PlayerRenderer playerRenderer;
    private SpriteBatch batch;
    private EnemyRenderer enemyRenderer;

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

    private AudioManager audioManager;

    @Override
    public void create() {

        gameInput = new GameInput();
        Gdx.input.setInputProcessor(gameInput);

        batch = new SpriteBatch();

        shapeRenderer = new ShapeRenderer();

        gameHUD = new GameHUD(shapeRenderer);

        audioManager = new AudioManager();

        playerRenderer = new PlayerRenderer(shapeRenderer, batch);

        enemyRenderer = new EnemyRenderer(batch);

        TmxMapLoader mapLoader = new TmxMapLoader();

        map = mapLoader.load("GoWOriginsMap.tmx");

        mapRenderer = new OrthogonalTiledMapRenderer(map);

        collisionManager = new CollisionManager(map);

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

        player = new Player(
            400,
            300,
            200
        );

        combatSystem = new CombatSystem();

        buildingSystem = new BuildingSystem();

        enemy = new Enemy(
            500,
            300,
            100
        );

        enemyAI = new EnemyAI(
            collisionManager,
            buildingSystem
        );

        playerAnimations = new PlayerAnimations();

        playerRenderer.setAnimations(
            playerAnimations
        );
    }

    @Override
    public void render() {

        float delta =
            Gdx.graphics.getDeltaTime();

        // =====================================================
        // MOVIMIENTO
        // =====================================================

        float movementX = 0;
        float movementY = 0;

        // Solo permitimos movimiento mientras está vivo
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
                playerAnimations.getCurrentState() != AnimationState.ATTACK &&
                    playerAnimations.getCurrentState() != AnimationState.DAMAGE
            ) {

                if (movementX != 0 || movementY != 0) {

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
            movementX * movementX +
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
        // MOVIMIENTO HORIZONTAL
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

            // =================================================
            // MOVIMIENTO VERTICAL
            // =================================================

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

            float clampedX = Math.max(
                0,
                Math.min(
                    player.getX(),
                    MAP_WIDTH - PLAYER_SIZE
                )
            );

            float clampedY = Math.max(
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
            player.isAlive() &&
                gameInput.consumeBuild()
        ) {

            buildingSystem.build(
                player,
                collisionManager
            );
        }

        // =====================================================
        // IA DEL ENEMIGO
        // =====================================================

        if (enemy.isAlive()) {

            enemyAI.update(
                enemy,
                player,
                delta
            );
        }

        enemy.updateAnimations(delta);

        if (player.consumeDamage()) {

            if (player.isAlive()) {

                playerAnimations.setState(
                    AnimationState.DAMAGE
                );
            }
        }

        combatSystem.update(delta);

        // =====================================================
        // COMBATE
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

        if (
            player.isAlive() &&
                gameInput.consumeAttack()
        ) {

            playerAnimations.setState(
                AnimationState.ATTACK
            );

            audioManager.playAttackSound();

            combatSystem.attack(
                player,
                enemy,
                facingX,
                facingY
            );

            System.out.println(
                "Vida del enemigo: "
                    + enemy.getHealth()
            );

            if (!enemy.isAlive()) {

                enemy.getAnimations().setState(
                    EnemyAnimationState.DEATH
                );
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
            playerCenterX <
                cameraLeft + cameraMargin
        ) {

            camera.position.x -=
                cameraSpeed * delta;
        }

        if (
            playerCenterX >
                cameraRight - cameraMargin
        ) {

            camera.position.x +=
                cameraSpeed * delta;
        }

        if (
            playerCenterY <
                cameraBottom + cameraMargin
        ) {

            camera.position.y -=
                cameraSpeed * delta;
        }

        if (
            playerCenterY >
                cameraTop - cameraMargin
        ) {

            camera.position.y +=
                cameraSpeed * delta;
        }

        camera.update();

        // =====================================================
        // LIMPIAR PANTALLA
        // =====================================================

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
        // SHAPE RENDERER - MUNDO
        // =====================================================

        shapeRenderer.setProjectionMatrix(
            camera.combined
        );

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Line
        );

        shapeRenderer.setColor(
            1,
            1,
            1,
            1
        );

        for (
            int x = -2000;
            x <= 2000;
            x += 100
        ) {

            shapeRenderer.line(
                x,
                -2000,
                x,
                2000
            );
        }

        for (
            int y = -2000;
            y <= 2000;
            y += 100
        ) {

            shapeRenderer.line(
                -2000,
                y,
                2000,
                y
            );
        }

        shapeRenderer.end();

        // =====================================================
        // ENEMIGO + ESTRUCTURAS
        // =====================================================

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        if (enemy.isAlive()) {

            gameHUD.renderEnemyHealth(
                enemy
            );
            shapeRenderer.end();
        }

        List<Building> buildings =
            buildingSystem.getBuildings();

        for (Building building : buildings) {

            shapeRenderer.setColor(
                0.5f,
                0.5f,
                0.5f,
                1
            );

            shapeRenderer.rect(
                building.getX(),
                building.getY(),
                building.getWidth(),
                building.getHeight()
            );
        }

        shapeRenderer.end();

        // =====================================================
        // JUGADOR - SPRITE
        // =====================================================

        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        enemyRenderer.render(enemy);

        playerRenderer.render(player);

        batch.end();

        // =====================================================
        // HUD
        // =====================================================

        shapeRenderer.setProjectionMatrix(hudCamera.combined);

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        gameHUD.render(
            player
        );

        shapeRenderer.end();
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
    public void dispose() {

        batch.dispose();

        shapeRenderer.dispose();

        map.dispose();

        audioManager.dispose();
    }
}

package com.goworigins.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.goworigins.player.Player;
import com.goworigins.enemy.Enemy;
import com.goworigins.combat.CombatSystem;

public class GameHUD {

    private final ShapeRenderer shapeRenderer;

    private float blinkTimer = 0f;
    private boolean blink = false;

    private final Color rageColor = Color.valueOf("4d1510");
    private final Color rageReadyColor = Color.valueOf("e61c09");

    public GameHUD(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public void render(Player player, CombatSystem combatSystem, float delta) {

        int maxHealth = 100;
        int currentHealth = player.getHealth();

        float x = 20;
        float y = 550;

        float width = 250;
        float height = 25;

        // Barra de vida.
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(x, y, width, height);

        float healthWidth =
            width * ((float) currentHealth / maxHealth);

        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.rect(x, y, healthWidth, height);

        // Barra de Ira Espartana.
        renderRageBar(combatSystem, delta);
    }

    private void renderRageBar(CombatSystem combatSystem, float delta) {

        float x = 20;
        float y = 530;

        float width = 250;
        float height = 12;

        // Fondo de la barra.
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);

        float chargeWidth =
            width * ((float) combatSystem.getRageCharge()
                / combatSystem.getKillsRequired());

        // Parpadeo cuando está completa y lista para activar.
        if (combatSystem.isRageReady()) {

            blinkTimer += delta;

            if (blinkTimer >= 0.3f) {
                blink = !blink;
                blinkTimer = 0f;
            }

            shapeRenderer.setColor(
                blink ? rageReadyColor : rageColor
            );

        } else {

            blinkTimer = 0f;
            blink = false;

            shapeRenderer.setColor(rageColor);
        }

        shapeRenderer.rect(x, y, chargeWidth, height);
    }

    public void renderEnemyHealth(Enemy enemy) {

        if (!enemy.isAlive()) {
            return;
        }

        float x = enemy.getX();
        float y = enemy.getY() + enemy.getHeight() + 35;

        float width = enemy.getWidth();
        float height = 6;

        int maxHealth = 100;
        int currentHealth = enemy.getHealth();

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(x, y, width, height);

        float healthWidth =
            width * ((float) currentHealth / maxHealth);

        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(x, y, healthWidth, height);
    }
}

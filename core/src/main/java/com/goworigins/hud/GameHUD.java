package com.goworigins.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.goworigins.player.Player;
import com.goworigins.enemy.Enemy;

public class GameHUD {

    private final ShapeRenderer shapeRenderer;

    public GameHUD(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public void render(Player player) {

        int maxHealth = 100;
        int currentHealth = player.getHealth();

        float x = 20;
        float y = 550;

        float width = 250;
        float height = 25;

        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(
            x,
            y,
            width,
            height
        );

        float healthWidth =
            width * ((float) currentHealth / maxHealth);

        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.rect(
            x,
            y,
            healthWidth,
            height
        );
    }

    public void renderEnemyHealth(Enemy enemy) {

        if (!enemy.isAlive()) {
            return;
        }

        float x = enemy.getX();
        float y = enemy.getY() + enemy.getHeight() + 10;

        float width = enemy.getWidth();
        float height = 6;

        int maxHealth = 100;
        int currentHealth = enemy.getHealth();

        // Fondo de la barra
        shapeRenderer.setColor(Color.RED);

        shapeRenderer.rect(
            x,
            y,
            width,
            height
        );

        // Vida actual
        float healthWidth =
            width * ((float) currentHealth / maxHealth);

        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.rect(
            x,
            y,
            healthWidth,
            height
        );
    }
}

package com.goworigins.combat;

import com.badlogic.gdx.math.Rectangle;
import com.goworigins.enemy.Enemy;
import com.goworigins.player.Player;

public class CombatSystem {

    private final int kratosDamage = 25;
    private final float attackRange = 70;

    private boolean attacking = false;
    private float attackTimer = 0f;
    private float attackDuration = 0.25f;
    private float attackCooldown = 0.4f;
    private float cooldownTimer = 0f;

    public void attack(
        Player player,
        Enemy enemy,
        float directionX,
        float directionY
    ) {

        if (cooldownTimer > 0 || attacking) {
            return;
        }

        attacking = true;
        attackTimer = attackDuration;
        cooldownTimer = attackCooldown;

        float attackX =
            player.getX() + 25 + directionX * attackRange;

        float attackY =
            player.getY() + 25 + directionY * attackRange;

        Rectangle attackArea = new Rectangle(
            attackX - 25,
            attackY - 25,
            50,
            50
        );

        Rectangle enemyArea = new Rectangle(
            enemy.getX(),
            enemy.getY(),
            enemy.getWidth(),
            enemy.getHeight()
        );

        if (attackArea.overlaps(enemyArea)) {
            enemy.takeDamage(kratosDamage);
        }
    }
    public void update(float delta) {

        if (cooldownTimer > 0) {
            cooldownTimer -= delta;
        }

        if (attacking) {

            attackTimer -= delta;

            if (attackTimer <= 0) {
                attacking = false;
            }
        }
    }
    public boolean isAttacking() {
        return attacking;
    }
}

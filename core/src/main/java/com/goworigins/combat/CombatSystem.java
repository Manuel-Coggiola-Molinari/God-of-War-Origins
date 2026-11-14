package com.goworigins.combat;

import com.badlogic.gdx.math.Rectangle;
import com.goworigins.enemy.Enemy;
import com.goworigins.enemy.EnemyType;
import com.goworigins.player.Player;

import java.util.List;

public class CombatSystem {

    private final int kratosDamage = 25;
    private final float attackRange = 70;

    private final int killsRequired = 10;
    private final float rageDuration = 8f;
    private final int rageDamageMultiplier = 3;

    private int rageCharge = 0;
    private boolean rageActive = false;
    private float rageTimer = 0f;

    private boolean attacking = false;
    private float attackTimer = 0f;
    private float attackDuration = 0.5f;
    private float attackCooldown = 0.5f;
    private float cooldownTimer = 0f;

    public Enemy attack(
        Player player,
        List<Enemy> enemies,
        float directionX,
        float directionY
    ) {

        if (cooldownTimer > 0 || attacking) {
            return null;
        }

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

        for (Enemy enemy : enemies) {

            if (enemy == null || !enemy.isAlive()) {
                continue;
            }

            Rectangle enemyArea = new Rectangle(
                enemy.getX(),
                enemy.getY(),
                enemy.getWidth(),
                enemy.getHeight()
            );

            if (attackArea.overlaps(enemyArea)) {

                attacking = true;
                attackTimer = attackDuration;
                cooldownTimer = attackCooldown;

                boolean wasAlive = enemy.isAlive();

                int damage = kratosDamage;

                if (rageActive) {

                    if (enemy.getType() == EnemyType.COMMON) {
                        damage = enemy.getHealth();
                    } else {
                        damage = kratosDamage * rageDamageMultiplier;
                    }
                }

                enemy.takeDamage(damage);

                if (wasAlive && !enemy.isAlive() && !rageActive) {

                    rageCharge++;

                    if (rageCharge > killsRequired) {
                        rageCharge = killsRequired;
                    }
                }

                return enemy;
            }
        }

        // Aunque no golpee, el ataque consume su tiempo de reutilización.
        attacking = true;
        attackTimer = attackDuration;
        cooldownTimer = attackCooldown;

        return null;
    }

    public boolean activateRage() {

        if (rageActive || rageCharge < killsRequired) {
            return false;
        }

        rageActive = true;
        rageTimer = rageDuration;
        rageCharge = 0;

        return true;
    }

    public void update(float delta) {

        if (cooldownTimer > 0) {
            cooldownTimer -= delta;

            if (cooldownTimer < 0) {
                cooldownTimer = 0;
            }
        }

        if (attacking) {
            attackTimer -= delta;

            if (attackTimer <= 0) {
                attacking = false;
            }
        }

        if (rageActive) {
            rageTimer -= delta;

            if (rageTimer <= 0) {
                rageTimer = 0;
                rageActive = false;
            }
        }
    }

    public boolean isRageActive() {
        return rageActive;
    }

    public boolean isRageReady() {
        return rageCharge >= killsRequired && !rageActive;
    }

    public int getRageCharge() {
        return rageCharge;
    }

    public int getKillsRequired() {
        return killsRequired;
    }

    public float getRageTimeRemaining() {
        return rageTimer;
    }

    public boolean isAttacking() {
        return attacking;
    }
}

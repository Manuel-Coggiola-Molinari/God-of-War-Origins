package com.goworigins.enemy;

import com.goworigins.animation.PesadillaAnimations;
import com.goworigins.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PesadillaAI {

    private final float detectionRange = 350;
    private final float attackRange = 220;
    private final float speed = 65;
    private final float attackCooldown = 2;

    private float attackTimer = 0;
    private int previousHealth = -1;

    private final List<PesadillaProjectile> projectiles =
        new ArrayList<>();

    public void update(Pesadilla enemy, Player player, float delta) {

        PesadillaAnimations animations = enemy.getAnimations();
        animations.update(delta);

        // Detectar si la Pesadilla recibió daño.
        if (previousHealth == -1) {
            previousHealth = enemy.getHealth();

        } else if (enemy.getHealth() < previousHealth) {

            previousHealth = enemy.getHealth();

            if (enemy.isAlive()) {
                animations.setState(PesadillaAnimations.State.DAMAGE);
            } else {
                animations.setState(PesadillaAnimations.State.DEATH);
            }
        }

        // Si murió, permitir que los proyectiles existentes terminen.
        if (!enemy.isAlive()) {
            updateProjectiles(player, delta);
            return;
        }

        // Si está recibiendo daño, esperar a que termine.
        if (animations.getCurrentState() ==
            PesadillaAnimations.State.DAMAGE) {

            if (animations.isDamageFinished()) {
                animations.setState(PesadillaAnimations.State.IDLE);
            }

            updateProjectiles(player, delta);
            return;
        }

        // Si está atacando, esperar a que termine la animación.
        if (animations.getCurrentState() ==
            PesadillaAnimations.State.ATTACK) {

            if (animations.isAttackFinished()) {

                projectiles.add(new PesadillaProjectile(
                    enemy.getCenterX(),
                    enemy.getCenterY(),
                    player.getX() + 25,
                    player.getY() + 25
                ));

                attackTimer = attackCooldown;
                animations.setState(PesadillaAnimations.State.IDLE);
            }

            updateProjectiles(player, delta);
            return;
        }

        if (attackTimer > 0) {
            attackTimer -= delta;
        }

        if (player.isAlive()) {

            float dx = player.getX() - enemy.getX();
            float dy = player.getY() - enemy.getY();

            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            if (distance <= detectionRange && distance > 0) {

                enemy.setFacingLeft(dx > 0);

                if (distance > attackRange) {

                    float moveX = (dx / distance) * speed * delta;
                    float moveY = (dy / distance) * speed * delta;

                    enemy.move(moveX, moveY);

                } else if (attackTimer <= 0) {

                    animations.setState(PesadillaAnimations.State.ATTACK);
                }
            }
        }

        updateProjectiles(player, delta);
    }

    private void updateProjectiles(Player player, float delta) {

        Iterator<PesadillaProjectile> iterator =
            projectiles.iterator();

        while (iterator.hasNext()) {

            PesadillaProjectile projectile = iterator.next();
            projectile.update(delta);

            if (player.isAlive() &&
                projectile.hitsPlayer(player.getX(), player.getY())) {

                player.takeDamage(projectile.getDamage());
                projectile.startImpact();
            }

            if (!projectile.isActive()) {
                iterator.remove();
            }
        }
    }

    public List<PesadillaProjectile> getProjectiles() {
        return projectiles;
    }
}

package com.goworigins.enemy;

import com.goworigins.animation.EnemyAnimationState;
import com.goworigins.player.Player;
import com.goworigins.collision.CollisionManager;
import com.goworigins.building.BuildingSystem;
import com.goworigins.render.EnemyRenderer;

public class EnemyAI {

    private EnemyState currentState = EnemyState.IDLE;
    private final float detectionRange = 300f;
    private final float movementSpeed = 80f;
    private final float attackRange = 60f;
    private final int attackDamage = 10;
    private float attackCooldown = 1f;
    private float attackTimer = 0f;

    private final CollisionManager collisionManager;
    private final BuildingSystem buildingSystem;
    private final EnemyRenderer enemyRenderer;

    public EnemyAI( CollisionManager collisionManager, BuildingSystem buildingSystem, EnemyRenderer enemyRenderer ) {
        this.collisionManager = collisionManager;
        this.buildingSystem = buildingSystem;
        this.enemyRenderer = enemyRenderer;
    }
    public void update(Enemy enemy, Player player, float delta) {

        if (!player.isAlive()) {
            currentState = EnemyState.IDLE;
            return;
        }

        float dx = player.getX() - enemy.getX();
        float dy = player.getY() - enemy.getY();

        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > detectionRange) {

            enemy.getAnimations().setState(
                EnemyAnimationState.IDLE
            );

            return;
        }
        attackTimer -= delta;

        if (distance <= attackRange) {

            if (enemy.getAnimations().getCurrentState() != EnemyAnimationState.DAMAGE) {

                enemy.getAnimations().setState(EnemyAnimationState.ATTACK);
            }

            if (attackTimer <= 0) {

                player.takeDamage(attackDamage);

                System.out.println(
                    "Vida del jugador: "
                        + player.getHealth()
                );

                attackTimer = attackCooldown;
            }

            return;
        }

        currentState = EnemyState.CHASE;

        if (distance > attackRange) {
            if (enemy.getAnimations().getCurrentState() != EnemyAnimationState.DAMAGE) {

                enemy.getAnimations().setState(EnemyAnimationState.WALK);
            }}

        float directionX = dx / distance;
        float directionY = dy / distance;

        enemyRenderer.setFacing(directionX);

        float deltaX = directionX * movementSpeed * delta;
        float deltaY = directionY * movementSpeed * delta;

        enemyRenderer.setFacing(directionX);

        float newX = enemy.getX() + deltaX;

        if (collisionManager.canMoveTo( newX, enemy.getY(), enemy.getWidth(), enemy.getHeight() )
            && buildingSystem.canMoveTo( newX, enemy.getY(), enemy.getWidth(), enemy.getHeight() ))
        {   enemy.move(deltaX, 0);   }

        float newY = enemy.getY() + deltaY;

        if (collisionManager.canMoveTo( enemy.getX(), newY, enemy.getWidth(), enemy.getHeight() )
            && buildingSystem.canMoveTo( enemy.getX(), newY, enemy.getWidth(), enemy.getHeight() ))
        {   enemy.move(0, deltaY);   }
    }
    public EnemyState getCurrentState() {
        return currentState;
    }
}

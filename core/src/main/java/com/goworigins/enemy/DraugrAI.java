package com.goworigins.enemy;

import com.goworigins.animation.EnemyAnimationState;
import com.goworigins.player.Player;
import com.goworigins.collision.CollisionManager;
import com.goworigins.building.BuildingSystem;

public class DraugrAI {

    private EnemyState currentState = EnemyState.IDLE;

    private final float detectionRange = 300f;
    private final float movementSpeed = 80f;
    private final float attackRange = 60f;
    private final int attackDamage = 10;

    private final float attackCooldown = 1f;
    private float attackTimer = 0f;

    private final CollisionManager collisionManager;
    private final BuildingSystem buildingSystem;

    public DraugrAI(
        CollisionManager collisionManager,
        BuildingSystem buildingSystem
    ) {
        this.collisionManager = collisionManager;
        this.buildingSystem = buildingSystem;
    }

    public void update(Draugr enemy, Player player, float delta) {

        if (!enemy.isAlive()) {

            currentState = EnemyState.DEAD;

            if (enemy.getAnimations().getCurrentState()
                != EnemyAnimationState.DEATH) {

                enemy.getAnimations().setState(
                    EnemyAnimationState.DEATH
                );
            }

            return;
        }

        if (!player.isAlive()) {

            currentState = EnemyState.IDLE;

            enemy.getAnimations().setState(
                EnemyAnimationState.IDLE
            );

            return;
        }

        float dx = (player.getX() + 25) - enemy.getCenterX();
        float dy = (player.getY() + 25) - enemy.getCenterY();

        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > detectionRange) {

            currentState = EnemyState.IDLE;

            enemy.getAnimations().setState(
                EnemyAnimationState.IDLE
            );

            return;
        }

        if (attackTimer > 0) {
            attackTimer -= delta;
        }

        if (distance <= attackRange) {

            if (enemy.getAnimations().getCurrentState()
                == EnemyAnimationState.DAMAGE) {
                return;
            }

            currentState = EnemyState.ATTACK;

            enemy.setFacingLeft(dx > 0);

            enemy.getAnimations().setState(
                EnemyAnimationState.ATTACK
            );

            if (attackTimer <= 0) {
                player.takeDamage(attackDamage);
                attackTimer = attackCooldown;
            }

            return;
        }

        currentState = EnemyState.CHASE;

        if (enemy.getAnimations().getCurrentState()
            != EnemyAnimationState.DAMAGE) {

            enemy.getAnimations().setState(
                EnemyAnimationState.WALK
            );
        }

        float directionX = dx / distance;
        float directionY = dy / distance;

        enemy.setFacingLeft(directionX > 0);

        float deltaX = directionX * movementSpeed * delta;
        float deltaY = directionY * movementSpeed * delta;

        float newX = enemy.getX() + deltaX;

        if (
            collisionManager.canMoveTo(
                newX,
                enemy.getY(),
                enemy.getWidth(),
                enemy.getHeight()
            )
                &&
                buildingSystem.canMoveTo(
                    newX,
                    enemy.getY(),
                    enemy.getWidth(),
                    enemy.getHeight()
                )
        ) {
            enemy.move(deltaX, 0);
        }

        float newY = enemy.getY() + deltaY;

        if (
            collisionManager.canMoveTo(
                enemy.getX(),
                newY,
                enemy.getWidth(),
                enemy.getHeight()
            )
                &&
                buildingSystem.canMoveTo(
                    enemy.getX(),
                    newY,
                    enemy.getWidth(),
                    enemy.getHeight()
                )
        ) {
            enemy.move(0, deltaY);
        }
    }

    public EnemyState getCurrentState() {
        return currentState;
    }
}

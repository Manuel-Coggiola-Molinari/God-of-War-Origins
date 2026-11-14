package com.goworigins.enemy;

import com.goworigins.animation.DraugrAnimations;
import com.goworigins.animation.EnemyAnimationState;

public class Draugr extends Enemy {

    private final DraugrAnimations animations;

    public Draugr(float x, float y, int health) {
        this(x, y, health, EnemyType.COMMON);
    }

    public Draugr(float x, float y, int health, EnemyType type) {
        super(x, y, health, type);
        animations = new DraugrAnimations();
    }

    @Override
    public void takeDamage(int damage) {
        if (!isAlive()) {
            return;
        }

        super.takeDamage(damage);

        if (isAlive()) {
            animations.setState(EnemyAnimationState.DAMAGE);
        } else {
            animations.setState(EnemyAnimationState.DEATH);
        }
    }

    public DraugrAnimations getAnimations() {
        return animations;
    }

    public void updateAnimations(float delta) {
        animations.update(delta);
    }
}

package com.goworigins.enemy;

import com.goworigins.animation.PesadillaAnimations;

public class Pesadilla extends Enemy {

    private final PesadillaAnimations animations;

    public Pesadilla(float x, float y, int health) {
        super(x, y, health, EnemyType.COMMON);
        animations = new PesadillaAnimations();
    }

    public PesadillaAnimations getAnimations() {
        return animations;
    }

    public void dispose() {
        animations.dispose();
    }
}

package com.goworigins.animation;

public class EnemyAnimations {

    private AnimationManager idle;
    private AnimationManager walk;
    private AnimationManager attack;
    private AnimationManager damage;
    private AnimationManager death;

    private EnemyAnimationState currentState;

    public EnemyAnimations() {

        currentState = EnemyAnimationState.IDLE;

        idle = new AnimationManager(
            "assets/sprites/Enemies/Draugr/idle.png",
            84,
            128,
            0.75f
        );

        walk = new AnimationManager(
            "assets/sprites/Enemies/Draugr/walk.png",
            80,
            128,
            0.15f
        );

        attack = new AnimationManager(
            "assets/sprites/Enemies/Draugr/attack.png",
            128,
            167,
            0.15f
        );

        attack.setLooping(false);

        damage = new AnimationManager(
            "assets/sprites/Enemies/Draugr/damage.png",
            114,
            120,
            0.20f
        );

        damage.setLooping(false);

        death = new AnimationManager(
            "assets/sprites/Enemies/Draugr/death.png",
            96,
            128,
            0.15f
        );

        death.setLooping(false);
    }

    public void setState(EnemyAnimationState state) {

        if (currentState != state) {

            currentState = state;

            AnimationManager animation =
                getCurrentAnimation();

            if (animation != null) {
                animation.reset();
            }
        }
    }

    public AnimationManager getCurrentAnimation() {

        switch (currentState) {

            case WALK:
                return walk;

            case ATTACK:
                return attack;

            case DAMAGE:
                return damage;

            case DEATH:
                return death;

            case IDLE:
            default:
                return idle;
        }
    }

    public EnemyAnimationState getCurrentState() {
        return currentState;
    }

    public void update(float delta) {

        AnimationManager animation =
            getCurrentAnimation();

        if (animation != null) {

            animation.update(delta);

            if (
                currentState == EnemyAnimationState.ATTACK
                    &&
                    animation.isFinished()
            ) {
                setState(EnemyAnimationState.IDLE);
            }

            if (
                currentState == EnemyAnimationState.DAMAGE
                    &&
                    animation.isFinished()
            ) {
                setState(EnemyAnimationState.IDLE);
            }
        }
    }
}

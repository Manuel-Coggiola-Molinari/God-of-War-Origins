package com.goworigins.animation;

public class PlayerAnimations {

    private AnimationManager idle;
    private AnimationManager walk;
    private AnimationManager attack;
    private AnimationManager damage;
    private AnimationManager death;

    private AnimationState currentState;

    public PlayerAnimations() {
        currentState = AnimationState.IDLE;
        setIdle(new AnimationManager("assets/sprites/Characters/Kratos/idle.png", 94, 100, 0.15f));

        AnimationManager walkAnimation = new AnimationManager("assets/sprites/Characters/Kratos/walk.png", 85, 100, 0.15f);
        setWalk(walkAnimation);

        AnimationManager attackAnimation = new AnimationManager("assets/sprites/Characters/Kratos/attack.png", 125, 100, 0.1f);
        attackAnimation.setLooping(false);
        setAttack(attackAnimation);

        AnimationManager damageAnimation = new AnimationManager("assets/sprites/Characters/Kratos/damage.png", 103, 100, 0.5f);
        damageAnimation.setLooping(false);
        setDamage(damageAnimation);

        AnimationManager deathAnimation = new AnimationManager("assets/sprites/Characters/Kratos/death.png", 100, 70, 0.15f);
        deathAnimation.setLooping(false);
        setDeath(deathAnimation);
    }

    public void setIdle(AnimationManager animation) { idle = animation; }
    public void setWalk(AnimationManager animation) { walk = animation; }
    public void setAttack(AnimationManager animation) { attack = animation; }
    public void setDamage(AnimationManager animation) { damage = animation; }
    public void setDeath(AnimationManager animation) { death = animation; }
    public void setState(AnimationState state) {

        if (currentState != state) {
            currentState = state;

            AnimationManager animation = getCurrentAnimation();

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

    public AnimationState getCurrentState() {
        return currentState;
    }

    public void update(float delta) {

        AnimationManager animation = getCurrentAnimation();

        if (animation != null) {
            animation.update(delta);

            if (currentState == AnimationState.ATTACK && animation.isFinished())
            { setState(AnimationState.IDLE); }

            if (currentState == AnimationState.DAMAGE && animation.isFinished())
            { setState(AnimationState.IDLE); }
        }
    }
}

package com.goworigins.animation;

public class PlayerAnimations {

    private AnimationManager idle;
    private AnimationManager walk;
    private AnimationManager attack;
    private AnimationManager death;

    private AnimationState currentState;

    public PlayerAnimations() {
        currentState = AnimationState.IDLE;
        String[] idleFrames = {
            "assets/sprites/Characters/Kratos/idle/kratosIdle.png"
        };
        setIdle(new AnimationManager(idleFrames, 0.15f));

        String[] walkFrames = {
            "assets/sprites/Characters/Kratos/walk/walk.png",
            "assets/sprites/Characters/Kratos/walk/walk1.png",
            "assets/sprites/Characters/Kratos/walk/walk2.png",
            "assets/sprites/Characters/Kratos/walk/walk3.png",
            "assets/sprites/Characters/Kratos/walk/walk4.png",
            "assets/sprites/Characters/Kratos/walk/walk5.png",
            "assets/sprites/Characters/Kratos/walk/walk6.png",
            "assets/sprites/Characters/Kratos/walk/walk7.png"
        };
        setWalk(new AnimationManager(walkFrames, 0.15f));

        String[] attackFrames = {
            "assets/sprites/Characters/Kratos/attack/attack1.png",
            "assets/sprites/Characters/Kratos/attack/attack2.png",
            "assets/sprites/Characters/Kratos/attack/attack3.png",
            "assets/sprites/Characters/Kratos/attack/attack4.png",
            "assets/sprites/Characters/Kratos/attack/attack5.png"
        };
        AnimationManager attackAnimation = new AnimationManager(attackFrames, 0.1f);

        attackAnimation.setLooping(false);

        setAttack(attackAnimation);

        String[] deathFrames = { "assets/sprites/Characters/Kratos/death/death.png" };

        AnimationManager deathAnimation = new AnimationManager(deathFrames, 0.15f);

        deathAnimation.setLooping(false);

        setDeath(deathAnimation);
    }

    public void setIdle(AnimationManager animation) { idle = animation; }
    public void setWalk(AnimationManager animation) { walk = animation; }
    public void setAttack(AnimationManager animation) { attack = animation; }
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
        }
    }
}

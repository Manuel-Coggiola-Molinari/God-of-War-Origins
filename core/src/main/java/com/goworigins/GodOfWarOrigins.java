package com.goworigins;

import com.badlogic.gdx.Game;
import com.goworigins.screens.MenuScreen;

public class GodOfWarOrigins extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen(this));
    }
}

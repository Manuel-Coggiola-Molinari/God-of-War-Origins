package com.goworigins.render;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.goworigins.building.Building;

public class BuildingRenderer {

    private SpriteBatch batch;

    private Texture refugeTexture;

    public BuildingRenderer(SpriteBatch batch) {

        this.batch = batch;

        refugeTexture = new Texture(
            "sprites/Builds/Shelter/shelter.png"
        );
    }

    public void render(Building building) {

        float width = building.getWidth();
        float height = building.getHeight();

        batch.draw(
            refugeTexture,
            building.getX(),
            building.getY(),
            width,
            height
        );
    }

    public void dispose() {
        refugeTexture.dispose();
    }
}

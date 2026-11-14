package com.goworigins.render;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.goworigins.resources.ResourceManager;
import com.goworigins.resources.ResourceNode;

public class ResourceRenderer {

    private final ShapeRenderer shapeRenderer;

    public ResourceRenderer() {
        shapeRenderer = new ShapeRenderer();
    }

    public void render(ResourceManager resourceManager, OrthographicCamera camera) {

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (ResourceNode node : resourceManager.getResources()) {

            if (node.agotado()) {
                continue;
            }

            switch (node.getType()) {

                case ARBOL:
                    shapeRenderer.setColor(Color.GREEN);
                    break;

                case ROCA:
                    shapeRenderer.setColor(Color.GRAY);
                    break;

                case MINERAL:
                    shapeRenderer.setColor(Color.YELLOW);
                    break;
            }

            shapeRenderer.rect(
                node.getX(),
                node.getY(),
                node.getWidth(),
                node.getHeight()
            );
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}

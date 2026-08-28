package com.goworigins.collision;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager {

    private final List<Rectangle> collisionRectangles;

    public CollisionManager(TiledMap map) {

        collisionRectangles = new ArrayList<>();

        MapLayer collisionLayer = map.getLayers().get("Colisiones");

        if (collisionLayer == null) {
            return;
        }

        for (MapObject object : collisionLayer.getObjects()) {

            if (object instanceof RectangleMapObject) {

                Rectangle rectangle =
                    ((RectangleMapObject) object).getRectangle();

                collisionRectangles.add(new Rectangle(rectangle));
            }
        }
    }

    public boolean canMoveTo(float x, float y, float width, float height) {

        Rectangle playerRectangle =
            new Rectangle(x, y, width, height);

        for (Rectangle collision : collisionRectangles) {

            if (playerRectangle.overlaps(collision)) {
                return false;
            }
        }

        return true;
    }
}

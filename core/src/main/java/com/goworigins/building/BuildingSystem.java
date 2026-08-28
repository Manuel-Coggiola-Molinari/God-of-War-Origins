package com.goworigins.building;

import com.goworigins.player.Player;
import com.goworigins.collision.CollisionManager;
import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.math.Rectangle;

public class BuildingSystem {

    private final List<Building> buildings = new ArrayList<>();
    private BuildingType selectedType = BuildingType.REFUGIO;

    public void build(Player player, CollisionManager collisionManager) {

            float x = player.getX() + 100;
            float y = player.getY();

        Building newBuilding = new Building(x, y, selectedType);

        if (!collisionManager.canMoveTo(
            newBuilding.getX(),
            newBuilding.getY(),
            newBuilding.getWidth(),
            newBuilding.getHeight()
        )) {
            System.out.println("No se puede construir aqui");
            return;
        }
        if (!canBuildAt(
            newBuilding.getX(),
            newBuilding.getY(),
            newBuilding.getWidth(),
            newBuilding.getHeight()
        )) {
            System.out.println("No se puede construir sobre otra estructura");
            return;
        }
        buildings.add(newBuilding);
        System.out.println("Estructura construida");
    }

    public List<Building> getBuildings() {
        return buildings;
    }
    public boolean canBuildAt( float x, float y, float width, float height ) {
        Rectangle newBuildingHitbox = new Rectangle( x, y, width, height );

        for (Building building : buildings) {
            if (newBuildingHitbox.overlaps(building.getHitbox()))
            {   return false;   }
        }
        return true;
    }
    public boolean canMoveTo( float x, float y, float width, float height ) {

        Rectangle playerHitbox = new Rectangle( x, y, width, height );
        for (Building building : buildings) {

            if (playerHitbox.overlaps(
                building.getHitbox()
            )) { return false; }
        }
        return true;
    }
    public void setSelectedType(BuildingType type) {
        selectedType = type;
    }

    public BuildingType getSelectedType() {
        return selectedType;
    }
}

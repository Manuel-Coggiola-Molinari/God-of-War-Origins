
package com.goworigins.resources;

import com.goworigins.player.Player;
import java.util.ArrayList;

public class ResourceManager {

    private final ArrayList<ResourceNode> resources = new ArrayList<>();

    private static final float INTERACTION_DISTANCE = 100f;
    private static final float PLAYER_SIZE = 50f;

    public void agregarNodo(ResourceNode node) {
        resources.add(node);
    }

    public ResourceNode recolectarCercano(Player player, float directionX, float directionY) {

        float playerCenterX = player.getX() + PLAYER_SIZE / 2f;
        float playerCenterY = player.getY() + PLAYER_SIZE / 2f;

        ResourceNode closestNode = null;
        float closestDistance = INTERACTION_DISTANCE;

        for (ResourceNode node : resources) {

            if (node.agotado()) {
                continue;
            }

            float nodeCenterX = node.getX() + node.getWidth() / 2f;
            float nodeCenterY = node.getY() + node.getHeight() / 2f;

            float distanceX = nodeCenterX - playerCenterX;
            float distanceY = nodeCenterY - playerCenterY;

            float distance = (float) Math.sqrt(
                distanceX * distanceX + distanceY * distanceY
            );

            if (distance == 0 || distance > INTERACTION_DISTANCE) {
                continue;
            }

            float normalizedX = distanceX / distance;
            float normalizedY = distanceY / distance;

            float directionDot =
                normalizedX * directionX +
                    normalizedY * directionY;

            // Solo permite recolectar recursos delante del jugador.
            if (directionDot >= 0.5f && distance < closestDistance) {

                closestDistance = distance;
                closestNode = node;
            }
        }

        if (closestNode != null && closestNode.recolectar()) {
            return closestNode;
        }

        return null;
    }
    public ArrayList<ResourceNode> getResources() {
        return resources;
    }
}

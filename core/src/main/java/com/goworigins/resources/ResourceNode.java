
package com.goworigins.resources;

public class ResourceNode {

    public enum ResourceType {
        ARBOL,
        ROCA,
        MINERAL
    }

    private final ResourceType type;
    private final float x;
    private final float y;
    private final float width;
    private final float height;
    private int resources;
    private final int resourcesPerGather;

    public ResourceNode(
        ResourceType type,
        float x,
        float y,
        float width,
        float height,
        int resources,
        int resourcesPerGather
    ) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.resources = resources;
        this.resourcesPerGather = resourcesPerGather;
    }

    public boolean recolectar() {
        if (resources <= 0) {
            return false;
        }

        resources -= Math.min(resources, resourcesPerGather);
        return true;
    }

    public boolean agotado() {
        return resources <= 0;
    }

    public ResourceType getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public int getResources() {
        return resources;
    }
}

package com.example.starwing.Utils;

public class BoundingBox {
    GraphicUtils.vec3 min;
    GraphicUtils.vec3 max;

    public BoundingBox() {
        min = new GraphicUtils.vec3(100,100,100);
        max = new GraphicUtils.vec3(-100,-100,-100);
    }

    public void update(GraphicUtils.vec3 vertex) {
        min.x = Math.min(min.x, vertex.x);
        min.y = Math.min(min.y, vertex.y);
        min.z = Math.min(min.z, vertex.z);
        max.x = Math.max(max.x, vertex.x);
        max.y = Math.max(max.y, vertex.y);
        max.z = Math.max(max.z, vertex.z);
    }

    public boolean isColliding(BoundingBox other) {
        return min.x <= other.max.x && max.x >= other.min.x &&
                min.y <= other.max.y && max.y >= other.min.y &&
                min.z <= other.max.z && max.z >= other.min.z;
    }
}
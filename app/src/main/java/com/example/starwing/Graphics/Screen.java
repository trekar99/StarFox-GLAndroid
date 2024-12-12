package com.example.starwing.Graphics;

import com.example.starwing.Utils.Mesh;

public class Screen extends Mesh {
    /**
     * Create a plane with a default with and height of 1 unit.
     */
    public Screen() {
        this(1, 1);
    }

    /**
     * Create a plane.
     *
     * @param width
     *            the width of the plane.
     * @param height
     *            the height of the plane.
     */
    public Screen(float width, float height) {
        // Mapping coordinates for the vertices
        float textureCoordinates[] = { 0.0f, 1.0f, //
                1.0f, 1.0f, //
                0.0f, 0.0f, //
                1.0f, 0.0f, //
        };

        short[] indices = new short[] { 0, 1, 2, 1, 3, 2 };

        float[] vertices = new float[] { -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f,
                -0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.0f };

        setIndices(indices);
        setVertices(vertices);
        setTextureCoordinates(textureCoordinates);
    }
}

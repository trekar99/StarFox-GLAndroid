package com.example.starwing.Graphics;

import com.example.starwing.Utils.Mesh;

public class Background extends Mesh {
    public Background() {
        float vertices[] = {
                -1.0f, -1.0f, 0.0f,
                1.0f,  -1.0f, 0.0f,
                -1.0f, 1.0f,0.0f,
                1.0f,  1.0f, 0.0f
        };

        short faces[] = { 0, 1, 2, 1, 3, 2 };

        float[] texCoords = { // Texture coords for the above face (NEW)
                0.0f, 1.0f,  // A. left-bottom (NEW)
                1.0f, 1.0f,  // B. right-bottom (NEW)
                0.0f, 0.0f,  // C. left-top (NEW)
                1.0f, 0.0f   // D. right-top (NEW)
        };

        setVertices(vertices);
        setIndices(faces);
        setTextureCoordinates(texCoords);
    }
}

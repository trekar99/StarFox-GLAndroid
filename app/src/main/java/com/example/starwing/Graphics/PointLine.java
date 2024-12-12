/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing.Graphics;

import android.opengl.GLES10;

import com.example.starwing.Utils.GraphicUtils;

import java.nio.FloatBuffer;

public class PointLine {
    private final FloatBuffer ver = GraphicUtils.ConvToFloatBuffer(new float[]{0.0f, 0.0f, 0.0f});

    public PointLine(){ GLES10.glPointSize(5); }

    public PointLine(float pointSize){ GLES10.glPointSize(pointSize); }

    public void draw(float bias){
        GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, ver);
        GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);

        GLES10.glDrawArrays(GLES10.GL_POINTS, 0, 1);
        GLES10.glPushMatrix();

        float staticBias = 1.0f; // X Coord separation
        for (int j = 1; j < 10; j++) {
            GLES10.glTranslatef(staticBias + bias/3, 0, 0);
            GLES10.glDrawArrays(GLES10.GL_POINTS, 0, 1);
        }
        GLES10.glPopMatrix();

        GLES10.glPushMatrix();
        for (int j = 1; j < 10; j++) {
            GLES10.glTranslatef(-(staticBias + bias/3), 0, 0);
            GLES10.glDrawArrays(GLES10.GL_POINTS, 0, 1);
        }
        GLES10.glPopMatrix();

        GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);

    }
}

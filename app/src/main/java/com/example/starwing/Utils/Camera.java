/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing.Utils;

import android.opengl.GLES10;
import android.opengl.GLU;

import javax.microedition.khronos.opengles.GL10;

public class Camera {

    private final GL10 gl;
    private int height, width;
    private boolean firstPerson = true;
    private float prevX, prevY, prevZ = 0;
    private float x, y, z = 0;

    public Camera(GL10 gl, int width, int height){
        setWidthHeight(width,height);
        this.gl = gl;
    }

    public void setWidthHeight(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void setDeviceRotation(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getRotationX() { return x; }

    public float getRotationY() { return y; }

    public float getRotationZ() { return z; }


    public void changeView() { this.firstPerson = !this.firstPerson; }

    public void setOrtographic(){
        GLES10.glMatrixMode(GLES10.GL_PROJECTION);
        GLES10.glLoadIdentity();
        GLES10.glOrthof(-5,5,-4,4,-5,5);
        GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
        GLES10.glLoadIdentity();
    }

    public void setPerspective(){
        GLES10.glMatrixMode(GLES10.GL_PROJECTION); // Select projection matrix
        GLES10.glLoadIdentity();                 // Reset projection matrix

        prevX += x;
        prevY -= y;
        prevZ += z;

        GLU.gluPerspective(gl, 60, (float) width / height, 0.1f, 100.f);

        // Use perspective projection
        if(firstPerson) {
            GLU.gluLookAt(gl, prevX/10, prevY/5 - 1, 0, 0, 0, -6, 0, 1, 0);
            GLES10.glRotatef(prevX, 0, 0, -1);
            GLES10.glRotatef(prevY*5, -1, 0, 0);
        }
        else {
            GLU.gluLookAt(gl, 0, 5, 0, 0, 0, -6, 0, 1, 0);
        }

        GLES10.glMatrixMode(GLES10.GL_MODELVIEW);  // Select model-view matrix
        GLES10.glLoadIdentity();
    }


}


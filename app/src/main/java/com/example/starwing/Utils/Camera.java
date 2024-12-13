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

    public Camera(GL10 gl, int width, int height){
        setWidthHeight(width,height);
        this.gl = gl;
    }

    public void setWidthHeight(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void changeView() { this.firstPerson = !this.firstPerson; }

    public void setOrtographic(){
        GLES10.glMatrixMode(GLES10.GL_PROJECTION);
        GLES10.glLoadIdentity();
        GLES10.glOrthof(-5,5,-4,4,-5,5);
        GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
        GLES10.glLoadIdentity();
    }

    public void setPerspective(){
        gl.glMatrixMode(GL10.GL_PROJECTION); // Select projection matrix
        gl.glLoadIdentity();                 // Reset projection matrix

        // Use perspective projection
        if(firstPerson) GLU.gluPerspective(gl, 60, (float) width / height, 0.1f, 100.f);
        else {
            GLU.gluPerspective(gl, 100, (float) width / height, 0.1f, 100.f);
            GLU.gluLookAt(gl, 0, 5, 0, 0, 0, -6, 0, 1, 0);
        }

        gl.glMatrixMode(GL10.GL_MODELVIEW);  // Select model-view matrix
        gl.glLoadIdentity();


    }


}


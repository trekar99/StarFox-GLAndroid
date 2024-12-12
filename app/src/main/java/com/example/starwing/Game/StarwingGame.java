/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing.Game;

import android.content.Context;
import android.opengl.GLES10;

import com.example.starwing.Graphics.PointLine;
import com.example.starwing.OpenGLView;
import com.example.starwing.R;
import com.example.starwing.Utils.Camera;
import com.example.starwing.Utils.GLTexture;
import com.example.starwing.Utils.GraphicUtils;
import com.example.starwing.Utils.Logger;
import com.example.starwing.Utils.Object3D;
import com.example.starwing.Graphics.Screen;
import com.example.starwing.Graphics.Square;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class StarwingGame {
    // Main propierties
    OpenGLView view;
    Context context;
    private Camera camera;
    private int width;
    private int height;

    // Background
    private Screen screen;
    private Square square;
    Object3D starship;
    private PointLine pointLine;

    // Textures
    private GLTexture bgTexture;
    private GLTexture IntroScreen;

    // Controls
    private float x, y = 0;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    // Auxiliar attributes
    private boolean loaded = false;
    private int velocity = 0;

    public void initialiseGame(GL10 gl, OpenGLView view){
        Logger.v(this, "Initializing the Game");

        // Main propierties
        this.view = view;
        this.context = view.getContext();

        // Init background
        //Create the objects
        square = new Square();
        square.loadTexture(gl, context);

        IntroScreen = new GLTexture(context,R.raw.mountains);
        screen = new Screen();

        this.starship = new Object3D(context, R.raw.star);
        pointLine = new PointLine();

        loaded = true;
        Logger.v(this, "Game Initialized Successfully");
    }

    public void runGame(GL10 gl){
        renderGame(gl);
    }

    public void pauseGame(){

    }

    public void resumeGame(){

    }

    public void loadGame(GL10 gl, Context context){
        Logger.v(this, "Game is LOADING");
    //
        float[] verts = {
                -1.0f, 1.0f, 0.0f,
                1.0f,  1.0f, 0.0f,
                -1.0f, -1.0f,0.0f,
                1.0f,  -1.0f, 0.0f
        };

        float[] texture = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f
        };


        FloatBuffer vertfb = GraphicUtils.ConvToFloatBuffer(verts);
        FloatBuffer texfb = GraphicUtils.ConvToFloatBuffer(texture);

        GLES10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT | GLES10.GL_DEPTH_BUFFER_BIT);

        GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
        GLES10.glEnableClientState(GLES10.GL_TEXTURE_COORD_ARRAY);
        GLES10.glEnable(GLES10.GL_TEXTURE_2D);

        if(IntroScreen == null) IntroScreen = new GLTexture(context,R.raw.jacob);

        GLES10.glBindTexture(GLES10.GL_TEXTURE_2D, IntroScreen.getTextureID());

        GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, vertfb);
        GLES10.glTexCoordPointer(2, GLES10.GL_FLOAT, 0, texfb);
        GLES10.glDrawArrays(GLES10.GL_TRIANGLE_STRIP, 0, 4);

        GLES10.glDisable(GLES10.GL_TEXTURE_2D);

        GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);
        GLES10.glDisableClientState(GLES10.GL_TEXTURE_COORD_ARRAY);
    //
        Logger.v(this,"Game is LOADED");
    }

    public void updateScreenSize(int width, int height){
        Logger.v(this, "Game commanded to update the Screen Size");

        //if(camera == null) camera = new Camera(width, height);
        //else camera.SetWidthHeight(width, height);

        this.width = width;
        this.height = height;





        Logger.v(this, "Screen Update complete");
    }

    public void addTouchEvent(float x, float y){
        if(loaded) {
            float dx = (x / width - 0.5f);
            float dy = - (y / height - 0.5f);

            this.setX(this.getX()+((dx>0)?0.1f:-0.1f));
            this.setY(this.getY()+((dy>0)?0.1f:-0.1f));
        }
    }

    private void renderGame(GL10 gl){
        // LEVEL 1
        // Clears the screen and depth buffer.
        GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT | GLES10.GL_DEPTH_BUFFER_BIT);
        // Replace the model matrix with the identity matrix
        GLES10.glLoadIdentity();
        // Translates 6 units into the screen.
        GLES10.glTranslatef(0, 0, -6);

        // Draw Background
        GLES10.glPushMatrix();
        GLES10.glTranslatef(0, 5f, 0);
        GLES10.glScalef(10, 12, 1);
        square.draw(0);
        GLES10.glPopMatrix();

        // Draw the starship.
        GLES10.glPushMatrix();
        GLES10.glTranslatef(x, y-2, 0);
        starship.draw();
        GLES10.glPopMatrix();

        // Draw points.
        GLES10.glTranslatef(0, -0.2f, 0);

        velocity = this.view.getFrameCount() % 10;
        GLES10.glTranslatef(0, (float) -velocity /25, (float) velocity /10);

        for (int i = 0; i < 12; i++) {
            pointLine.draw((float)i);
            GLES10.glTranslatef(0, -0.6f, 0);
        }

    }
}

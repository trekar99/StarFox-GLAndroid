/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing.Game;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.example.starwing.OpenGLView;
import com.example.starwing.R;
import com.example.starwing.Utils.Camera;
import com.example.starwing.Utils.GLTexture;
import com.example.starwing.Utils.GraphicUtils;
import com.example.starwing.Utils.Logger;
import com.example.starwing.World.Background;
import com.example.starwing.World.Screen;
import com.example.starwing.World.Square;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class StarwingGame {
    // Main propierties
    Context context;
    private Camera camera;
    private int width;
    private int height;

    // Background
    //private Background background;
    private Screen screen;
    private Square square;

    // Textures
    private GLTexture bgTexture;
    private GLTexture IntroScreen;

    // Auxiliar attributes
    private boolean loaded = false;

    public void initialiseGame(GL10 gl, Context context){
        Logger.v(this, "Initializing the Game");

        // Init background
        //Create the objects
        square = new Square();
        square.loadTexture(gl, context);

        IntroScreen = new GLTexture(context,R.raw.mountains);
        screen = new Screen();

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
        float verts[] = {
                -1.0f, 1.0f, 0.0f,
                1.0f,  1.0f, 0.0f,
                -1.0f, -1.0f,0.0f,
                1.0f,  -1.0f, 0.0f
        };

        float texture[] = {
                0.0f, 1.0f,
                1.0f, 1.0f,
                0.0f, 0.0f,
                1.0f, 0.0f
        };

        this.context = context;

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
            Logger.v(this, "ME TOCO");
        }
    }

    private void renderGame(GL10 gl){
        // Clears the screen and depth buffer.
        GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT | GLES10.GL_DEPTH_BUFFER_BIT);
        // Replace the current matrix with the identity matrix
        GLES10.glLoadIdentity();
        // Translates 6 units into the screen.
        GLES10.glTranslatef(0, 0, -6);

        // Draw our square.
//        float width = Resources.getSystem().getDisplayMetrics().widthPixels;
//        float height = Resources.getSystem().getDisplayMetrics().heightPixels;

        GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
        GLES10.glLoadIdentity();
        GLES10.glTranslatef(0, 0.5f, 0);
        GLES10.glScalef(3, 2, 1);



        GLES10.glMatrixMode(GLES10.GL_PROJECTION);
        GLES10.glLoadIdentity();


        //GLU.gluPerspective(gl, 60.0f, (float) width / (float) height, 0.1f, 1000.0f);


        square.draw(0);


        // Draw our square.
        GLES10.glPushMatrix();
        GLES10.glMatrixMode(GL10.GL_PROJECTION);
        // Reset the projection matrix
        GLES10.glLoadIdentity();
        // Calculate the aspect ratio of the window
        //GLES10.glFrustumf(-1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 100.0f);
        GLU.gluPerspective(gl, 60.0f, (float) width / (float) height, 0.1f, 1000.0f);
        GLES10.glMatrixMode(GLES10.GL_MODELVIEW);
        GLES10.glLoadIdentity();
        GLES10.glTranslatef(0, 0, -6);

        square.draw();
        GLES10.glPopMatrix();

    }
}

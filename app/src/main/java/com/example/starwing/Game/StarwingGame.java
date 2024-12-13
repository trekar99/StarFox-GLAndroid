/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing.Game;

import android.content.Context;
import android.opengl.GLES10;
import android.opengl.GLU;

import com.example.starwing.Graphics.Background;
import com.example.starwing.Graphics.HUD;
import com.example.starwing.Graphics.PointLine;
import com.example.starwing.OpenGLView;
import com.example.starwing.R;
import com.example.starwing.Utils.Camera;
import com.example.starwing.Utils.GraphicUtils;
import com.example.starwing.Utils.Logger;
import com.example.starwing.Utils.Object3D;

import javax.microedition.khronos.opengles.GL10;

public class StarwingGame {
    // Main propierties
    OpenGLView view;
    Context context;
    private Camera camera;

    // Background
    private Object3D starship;
    private PointLine pointLine;
    private Background background;

    // HUD
    private HUD hud;
    private final float MAX_SHIELD = 0.785f;
    private float shield = MAX_SHIELD;

    private final float MAX_BOOST = 0.785f;
    private float boost = MAX_BOOST;
    private boolean boostEnabled = false;

    // Controls
    private float x, y = 0;

    // Auxiliar attributes
    private boolean loaded = false;
    private int velocity = 0;

    public void initialiseGame(GL10 gl, OpenGLView view){
        Logger.v(this, "Initializing the Game");

        // Main propierties
        this.view = view;
        this.context = view.getContext();
        this.camera = new Camera(gl, view.getWidth(), view.getHeight());

        // Create the objects
        background = new Background();
        background.loadBitmap(GraphicUtils.loadBitmap(this.context, R.raw.mountains));

        this.starship = new Object3D(context, R.raw.arwing);
        starship.loadBitmap(GraphicUtils.loadBitmap(this.context, R.raw.arwingbody));

        pointLine = new PointLine();

        // HUD
        hud = new HUD(context);

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
//        float[] verts = {
//                -1.0f, 1.0f, 0.0f,
//                1.0f,  1.0f, 0.0f,
//                -1.0f, -1.0f,0.0f,
//                1.0f,  -1.0f, 0.0f
//        };
//
//        float[] texture = {
//                0.0f, 1.0f,
//                1.0f, 1.0f,
//                0.0f, 0.0f,
//                1.0f, 0.0f
//        };
//
//
//        FloatBuffer vertfb = GraphicUtils.ConvToFloatBuffer(verts);
//        FloatBuffer texfb = GraphicUtils.ConvToFloatBuffer(texture);
//
//        GLES10.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
//        GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT | GLES10.GL_DEPTH_BUFFER_BIT);
//
//        GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
//        GLES10.glEnableClientState(GLES10.GL_TEXTURE_COORD_ARRAY);
//        GLES10.glEnable(GLES10.GL_TEXTURE_2D);
//
//        if(IntroScreen == null) IntroScreen = new GLTexture(context,R.raw.jacob);
//
//        GLES10.glBindTexture(GLES10.GL_TEXTURE_2D, IntroScreen.getTextureID());
//
//        GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, vertfb);
//        GLES10.glTexCoordPointer(2, GLES10.GL_FLOAT, 0, texfb);
//        GLES10.glDrawArrays(GLES10.GL_TRIANGLE_STRIP, 0, 4);
//
//        GLES10.glDisable(GLES10.GL_TEXTURE_2D);
//
//        GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);
//        GLES10.glDisableClientState(GLES10.GL_TEXTURE_COORD_ARRAY);
    //
        Logger.v(this,"Game is LOADED");
    }

    public void updateScreenSize(int width, int height){

//        if(camera == null) camera = new Camera(width, height);
//        //else camera.SetWidthHeight(width, height);
//
//        this.width = width;
//        this.height = height;
    }

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

    public boolean isBoosted() { return this.boostEnabled; }

    public void enableBoost() { this.boostEnabled = true; }

    public void disableBoost() { this.boostEnabled = false; }

    public void addTouchEvent(float x, float y){
        if(loaded) {
            this.setX(this.getX()-x/900);
            this.setY(this.getY()+y/900);
        }
    }

    public Camera getCamera() { return camera;}

    private void renderGame(GL10 gl){
        int frameCount = this.view.getFrameCount();

        camera.setPerspective();
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
        background.draw();
        GLES10.glPopMatrix();

        // Draw points.
        GLES10.glPushMatrix();
        GLES10.glTranslatef(0, -0.2f, 0);

        velocity = boostEnabled ? frameCount % 4 : frameCount % 10;
        GLES10.glTranslatef(0, (float) -velocity /25, (float) velocity /10);

        for (int i = 0; i < 12; i++) {
            pointLine.draw((float)i);
            GLES10.glTranslatef(0, -0.6f, 0);
        }
        GLES10.glPopMatrix();

        // Draw the starship.
        GLES10.glPushMatrix();
        GLES10.glScalef(1,1,1);
        GLES10.glTranslatef(x, y, 0);
        GLES10.glRotatef(180f, 1, 0, 0);
        starship.draw();
        GLES10.glPopMatrix();

        // HUD
        //setOrthographicProjection(gl);
        camera.setOrtographic();
        if(boostEnabled && boost > 0) {
            boost -= ((1 * MAX_BOOST)/100);
            if(boost <= 0) disableBoost();
        }
        else if (!boostEnabled && boost < MAX_BOOST) boost += ((1 * MAX_BOOST)/500);
        hud.draw(shield, boost);

    }
}

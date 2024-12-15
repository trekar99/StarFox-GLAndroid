/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing.Game;

import android.content.Context;
import android.opengl.GLES10;

import com.example.starwing.Graphics.Background;
import com.example.starwing.Graphics.HUD;
import com.example.starwing.Graphics.PointLine;
import com.example.starwing.OpenGLView;
import com.example.starwing.R;
import com.example.starwing.Utils.Camera;
import com.example.starwing.Utils.GraphicUtils;
import com.example.starwing.Utils.Light;
import com.example.starwing.Utils.Logger;
import com.example.starwing.Utils.Object3D;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class StarwingGame {
    // Main propierties
    OpenGLView view;
    Context context;
    private Camera camera;
    private float x, y, z = 0;

    // Objects
    private Object3D starship;
    private Object3D coin;
    private PointLine pointLine;
    private Background background;

    // Lights
    private Light light;

    // HUD
    private HUD hud;
    private final float MAX_SHIELD = 0.785f;
    private float shield = MAX_SHIELD;

    private final float MAX_BOOST = 0.785f;
    private float boost = MAX_BOOST;
    private boolean boostEnabled = false;

    // Auxiliar attributes
    private boolean loaded = false;
    private int velocity = 0;
    Random random = new Random();
    float r = (random.nextFloat() - 0.5f) * 5f ;

    public void initialiseGame(GL10 gl, OpenGLView view){
        Logger.v(this, "Initializing the Game");

        // Main propierties
        this.view = view;
        this.context = view.getContext();
        this.camera = new Camera(gl, view.getWidth(), view.getHeight());

        // Create the objects
        background = new Background();
        background.loadBitmap(GraphicUtils.loadBitmap(this.context, R.raw.mountains));

        this.starship = new Object3D(context, R.raw.arw);
        starship.loadBitmap(GraphicUtils.loadBitmap(this.context, R.raw.arwbody));

        this.coin = new Object3D(context, R.raw.coin);
        coin.loadBitmap(GraphicUtils.loadBitmap(this.context, R.raw.gold));

        pointLine = new PointLine();

        // Lights

        float[] lightAmbient = {1f, 1f,1f, 1.0f};
        float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
        float[] lightPosition = {0.0f, 0.0f, -2.0f, 0.0f};

        gl.glEnable(GL10.GL_LIGHT0);

        light = new Light(gl, GL10.GL_LIGHT0);
        light.setPosition(lightPosition);
        light.setAmbientColor(lightAmbient);
        light.setDiffuseColor(lightDiffuse);

        // HUD
        hud = new HUD(context);

        loaded = true;
        Logger.v(this, "Game Initialized Successfully");
    }

    public void runGame(GL10 gl){
        if(loaded) renderGame(gl);
    }

    public void pauseGame(){

    }

    public void resumeGame(){

    }

    public void updateScreenSize(GL10 gl, int width, int height){
        if(camera == null) camera = new Camera(gl, width, height);
        camera.setWidthHeight(width, height);
    }

    public boolean isBoosted() { return this.boostEnabled; }

    public void enableBoost() { this.boostEnabled = true; }

    public void disableBoost() { this.boostEnabled = false; }

    public void setDeviceRotation(float x, float y, float z) { camera.setDeviceRotation(x, y, z); }

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

        // Draw coin
        gl.glEnable(GL10.GL_LIGHTING);
        // Enable Normalize
        gl.glEnable(GL10.GL_NORMALIZE);

        GLES10.glPushMatrix();
        GLES10.glScalef(0.05f,0.05f,0.05f);
        GLES10.glRotatef(180f, 0,1,0);
        if(frameCount % 100 == 0) r = (random.nextFloat() - 0.5f) * 100f ;
        z = - (float) frameCount % 100 ;
        GLES10.glTranslatef(r, z/2f + 10, z);
        GLES10.glTranslatef(0, (float) -velocity /25, (float) velocity /10);

        coin.draw();
        GLES10.glPopMatrix();

        // Draw the starship.

        GLES10.glPushMatrix();
        GLES10.glScalef(0.2f,0.2f,0.2f);
        GLES10.glRotatef(180f, 0, 1, 0);

        x -= camera.getRotationX();
        y -= camera.getRotationY() * 2;
        GLES10.glTranslatef(x * 3, y-3, (MAX_BOOST - boost)*15);

        GLES10.glRotatef(x*5, 0, 0, -1);
        GLES10.glRotatef(y*5, -1, 0, 0);

        starship.draw();
        GLES10.glPopMatrix();
        gl.glDisable(GL10.GL_LIGHTING);
        gl.glDisable(GL10.GL_NORMALIZE);



        // HUD
        camera.setOrtographic();
        if(boostEnabled && boost > 0) {
            boost -= ((1 * MAX_BOOST)/100);
            if(boost <= 0) disableBoost();
        }
        else if (!boostEnabled && boost < MAX_BOOST) boost += ((1 * MAX_BOOST)/100);
        hud.draw(shield, boost);
    }
}

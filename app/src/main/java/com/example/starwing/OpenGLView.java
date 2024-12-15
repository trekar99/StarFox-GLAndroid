/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.starwing.Game.GestureListener;
import com.example.starwing.Game.StarwingGame;
import com.example.starwing.Graphics.Background;
import com.example.starwing.Utils.GraphicUtils;
import com.example.starwing.Utils.Logger;

public class OpenGLView extends GLSurfaceView implements Renderer {

	StarwingGame Game;
	Context context;
	GestureDetector gestureDetector;

	private int frameCount = 0;
	private boolean start = false;

	private Background intro;

	public OpenGLView(Context context){
		super(context);
		Logger.v(this, "View's constructor called");

		setRenderer(this);
		Game = new StarwingGame();
		this.context = context;
		gestureDetector = new GestureDetector(context, new GestureListener(Game));

		Logger.v(this, "All set for View");
	}

	public void onPause(){
		Logger.v(this, "View Paused");
		Game.pauseGame();
	}

	public void onResume(){
		Logger.v(this, "View Resumed");
		Game.resumeGame();
	}

	public boolean onTouchEvent(final MotionEvent event){
		if(!start) start = true;
		return gestureDetector.onTouchEvent(event);
	}

	public void setDeviceRotation(float x, float y, float z) { Game.setDeviceRotation(x, y, z); }

	public void onSurfaceCreated(GL10 gl, EGLConfig config){
		Logger.Debug(this, "Surface Created, Do perspective");
		intro = new Background();
		intro.loadBitmap(GraphicUtils.loadBitmap(this.context, R.raw.intro));

		intro.draw();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height){
		Logger.Debug(this, "Surface changed, Update the game screen");
		Game.updateScreenSize(gl, width, height);
	}

	public int getFrameCount() { return frameCount; }

	@Override
	public void onDrawFrame(GL10 gl){
		if(frameCount == 1)
		{
			Logger.v(this, "Drawing the first frame for the game");
			Game.initialiseGame(gl, this);
		}
		else if(!start) intro.draw();
		else if(frameCount > 1) Game.runGame(gl);


		frameCount++;
	}


}

/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.starwing.Utils.Logger;

public class StarWing extends Activity {
    private GLSurfaceView view;

    private Boolean _FocusChangeFalseSeen = false;
    private Boolean _Resume = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.v(this,"Starting up the application");
        super.onCreate(savedInstanceState);

        Logger.v(this,"Setting up fullscreen flags");
        setImmersiveMode();

        Logger.v(this,"Setting up the View");
        view = new OpenGLView(this);
        setContentView(view);

        Logger.v(this,"onCreate function ended");
    }

    private void setImmersiveMode(){
        Logger.v(this, "Starting the setImmersiveMode()");
        int newUiOptions = getWindow().getDecorView().getSystemUiVisibility();

        //Set the Flags for maximum Screen utilization
        //We have Immersive mode available
        newUiOptions = newUiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        newUiOptions = newUiOptions | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        newUiOptions = newUiOptions | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        //All flags are a go, let it rip!!
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);

        Logger.v(this, "Ending the setImmersiveMode()");
    }

    @Override
    public void onPause(){
        Logger.v(this, "Application Paused");
        view.onPause();
        super.onPause();
    }

    @Override
    public void onResume(){
        Logger.v(this, "Application Resumed");
        if(!_FocusChangeFalseSeen)
        {
            view.onResume();
        }
        setImmersiveMode();
        _Resume = true;
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean focus){
        Logger.v(this, "Window Focus Changed");
        if(focus)
        {
            if(_Resume)
            {
                view.onResume();
            }

            _Resume = false;
            _FocusChangeFalseSeen = false;
        }
        else
        {
            _FocusChangeFalseSeen = true;
        }
    }
}
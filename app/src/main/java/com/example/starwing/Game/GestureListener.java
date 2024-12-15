package com.example.starwing.Game;

import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.starwing.Utils.Logger;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {

    StarwingGame Game;

    public GestureListener(StarwingGame Game){ this.Game = Game; }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    // event when double tap occurs
    @Override
    public boolean onDoubleTap(MotionEvent e) {
        if(Game.isBoosted()) Game.disableBoost();
        else Game.enableBoost();

        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        Game.getCamera().changeView();

        Logger.v("Long Press", "LONG PRESS: (" + x + "," + y + ")");
    }

//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Game.addTouchEvent(distanceX, distanceY);
//        return true;
//    }
}

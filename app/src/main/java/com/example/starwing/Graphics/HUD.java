package com.example.starwing.Graphics;

import android.content.Context;
import android.opengl.GLES10;

import com.example.starwing.R;
import com.example.starwing.Utils.GraphicUtils;

public class HUD {

    private final Background shieldBar;
    private final Background shieldCurr;

    private final Background boostBar;
    private final Background boostCurr;

    public HUD(Context context){
        shieldBar = new Background();
        shieldBar.loadBitmap(GraphicUtils.loadBitmap(context, R.raw.shield_bar));
        shieldBar.enableTransparency();

        shieldCurr = new Background();
        shieldCurr.setColor(1, 0, 0, 1);

        boostBar = new Background();
        boostBar.loadBitmap(GraphicUtils.loadBitmap(context, R.raw.boost_bar));
        boostBar.enableTransparency();

        boostCurr = new Background();
        boostCurr.setColor(0, 0,1,1);
    }

    public void draw(float shield, float boost){
        GLES10.glPushMatrix();
        GLES10.glTranslatef(-4f,-2.3f,0);
        GLES10.glScalef(1f,1f,1f);
        shieldBar.draw();

        GLES10.glPushMatrix();
        GLES10.glTranslatef(-0.91f + shield,-0.775f,0);
        GLES10.glScalef(shield,0.125f,1f);
        shieldCurr.draw();
        GLES10.glPopMatrix();

        GLES10.glTranslatef(8f,0,0);
        boostBar.draw();

        GLES10.glTranslatef(-0.66f + boost,-0.775f,0);
        GLES10.glScalef(boost,0.125f,1f);
        boostCurr.draw();

        GLES10.glPopMatrix();
    }
}

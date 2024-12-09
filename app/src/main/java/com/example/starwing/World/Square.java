package com.example.starwing.World;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLUtils;

import com.example.starwing.R;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square {
    float vertices[] = {
            -1.0f, 1.0f, 0.0f,
            1.0f,  1.0f, 0.0f,
            -1.0f, -1.0f,0.0f,
            1.0f,  -1.0f, 0.0f
    };

    private short faces[] = { 0, 1, 2, 0, 2, 3 };
    private float colors[] = {
            1,0,0,1,
            0,1,0,1,
            0,0,1,1,
            1,0,1,1
    };
    float[] texCoords = { // Texture coords for the above face (NEW)
            0.0f, 1.0f,  // A. left-bottom (NEW)
            1.0f, 1.0f,  // B. right-bottom (NEW)
            0.0f, 0.0f,  // C. left-top (NEW)
            1.0f, 0.0f   // D. right-top (NEW)
    };

    // Textures
    int[] textureIDs = new int[1];

    // Buffers
    private FloatBuffer vertexBuffer;
    private FloatBuffer colorBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer texBuffer;

    private boolean colorEnabled = false;

    public Square() {
        //Move the vertices list into a buffer
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        //Move the faces list into a buffer
        ByteBuffer ibb = ByteBuffer.allocateDirect(faces.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(faces);
        indexBuffer.position(0);

        // Setup texture-coords-array buffer, in float. An float has 4 bytes (NEW)
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();
        texBuffer.put(texCoords);
        texBuffer.position(0);

        setColor(colors);
    }

    public void setColor(float []colors) {
        ByteBuffer ibb = ByteBuffer.allocateDirect(colors.length * 4);
        ibb.order(ByteOrder.nativeOrder());
        colorBuffer = ibb.asFloatBuffer();
        colorBuffer.put(colors);
        colorBuffer.position(0);
    }

    public void enableColor() { colorEnabled = true; }
    public void disableColor() { colorEnabled = false; }

    // Load an image into GL texture
    public void loadTexture(GL10 gl, Context context) {
        gl.glGenTextures(1, textureIDs, 0); // Generate texture-ID array

        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[0]);   // Bind to texture ID
        // Set up texture filters
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);

        // Construct an input stream to texture image "res\drawable\nehe.png"
        InputStream istream = context.getResources().openRawResource(R.raw.jacob);

        Bitmap bitmap;
        try {
            // Read and decode input as bitmap
            bitmap = BitmapFactory.decodeStream(istream);
        } finally {
            try {
                istream.close();
            } catch(IOException e) { }
        }

        // Build Texture from loaded bitmap for the currently-bind texture ID
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    // Draw the shape
    public void draw(int aux) {
//        gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
//        gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
//        gl.glCullFace(GL10.GL_BACK);    // Cull the back face (don't display)
        GLES10.glEnable(GL10.GL_TEXTURE_2D);  // Enable texture

        GLES10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        GLES10.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        GLES10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Enable texture-coords-array
        GLES10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer); // Define texture-coords buffer

        // front
        GLES10.glPushMatrix();
        GLES10.glTranslatef(0.0f, 0.0f, 1.0f);
        GLES10.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
        GLES10.glPopMatrix();


        GLES10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);  // Disable texture-coords-array (NEW)
        GLES10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        //GLES10.glDisable(GL10.GL_CULL_FACE);
        GLES10.glDisable(GL10.GL_TEXTURE_2D);  // Enable texture (NEW));
    }

    public void draw() {
        // Enabled the color buffer for writing and to be used during rendering.
        GLES10.glEnableClientState(GLES10.GL_COLOR_ARRAY);

        // Enabled the vertices buffer for writing and to be used during rendering.
        GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);

        // Specifies the location and data format of an array of colors to use when rendering.
        GLES10.glColorPointer(4, GLES10.GL_FLOAT, 0, colorBuffer);

        // Specifies the location and data format of vertex array cords to use when rendering.
        GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, vertexBuffer);

        // Draw the square
        //gl.glDrawElements(GL10.GL_TRIANGLES, faces.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);
        GLES10.glDrawArrays(GLES10.GL_TRIANGLE_STRIP, 0, 4);

        // Disable the vertices buffer.
        GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);

        // Disable the colors buffer.
        GLES10.glDisableClientState(GLES10.GL_COLOR_ARRAY);
    }
}

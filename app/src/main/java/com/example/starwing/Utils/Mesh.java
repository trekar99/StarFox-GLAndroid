/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLUtils;

/**
 * Mesh is a base class for 3D objects making it easier to create and maintain
 * new primitives.
 *
 * @author Per-Erik Bergman (per-erik.bergman@jayway.com)
 *
 */
public class Mesh {
    // Our vertex buffer.
    private FloatBuffer mVerticesBuffer = null;

    // Our index buffer.
    private ShortBuffer mIndicesBuffer = null;

    // Our UV texture buffer.
    private FloatBuffer mTextureBuffer;

    // Our texture id.
    private int mTextureId = -1; // New variable.

    // The bitmap we want to load as a texture.
    private Bitmap mBitmap;

    // Indicates if we need to load the texture.
    private boolean mShouldLoadTexture = false;

    // The number of indices.
    private int mNumOfIndices = -1;

    // Flat Color
    private final float[] mRGBA = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };

    // Smooth Colors
    private FloatBuffer mColorBuffer = null;

    // Indicates if we need to make transparency.
    private boolean mTransparency = false;

    // Translate params.
    public float x = 0;

    public float y = 0;

    public float z = 0;

    // Rotate params.
    public float rx = 0;

    public float ry = 0;

    public float rz = 0;

    /**
     * Render the mesh.
     *
     */
    public void draw() {
        // Enabled the vertices buffer for writing and to be used during rendering.
        GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of an array of vertex coords to use when rendering.
        GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, mVerticesBuffer);
        // Set flat color
        GLES10.glColor4f(mRGBA[0], mRGBA[1], mRGBA[2], mRGBA[3]);
        // Smooth color
        if (mColorBuffer != null) {
            // Enable the color array buffer to be used during rendering.
            GLES10.glEnableClientState(GL10.GL_COLOR_ARRAY);
            GLES10.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        }

        // Load textures
        if (mShouldLoadTexture) {
            loadGLTexture();
            mShouldLoadTexture = false;
        }

        if (mTextureId != -1 && mTextureBuffer != null) {
            GLES10.glEnable(GL10.GL_TEXTURE_2D);
            // Enable the texture state
            GLES10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            // Point to our buffers
            GLES10.glTexCoordPointer(2, GLES10.GL_FLOAT, 0, mTextureBuffer);
            GLES10.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
        }

        GLES10.glTranslatef(x, y, z);
        GLES10.glRotatef(rx, 1, 0, 0);
        GLES10.glRotatef(ry, 0, 1, 0);
        GLES10.glRotatef(rz, 0, 0, 1);

        if(mTransparency) {
            GLES10.glEnable(GLES10.GL_BLEND);
            GLES10.glBlendFunc(GLES10.GL_SRC_ALPHA, GLES10.GL_ONE_MINUS_SRC_ALPHA);
        }

        // Point out the where the color buffer is.
        GLES10.glDrawElements(GLES10.GL_TRIANGLES, mNumOfIndices, GLES10.GL_UNSIGNED_SHORT, mIndicesBuffer);
        // Disable the vertices buffer.
        GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);

        if(mTransparency) GLES10.glDisable(GLES10.GL_BLEND);

        // End loading textures
        if (mTextureId != -1 && mTextureBuffer != null) {
            GLES10.glDisable(GL10.GL_TEXTURE_2D);
            GLES10.glDisableClientState(GLES10.GL_TEXTURE_COORD_ARRAY);
        }

        if (mColorBuffer != null) {
            // Enable the color array buffer to be used during rendering.
            GLES10.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }
    }

    /**
     * Set the vertices.
     *
     * @param vertices
     */
    protected void setVertices(float[] vertices) {
        mVerticesBuffer = GraphicUtils.ConvToFloatBuffer(vertices);
    }

    /**
     * Set the indices.
     *
     * @param indices
     */
    protected void setIndices(short[] indices) {
        mIndicesBuffer = GraphicUtils.ConvToShortBuffer(indices);
        mNumOfIndices = indices.length;
    }

    /**
     * Set the texture coordinates.
     *
     * @param textureCoords
     */
    protected void setTextureCoordinates(float[] textureCoords) {
        mTextureBuffer = GraphicUtils.ConvToFloatBuffer(textureCoords);
    }

    /**
     * Set one flat color on the mesh.
     *
     * @param red
     * @param green
     * @param blue
     * @param alpha
     */
    public void setColor(float red, float green, float blue, float alpha) {
        mRGBA[0] = red;
        mRGBA[1] = green;
        mRGBA[2] = blue;
        mRGBA[3] = alpha;
    }

    /**
     * Set the colors
     *
     * @param colors
     */
    protected void setColors(float[] colors) {
        mColorBuffer = GraphicUtils.ConvToFloatBuffer(colors);
    }

    /**
     * Activates transparency
     *
     */
    public void enableTransparency() { this.mTransparency = true; }

    /**
     * Set the bitmap to load into a texture.
     *
     * @param bitmap
     */
    public void loadBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
        mShouldLoadTexture = true;
    }

    /**
     * Loads the texture.
     *
     */
    private void loadGLTexture() {
        // Generate one texture pointer...
        int[] textures = new int[1];
        GLES10.glGenTextures(1, textures, 0);
        mTextureId = textures[0];

        // ...and bind it to our array
        GLES10.glBindTexture(GLES10.GL_TEXTURE_2D, mTextureId);

        // Create Nearest Filtered Texture
        GLES10.glTexParameterf(GLES10.GL_TEXTURE_2D, GLES10.GL_TEXTURE_MIN_FILTER,
                GLES10.GL_LINEAR);
        GLES10.glTexParameterf(GLES10.GL_TEXTURE_2D, GLES10.GL_TEXTURE_MAG_FILTER,
                GLES10.GL_LINEAR);

        // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
        GLES10.glTexParameterf(GLES10.GL_TEXTURE_2D, GLES10.GL_TEXTURE_WRAP_S,
                GLES10.GL_CLAMP_TO_EDGE);
        GLES10.glTexParameterf(GLES10.GL_TEXTURE_2D, GLES10.GL_TEXTURE_WRAP_T,
                GLES10.GL_REPEAT);

        // Use the Android GLUtils to specify a two-dimensional texture image
        // from our bitmap
        GLUtils.texImage2D(GLES10.GL_TEXTURE_2D, 0, mBitmap, 0);
    }
}
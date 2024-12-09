/*
 * Copyright © 2024/25 Germán Puerto
 *
 * Based on Starwing SNES
 */

package com.example.starwing.World;

import com.example.starwing.R;
import com.example.starwing.Utils.GLTexture;
import com.example.starwing.Utils.Mesh;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES10;
import android.opengl.GLUtils;

public class Background {
	private float vertices[] = {
			-1.0f, -1.0f, 0.0f,  // 0. left-bottom-front
			1.0f, -1.0f, 0.0f,  // 1. right-bottom-front
			-1.0f,  1.0f, 0.0f,  // 2. left-top-front
			1.0f,  1.0f, 0.0f   // 3. right-top-front
	};
	private short faces[] = { 0, 1, 2, 0, 2, 3 };


	private float[] texVertex = { // Texture coords for the above face (NEW)
			0.0f, 1.0f,  // A. left-bottom (NEW)
			1.0f, 1.0f,  // B. right-bottom (NEW)
			0.0f, 0.0f,  // C. left-top (NEW)
			1.0f, 0.0f   // D. right-top (NEW)
	};

	// Our vertex buffer.
	private FloatBuffer vertexBuffer;

	// Our index buffer.
	private ShortBuffer indexBuffer;

	private FloatBuffer texBuffer;
	int[] textureIDs = new int[1];   // Array for 1 texture-ID (NEW)

	private boolean colorEnabled = false;

	// News
	private GLTexture bgTexture;

	public Background() {
		//Move the vertices list into a buffer
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

//		//Move the faces list into a buffer
		ByteBuffer ibb = ByteBuffer.allocateDirect(faces.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(faces);
		indexBuffer.position(0);

		//Move the textures list into a buffer
		ByteBuffer tbb = ByteBuffer.allocateDirect(vertices.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texBuffer = tbb.asFloatBuffer();
		texBuffer.put(texVertex);
		texBuffer.position(0);
	}

	// Load an image into GL texture
	public void loadTexture(Context context) {
		GLES10.glGenTextures(1, textureIDs, 0); // Generate texture-ID array

		GLES10.glBindTexture(GLES10.GL_TEXTURE_2D, textureIDs[0]);   // Bind to texture ID
		// Set up texture filters
		GLES10.glTexParameterf(GLES10.GL_TEXTURE_2D, GLES10.GL_TEXTURE_MIN_FILTER, GLES10.GL_NEAREST);
		GLES10.glTexParameterf(GLES10.GL_TEXTURE_2D, GLES10.GL_TEXTURE_MAG_FILTER, GLES10.GL_LINEAR);

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
		GLUtils.texImage2D(GLES10.GL_TEXTURE_2D, 0, bitmap, 0);
		bitmap.recycle();
	}

	public void draw() {
		//gl.glColor4f(1, 0, 0, 1.0f);
		GLES10.glEnableClientState(GLES10.GL_COLOR_ARRAY);

		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		//gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

		// Enabled the vertices buffer for writing and to be used during rendering.
		GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);

		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, vertexBuffer);

		GLES10.glDrawElements(GLES10.GL_TRIANGLES, faces.length,
				GLES10.GL_UNSIGNED_SHORT, indexBuffer);

		// Disable the vertices buffer.
		GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);
		GLES10.glDisableClientState(GLES10.GL_COLOR_ARRAY);// Enable texture (NEW));
	}
}

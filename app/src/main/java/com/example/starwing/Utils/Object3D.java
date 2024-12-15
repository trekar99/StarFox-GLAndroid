package com.example.starwing.Utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES10;
import android.opengl.GLUtils;

public class Object3D {

    // Color enabled or not
    boolean colorEnabled = false;

    // Texture enabled or not
    boolean textureEnabled = false;

    // Indicates if we need to load the texture.
    private boolean mShouldLoadTexture = false;

    // The bitmap we want to load as a texture.
    private Bitmap mBitmap;

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our normal buffer.
    private FloatBuffer normalBuffer;

    // Our index buffer.
    private ShortBuffer indexBuffer;

    // Our texture buffer.
    private FloatBuffer texcoordBuffer;

    private boolean fanEnabled = false;

    // Our texture id.
    private int mTextureId = -1; // New variable.

    int numFaceIndexs = 0;

    // Bounding Box
    public BoundingBox boundingBox;

    public Object3D(Context ctx, int filenameId) {

        boundingBox = new BoundingBox();
        try {
            String line;
            String[] tmp,ftmp;

            ArrayList<Float> vlist = new ArrayList<Float>();
            ArrayList<Float> tlist = new ArrayList<Float>();
            ArrayList<Float> nlist = new ArrayList<Float>();
            ArrayList<Integer> vindex = new ArrayList<Integer>();
            ArrayList<Integer> tindex = new ArrayList<Integer>();
            ArrayList<Integer> nindex = new ArrayList<Integer>();

            InputStream is = ctx.getResources().openRawResource(filenameId);
            BufferedReader inb = new BufferedReader(new InputStreamReader(is), 1024);
            while ((line = inb.readLine()) != null) {
                tmp = line.split(" ");
                if (tmp[0].equalsIgnoreCase("v")) {

                    GraphicUtils.vec3 vec = new GraphicUtils.vec3(
                            Float.parseFloat(tmp[1]),
                            Float.parseFloat(tmp[2]),
                            Float.parseFloat(tmp[3]));
                    boundingBox.update(vec);
                    System.out.println(vec.x + " " + vec.y + " " + vec.z);
                    for (int i = 1; i < 4; i++) {
                        vlist.add( Float.parseFloat(tmp[i]) );

                    }

                }
                if (tmp[0].equalsIgnoreCase("vn")) {

                    for (int i = 1; i < 4; i++) {
                        nlist.add( Float.parseFloat(tmp[i]) );
                    }

                }
                if (tmp[0].equalsIgnoreCase("vt")) {
                    for (int i = 1; i < 3; i++) {
                        tlist.add( Float.parseFloat(tmp[i]) );
                    }

                }
                if (tmp[0].equalsIgnoreCase("f")) {
                    for (int i = 1; i < 4; i++) {
                        ftmp = tmp[i].split("/");

                        vindex.add(Integer.parseInt(ftmp[0]) - 1);
                        if (tlist.size()>0)
                            tindex.add(Integer.parseInt(ftmp[1]) - 1);
                        if (nlist.size()>0)
                            nindex.add(Integer.parseInt(ftmp[2]) - 1);

                        numFaceIndexs++;
                    }
                }
            }

            ByteBuffer vbb = ByteBuffer.allocateDirect(vindex.size() * 4 * 3);
            vbb.order(ByteOrder.nativeOrder());
            vertexBuffer = vbb.asFloatBuffer();

            for (int j = 0; j < vindex.size(); j++) {
                vertexBuffer.put(vlist.get( vindex.get(j)*3 ));
                vertexBuffer.put(vlist.get( vindex.get(j)*3+1 ));
                vertexBuffer.put(vlist.get( vindex.get(j)*3+2 ));
            }
            vertexBuffer.position(0);


            if (tindex.size()>0)  {
                ByteBuffer vtbb = ByteBuffer.allocateDirect(tindex.size() * 4 * 2);
                vtbb.order(ByteOrder.nativeOrder());
                texcoordBuffer = vtbb.asFloatBuffer();

                for (int j = 0; j < tindex.size(); j++) {
                    texcoordBuffer.put(tlist.get( tindex.get(j)*2 ));
                    texcoordBuffer.put(tlist.get( tindex.get(j)*2+1 ));
                }
                texcoordBuffer.position(0);
            }

            if(nindex.size()>0) {
                ByteBuffer nbb = ByteBuffer.allocateDirect(nindex.size() * 4 * 3);
                nbb.order(ByteOrder.nativeOrder());
                normalBuffer = nbb.asFloatBuffer();

                for (int j = 0; j < nindex.size(); j++) {
                    normalBuffer.put(nlist.get( nindex.get(j)*3 ));
                    normalBuffer.put(nlist.get( nindex.get(j)*3+1 ));
                    normalBuffer.put(nlist.get( nindex.get(j)*3+2 ));
                }
                normalBuffer.position(0);
            }

            ByteBuffer ibb = ByteBuffer.allocateDirect(numFaceIndexs * 2);
            ibb.order(ByteOrder.nativeOrder());
            indexBuffer = ibb.asShortBuffer();

            for (short j = 0; j < numFaceIndexs; j++) {
                indexBuffer.put(j);
            }
            indexBuffer.position(0);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

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

        textureEnabled = true;
    }

    public void enableFan() { fanEnabled = true; }

    public void draw() {
        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        GLES10.glColor4f(1,1,1,1);
        GLES10.glEnableClientState(GLES10.GL_VERTEX_ARRAY);

        // Load textures
        if (mShouldLoadTexture) {
            loadGLTexture();
            mShouldLoadTexture = false;
        }

        //////////////////////// NEW ////////////////////////////////
        GLES10.glEnableClientState(GLES10.GL_NORMAL_ARRAY);
        //////////////////////// NEW ////////////////////////////////

        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        GLES10.glVertexPointer(3, GLES10.GL_FLOAT, 0, vertexBuffer);

        //////////////////////// NEW ////////////////////////////////
        if(normalBuffer != null) GLES10.glNormalPointer(GLES10.GL_FLOAT, 0, normalBuffer);
        //////////////////////// NEW ////////////////////////////////

        if (textureEnabled) {
            GLES10.glEnable(GL10.GL_TEXTURE_2D);
            // Enable the texture state
            GLES10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

            // Point to our buffers
            GLES10.glTexCoordPointer(2, GLES10.GL_FLOAT, 0, texcoordBuffer);
            GLES10.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId);
        }

        if(fanEnabled) GLES10.glDrawElements(GLES10.GL_TRIANGLE_FAN, numFaceIndexs, GLES10.GL_UNSIGNED_SHORT, indexBuffer);
        else GLES10.glDrawElements(GLES10.GL_TRIANGLES, numFaceIndexs, GLES10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        GLES10.glDisableClientState(GLES10.GL_VERTEX_ARRAY);
        if(textureEnabled) {
            GLES10.glDisable(GL10.GL_TEXTURE_2D);
            GLES10.glDisableClientState(GLES10.GL_TEXTURE_COORD_ARRAY);
        }

        //////////////////////// NEW ////////////////////////////////
        GLES10.glDisableClientState(GLES10.GL_NORMAL_ARRAY);
        //////////////////////// NEW ////////////////////////////////
    }
}

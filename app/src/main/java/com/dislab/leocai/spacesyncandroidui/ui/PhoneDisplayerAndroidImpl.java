package com.dislab.leocai.spacesyncandroidui.ui;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import com.dislab.leocai.spacesync.ui.PhoneDisplayer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PhoneDisplayerAndroidImpl extends GLSurfaceView implements GLSurfaceView.Renderer, PhoneDisplayer {
    private Square square  = new Square();

    public PhoneDisplayerAndroidImpl(Context context) {
        super(context);
        setRenderer(this);

    }

    @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            Log.d("MyOpenGLRenderer", "Surface changed. Width=" + width
                    + " Height=" + height);


            gl.glViewport(0, 0, width, height);
            gl.glMatrixMode(GL10.GL_PROJECTION);
            gl.glLoadIdentity();
            GLU.gluPerspective(gl, 45.0f, (float) width / (float) height,
                    0.1f, 100.0f);
            gl.glMatrixMode(GL10.GL_MODELVIEW);
            gl.glLoadIdentity();
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            Log.d("MyOpenGLRenderer", "Surface created");
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
            gl.glLoadIdentity();
            gl.glTranslatef(0.0f, 0.0f, -5.0f);
          //  triangle1.draw(gl);
            square.draw(gl);



        }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    @Override
    public void initView() {
    }

    @Override
    public void updateView() {

    }

    @Override
    public void reset() {

    }

    @Override
    public void setRotationMatrix_b2g(double[][] rtm_b2g) {
        square.setRotateMatrix_B2G(rtm_b2g);

    }
}
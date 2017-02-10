package com.dislab.leocai.spacesyncandroidui.ui;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import com.dislab.leocai.spacesync.ui.PhoneDisplayer;
import com.dislab.leocai.spacesync.utils.MatrixUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class PhoneDisplayerAndroidImpl extends GLSurfaceView implements GLSurfaceView.Renderer, PhoneDisplayer {
    private Square square  = new Square();

    private CoordinatesUI coordinatesUI = new CoordinatesUI();
    private CoordinatesUI coordinatesUI_2 = new CoordinatesUI();
    private double[][] mat = new double[][]{
            {-1,0,0},
            {0,1,0},
            {0,0,1}
    };
    private double[][] rtm_mag_b2g;
    private double[][] rtm_b2g;


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
            gl.glRotatef(15, 1, 0, 0);
            gl.glRotatef(-10, 0, 1, 0);

            square.draw(gl);
            coordinatesUI.draw(gl);
            //TODO question
            mat = MatrixUtils.multiply(MatrixUtils.T(rtm_mag_b2g), rtm_b2g);
            coordinatesUI_2.setMat(mat);
            coordinatesUI_2.draw(gl);
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
        this.rtm_b2g = rtm_b2g;

    }

    @Override
    public void setMagRotationMatrix_b2g(double[][] rtm_mag_b2g){
        this.rtm_mag_b2g= rtm_mag_b2g;
    }
}
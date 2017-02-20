package com.dislab.leocai.spacesyncandroidui.ui;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import com.dislab.leocai.spacesync.ui.PhoneDisplayer;
import com.dislab.leocai.spacesync.utils.MatrixUtils;
import com.dislab.leocai.spacesync.utils.RotationUtils;

import java.util.Random;

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
    private double[][] rtm_mag_b2g = new double[][]{
            {-1,0,0},
            {0,1,0},
            {0,0,1}
    };
    private double[][] rtm_b2g = new double[][]{
            {-1,0,0},
            {0,1,0},
            {0,0,1}
    };
    private double angle = Math.PI/12;

    {
        coordinatesUI.setColor(new float[]{0f, 0.3f, 0.8f, 0.8f});
        coordinatesUI_2.setColor(new float[]{1f, 0f, 0f, 1});
        Random rand = new Random();
        double[] vg = new double[]{0,0,1};
//        double[] vg = new double[]{0,0,1};
        rtm_mag_b2g = MatrixUtils.T(RotationUtils.getRotationMatrixG2BBy2Vectors(vg, new double[]{0, 1, 3}));
        angle += rand.nextDouble()*0.17;
        double[][] mat_k = RotationUtils.quaternionToMatrix(angle, vg);
        rtm_b2g = MatrixUtils.multiply(rtm_mag_b2g, mat_k);


        double[][] mat_phone = RotationUtils.quaternionToMatrix(Math.PI/3+rand.nextDouble()*0.8, MatrixUtils.selectColumn(rtm_b2g,0));
        double[][] mat_phone_z = RotationUtils.quaternionToMatrix(rand.nextDouble()*0.5, MatrixUtils.selectColumn(rtm_b2g,2));
        square.setRotateMatrix_B2G(MatrixUtils.T(MatrixUtils.multiply(MatrixUtils.multiply(rtm_b2g, mat_phone),mat_phone_z)));
    }

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
            gl.glTranslatef(-1.5f, -0.2f, -5.0f);
          //  triangle1.draw(gl);
            gl.glRotatef(15, 1, 0, 0);
            gl.glRotatef(-10, 0, 1, 0);

            square.draw(gl);
            coordinatesUI.draw(gl);
            //TODO question
            if(rtm_mag_b2g!=null && rtm_b2g!=null){
                coordinatesUI.setMat(MatrixUtils.T(rtm_mag_b2g));
                coordinatesUI_2.setMat(MatrixUtils.T(rtm_b2g));
            }
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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double[][] getSpaceSyncMatrix() {
        return rtm_b2g;
    }

}
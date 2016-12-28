package com.dislab.leocai.spacesyncandroidui.ui;

import com.dislab.leocai.spacesync.utils.MatrixUtils;
import com.dislab.leocai.spacesync.utils.RotationUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square {

    private double yawAngle = 0;
    private double rollAngle = 0;
    private double pitchAngle = 0;

    // Our vertices.
    private float[] vertices = {
            -0.5f, 1, 0,//0
            -0.5f, -1, 0,//1
            0.5f, -1, 0,//2
            0.5f, 1, 0,//3
    };


    // The order we like to connect them.
    private short[] indices = {0, 1, 2, 3};

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our index buffer.
    private ShortBuffer indexBuffer;
    private double angle = 1;
    private double[][] rotateMatrix = MatrixUtils.MATRIX_E;

    public double getYawAngle() {
        return yawAngle;
    }

    public void setYawAngle(double yawAngle) {
        this.yawAngle = yawAngle;
    }

    public double getRollAngle() {
        return rollAngle;
    }

    public void setRollAngle(double rollAngle) {
        this.rollAngle = rollAngle;
    }

    public double getPitchAngle() {
        return pitchAngle;
    }

    public void setPitchAngle(double pitchAngle) {
        this.pitchAngle = pitchAngle;
    }

    public Square() {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();

    }

    /**
     * This function draws our square on screen.
     *
     * @param gl
     */
    public void draw(GL10 gl) {
        double[][] vertexArray = MatrixUtils.floatArrayToMatrix(vertices);
//        vertexArray = EularRotate.rotateI2B(vertexArray,yawAngle,pitchAngle,rollAngle);
        vertexArray = MatrixUtils.multiply(rotateMatrix,MatrixUtils.T(vertexArray));
       // rollAngle+=0.01;
        float[] newV = MatrixUtils.matrixToFloatArray(MatrixUtils.T(vertexArray));
        vertexBuffer.put(newV);
        vertexBuffer.position(0);

        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);


        // Counter-clockwise winding.
        gl.glFrontFace(GL10.GL_CCW);
        // Enable face culling.
        gl.glEnable(GL10.GL_CULL_FACE);
        // What faces to remove with the face culling.
        gl.glCullFace(GL10.GL_BACK);

        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0,
                vertexBuffer);
        gl.glColor4f(1, 1, 0, 0.5f);


        gl.glDrawElements(GL10.GL_LINE_LOOP, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
    }

    public double[][] getRotateMatrix() {
        return rotateMatrix;
    }

    public void setRotateMatrix_B2G(double[][] rotateMatrix_B2G) {
        this.rotateMatrix = rotateMatrix_B2G;
    }
}
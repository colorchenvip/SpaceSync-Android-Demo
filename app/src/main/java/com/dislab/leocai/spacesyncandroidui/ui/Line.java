package com.dislab.leocai.spacesyncandroidui.ui;

import com.dislab.leocai.spacesync.utils.MatrixUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Line {


    // Our vertices.
    private float[] vertices = {
            -1f, 1, 0,//0
            -1f, 1, 0,//1
    };

    private float[] color = {1.5f, 1.5f, 1.5f, 1};




    // The order we like to connect them.
    private short[] indices = {0, 1, 2, 1,3};

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;
    private double[][] mat_b2g = new double[][]{
            {1,0,0},
            {0,1,0},
            {0,0,1}
    };

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public Line(float[] vertices) {
        this.vertices = vertices;
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();

    }

    public void setMatirx_b2g(double[][] mat_b2g){
        this.mat_b2g = mat_b2g;
    }

    /**
     * This function draws our square on screen.
     *
     * @param gl
     */
    public void draw(GL10 gl) {
        double[][] vertexArray = MatrixUtils.floatArrayToMatrix(vertices);
//        vertexArray = EularRotate.rotateI2B(vertexArray,yawAngle,pitchAngle,rollAngle);
        vertexArray = MatrixUtils.multiply(mat_b2g,MatrixUtils.T(vertexArray));
        // rollAngle+=0.01;
        float[] newV = MatrixUtils.matrixToFloatArray(MatrixUtils.T(vertexArray));
        vertexBuffer.put(newV);
        vertexBuffer.position(0);

        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        ShortBuffer indexBuffer = ibb.asShortBuffer();
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
//        gl.glLineWidth(5);
        gl.glColor4f(color[0], color[1], color[2], color[3]);


        gl.glDrawElements(GL10.GL_LINE_STRIP, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);

        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        // Disable face culling.
        gl.glDisable(GL10.GL_CULL_FACE);
    }

}
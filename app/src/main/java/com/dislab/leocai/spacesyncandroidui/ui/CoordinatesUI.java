package com.dislab.leocai.spacesyncandroidui.ui;

import com.dislab.leocai.spacesync.utils.MatrixUtils;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by leocai on 17-1-20.
 */
public class CoordinatesUI {

    private Line lineX = new Line(new float[]{
            0,0,0,
            1.5f,0,0,
            1.4f, -0.1f,0,
            1.4f, 0.1f,0
    });
    private double[][] mat = new double[][]{
            {1,0,0},
            {0,1,0},
            {0,0,1}
    };
    private float[] color;


    public void draw(GL10 gl){
        double[][] mat1 = new double[][]{
                {0, 1, 0},
                {1, 0, 0},
                {0, 0, 1}
        };


        lineX.setColor(color);
        lineX.setMatirx_b2g(MatrixUtils.multiply(mat, mat1));
        lineX.draw(gl);
        double[][] mat2 = new double[][]{
                {1, 0, 0},
                {0, 1, 0},
                {0, 0, 1}
        };
        lineX.setMatirx_b2g(MatrixUtils.multiply(mat, mat2));
        lineX.draw(gl);
        double[][] mat3 = new double[][]{
                {0, 0, 1},
                {0, 1, 0},
                {1, 0, 0}

        };
        lineX.setMatirx_b2g(MatrixUtils.multiply(mat, mat3));
        lineX.draw(gl);
    }

    public void setMat(double[][] mat) {
        this.mat = mat;
    }

    public void setColor(float[] color) {
        this.color = color;
    }
}

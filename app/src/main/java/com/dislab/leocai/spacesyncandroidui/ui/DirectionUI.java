package com.dislab.leocai.spacesyncandroidui.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.dislab.leocai.spacesync.utils.VectorUtils;

/**
 * Created by leocai on 17-1-9.
 */
public class DirectionUI extends View {

    private float[] v1 = new float[]{1,0,0};
    private float[] v2 = new float[]{0,1,0};
    private Paint paint1 = new Paint();
    private Paint paint2 = new Paint();
    private double angle;

    public DirectionUI(Context context) {
        super(context);
    }

    public DirectionUI(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DirectionUI(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float centerX = getWidth()/2;
        float centerY = getHeight() /2;
        paint1.setColor(Color.BLACK);
        paint2.setColor(Color.RED);
        paint1.setStrokeWidth(5);
        paint2.setStrokeWidth(5);
        float[] v1_2d = unitVector2D(v1);
        float[] v2_2d = unitVector2D(v2);
//        canvas.drawLine(-v1_2d[0] * centerX + centerX, -v1_2d[1] * centerX + centerY, v1_2d[0] * centerX + centerX, v1_2d[1] * centerX + centerY, paint1);
//        canvas.drawLine(-v2_2d[0] * centerX + centerX, -v2_2d[1] * centerX + centerY, v2_2d[0] * centerX + centerX, v2_2d[1] * centerX + centerY, paint2);
        int size = 80;
        paint1.setTextSize(size);
        canvas.drawText("Ag: " + String.format("%.2f", angle), 200, 100, paint1);
        canvas.drawText(getVectorSrt("Ma: ", v1), 200, 100+size+40, paint1);
        canvas.drawText(getVectorSrt("Fc: ",v2), 200, 100+size*2+80, paint1);
    }

    private String getVectorSrt(String lable, float[] v) {
        String str = lable;
        for (int i = 0; i < 3; i++) {
            str+=String.format("%.2f", v[i])+" ";
        }
        return str;
    }

    private double getAngle(float[] v1, float[] v2) {
        double costheta = (v1[0]*v2[0]+v1[1]*v2[1]+v1[2]*v2[2])/(abs(v1)*abs(v2));
        double angle = Math.acos(costheta)/Math.PI*180;
        if(angle>180) angle = angle - 360;
        if(angle > 90 && angle < 180) angle = 180-angle;
        else if(angle < -90  && angle > - 180) angle = -180 - angle;
        return angle;
    }

    private double abs(float[] v1) {
        return Math.sqrt(Math.pow(v1[0], 2) + Math.pow(v1[1], 2)+ Math.pow(v1[2], 2));
    }


    private float[] unitVector(float[] v1) {
        double len = abs(v1);
        return new float[]{(float) (v1[0]/len), (float) (v1[1]/len), (float) (v1[2]/len)};
    }

    private double abs2D(float[] v1) {
        return Math.sqrt(Math.pow(v1[0], 2) + Math.pow(v1[1], 2));
    }

    private float[] unitVector2D(float[] v1) {
        double len = abs2D(v1);
        return new float[]{(float) (v1[0]/len), (float) (v1[1]/len)};
    }



    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double computAngle() {
        angle = getAngle(v1,v2);
        postInvalidate();
        return angle;
    }

    public void setV1(double[] v1) {
        this.v1 = new float[]{(float) v1[0], (float) v1[1], (float) v1[2]};
        postInvalidate();
    }

    public void setV2(double[] v2) {
        this.v2 = new float[]{(float) v2[0], (float) v2[1], (float) v2[2]};
        postInvalidate();
    }
}

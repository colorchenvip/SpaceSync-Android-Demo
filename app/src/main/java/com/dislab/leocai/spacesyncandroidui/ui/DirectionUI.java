package com.dislab.leocai.spacesyncandroidui.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by leocai on 17-1-9.
 */
public class DirectionUI extends View {

    private float[] v1 = new float[]{1,0};
    private float[] v2 = new float[]{0,1};
    private Paint paint1 = new Paint();
    private Paint paint2 = new Paint();

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
        canvas.drawLine(centerX, centerY, v1[0]*centerX+centerX, v1[1]*centerX+centerY, paint1);
        canvas.drawLine(centerX, centerY, v2[0]*centerX+centerX, v2[1]*centerX+centerY, paint2);
    }

    public void setV1(float[] v1) {
        this.v1 = v1;
        postInvalidate();
    }

    public void setV2(float[] v2) {
        this.v2 = v2;
        postInvalidate();
    }
}

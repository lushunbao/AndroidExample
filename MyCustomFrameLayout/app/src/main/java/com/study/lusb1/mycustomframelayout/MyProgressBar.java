package com.study.lusb1.mycustomframelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by lusb1 on 2017/2/15.
 */

public class MyProgressBar extends View {
    private int radius = 50;
    private float progress = 0;
    private boolean isStateOne = true;
    private int circleWidth = 20;
    private String progressText;
    public MyProgressBar(Context context) {
        this(context,null);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MyProgressBar,defStyleAttr,0);
        int n = a.getIndexCount();
        for(int i=0;i<n;i++){
            int attr = a.getIndex(i);
            switch(attr){
                case R.styleable.MyProgressBar_radius:
                    radius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.MyProgressBar_cicle_width:
                    circleWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        new Thread(){
            @Override
            public void run() {
                while(true){
                    progress++;
                    progressText = (int)(progress/360*100) + "%";
                    if(progress == 360){
                        progress = 0;
                        isStateOne = !isStateOne;
                    }
                    postInvalidate();
                    try{
                        Thread.sleep(10);
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth()/2;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(circleWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(radius/4);
        RectF oval = new RectF(center-radius,center-radius,center+radius,center+radius);

        if(isStateOne){
            paint.setColor(Color.GRAY);
            canvas.drawCircle(center,center,radius,paint);
            paint.setColor(Color.LTGRAY);
            canvas.drawArc(oval,-90,progress,false,paint);
            paint.setStrokeWidth(1);
            paint.setColor(Color.BLACK);
            canvas.drawText(progressText,center,center,paint);
        }
        else{
            paint.setColor(Color.LTGRAY);
            canvas.drawCircle(center,center,radius,paint);
            paint.setColor(Color.GRAY);
            canvas.drawArc(oval,-90,progress,false,paint);
            paint.setStrokeWidth(1);
            paint.setColor(Color.BLACK);
            canvas.drawText(progressText,center,center,paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int targetWidth;
        int targetHeight;
        if(widthMode == MeasureSpec.EXACTLY){
            targetWidth = widthSize;
        }
        else{
            targetWidth = radius*2 + circleWidth;
        }
        if(heightMode == MeasureSpec.EXACTLY){
            targetHeight = heightSize;
        }
        else{
            targetHeight = radius*2 + circleWidth;
        }
        setMeasuredDimension(targetWidth,targetHeight);
    }
}

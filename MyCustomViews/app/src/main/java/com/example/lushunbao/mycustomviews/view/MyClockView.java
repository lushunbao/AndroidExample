package com.example.lushunbao.mycustomviews.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.util.Calendar;

/**
 * Created by lushunbao on 2017/3/27.
 */

public class MyClockView extends View {
    private float mWidth;
    private float mHeight;
    private WindowManager wm;
    //表盘半径
    private static final float RADIUS = 600f;
    //时针长度
    private static final float HOUR_LENGTH = 600f;
    //分针长度
    private static final float MINUTE_LENGTH = 600f;
    //秒针长度
    private static final float SECOND_LENGTH = 600f;
    //时分秒指针终点点坐标
    private Point[] endPoints = new Point[3];
    private boolean isRunning = true;
    public MyClockView(Context context) {
        this(context,null);
    }

    public MyClockView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public MyClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
//        Point p = new Point();
//        wm.getDefaultDisplay().getSize(p);
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        mWidth = displayMetrics.widthPixels;
        mHeight = displayMetrics.heightPixels;
        new Thread(new TickRunnable()).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw the clock

        //draw the circle
        Paint paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setStrokeWidth(5);
        canvas.drawCircle(mWidth/2,mHeight/2,RADIUS,paintCircle);
        paintCircle.setStrokeWidth(10);
        canvas.drawPoint(mWidth/2,mHeight/2,paintCircle);
        //draw the degree
        //四分之一
        Paint paintQuarter = new Paint();
        //整点
        Paint paintInter = new Paint();
        //半点
        Paint paintNonInter = new Paint();
        paintQuarter.setAntiAlias(true);
        paintInter.setAntiAlias(true);
        paintNonInter.setAntiAlias(true);
        for(int i=0;i<24;i++){
            paintQuarter.setStrokeWidth(3);
            paintInter.setStrokeWidth(2);
            paintNonInter.setStrokeWidth(1);
            if(i%6 == 0){
                canvas.drawLine(mWidth/2,mHeight/2-RADIUS,mWidth/2,mHeight/2-RADIUS+30,paintQuarter);
                paintQuarter.setTextSize(40);
                canvas.drawText(i/2+"",mWidth/2-paintQuarter.measureText(i/2+"")/2,mHeight/2-RADIUS+70,paintQuarter);
            }
            else if(i%2 == 0){
                canvas.drawLine(mWidth/2,mHeight/2-RADIUS,mWidth/2,mHeight/2-RADIUS+20,paintInter);
                paintInter.setTextSize(30);
                canvas.drawText(i/2+"",mWidth/2-paintInter.measureText(i/2+"")/2,mHeight/2-RADIUS+50,paintInter);
            }
            else if(i%2 == 1){
                canvas.drawLine(mWidth/2,mHeight/2-RADIUS,mWidth/2,mHeight/2-RADIUS+10,paintNonInter);
            }
            canvas.rotate(15,mWidth/2,mHeight/2);
        }
        canvas.save();

        //绘制时分秒针
        Paint paintHour = new Paint();
        Paint paintMin = new Paint();
        Paint painSec = new Paint();
        paintHour.setAntiAlias(true);
        paintHour.setStrokeWidth(12);
        paintMin.setAntiAlias(true);
        paintMin.setStrokeWidth(8);
        painSec.setAntiAlias(true);
        painSec.setStrokeWidth(4);
        canvas.drawLine(mWidth/2,mHeight/2,endPoints[0].x,endPoints[0].y,paintHour);
        canvas.drawLine(mWidth/2,mHeight/2,endPoints[1].x,endPoints[1].y,paintMin);
        canvas.drawLine(mWidth/2,mHeight/2,endPoints[2].x,endPoints[2].y,painSec);
    }

    @Override
    protected void onDetachedFromWindow() {
        isRunning = false;
        super.onDetachedFromWindow();
    }

    private class TickRunnable implements Runnable{

        @Override
        public void run() {
            while(isRunning){
                calEndPoints();
                postInvalidate();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //获取时分秒指针对应的角度
    private float[] getDegrees(){
        int degreePerMin = 360/60;
        int degreePerHour = 360/12;
        float[] res  = new float[3];
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        float secondDegree = second*degreePerMin;
        float minuteDegree = minute*degreePerMin + secondDegree/60;
        float hourDegree = hour*degreePerHour + (minuteDegree/12);
        res[0] = hourDegree;
        res[1] = minuteDegree;
        res[2] = secondDegree;
//        Log.d("lusb1",secondDegree+"\t"+minuteDegree+"\t"+hourDegree);
//        Log.d("lusb1",second+"\t"+minute+"\t"+hour);
        return res;
    }
    //获取时分秒指针终点坐标
    private void calEndPoints(){
        float[] degrees = getDegrees();
        Point hourEndPoint = new Point();
        Point minEndPoint = new Point();
        Point secEndPoint = new Point();
        hourEndPoint.set((int)(mWidth/2+HOUR_LENGTH*Math.sin(degrees[0]/180* Math.PI)),(int)(mHeight/2-HOUR_LENGTH*Math.cos(degrees[0]/180* Math.PI)));
        minEndPoint.set((int)(mWidth/2+MINUTE_LENGTH*Math.sin(degrees[1]/180* Math.PI)),(int)(mHeight/2-MINUTE_LENGTH*Math.cos(degrees[1]/180* Math.PI)));
        secEndPoint.set((int)(mWidth/2+SECOND_LENGTH*Math.sin(degrees[2]/180* Math.PI)),(int)(mHeight/2-SECOND_LENGTH*Math.cos(degrees[2]/180* Math.PI)));
//        Log.d("lusb1",degrees[2]+"\t"+Math.sin(degrees[2]/180* Math.PI)+"\t"+Math.cos(degrees[2]/180* Math.PI));
//        hourEndPoint.set((int)(mWidth/2+HOUR_LENGTH),(int)(mHeight/2-HOUR_LENGTH));
//        minEndPoint.set((int)(mWidth/2+MINUTE_LENGTH),(int)(mHeight/2-MINUTE_LENGTH));
//        secEndPoint.set((int)(mWidth/2+SECOND_LENGTH),(int)(mHeight/2-SECOND_LENGTH));
        endPoints[0] = hourEndPoint;
        endPoints[1] = minEndPoint;
        endPoints[2] = secEndPoint;
    }

}

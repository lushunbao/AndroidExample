package com.study.lusb1.mysinablog.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;


import com.study.lusb1.mysinablog.R;
import com.study.lusb1.mysinablog.util.MyLog;

import java.lang.ref.WeakReference;

/**
 * Created by lusb1 on 2017/4/10.
 */

public class CircleImageView extends ImageView {

    private boolean isDebug = false;
    public static final String TAG = "MySinaBlog.CircleImageView";

    private Paint mPaint;
    //先绘制的是DST，使头像为原型就先加载头像的图片
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private Bitmap mMaskBitmap;

    private WeakReference<Bitmap> mWeakBitmap;

    /**
     * 图片的类型，圆形or圆角
    */
    private int type;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    /**
     * 圆角大小的默认值
     */
    private static final int BORDER_RADIUS_DEFAULT = 10;
    /**
     * 圆角的大小
     */
    private int mBorderRadius;


    public CircleImageView(Context context) {
        this(context,null);
        MyLog.d(isDebug,TAG,"constructor one");
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context,attrs);
        MyLog.d(isDebug,TAG,"constructor two");
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.CircleImageView);
        mBorderRadius = ta.getDimensionPixelSize(R.styleable.CircleImageView_borderRadius,
                (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,BORDER_RADIUS_DEFAULT,
                        getResources().getDisplayMetrics()));

        type = ta.getInt(R.styleable.CircleImageView_type,TYPE_CIRCLE);

        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        MyLog.d(isDebug,TAG,"onDraw");

        //在缓存中取出bitmap
        Bitmap bitmap = mWeakBitmap == null ? null : mWeakBitmap.get();

        if(bitmap == null || bitmap.isRecycled()){
            //拿到Drawable
            Drawable drawable = getDrawable();
            //获取drawable的宽高
            int dWidth = drawable.getIntrinsicWidth();
            int dHeight = drawable.getIntrinsicHeight();
            MyLog.d(isDebug,TAG,"dWidth:"+dWidth+"\t"+"dHeight:"+dHeight);
            MyLog.d(isDebug,TAG,"width:"+getWidth()+"\t"+"height:"+getHeight());

            if(drawable != null){
                //创建bitmap
                bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
                float scale = 1.0f;
                //创建画布
                Canvas drawCanvas = new Canvas(bitmap);
                //按照bitmap的宽高，以及view的宽高，计算缩放比例；因为设置的src宽高比例可能和imageview的宽高比例不同，这里我们不希望图片失真
                if(type == TYPE_ROUND){
                    // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值
                    scale = Math.max(getWidth()*1.0f/dWidth,getHeight()*1.0f/dHeight);
                }
                else{
                    scale = getWidth()*1.0f/Math.min(dWidth,dHeight);
                    MyLog.d(isDebug,TAG,"scale:"+scale);
                }

                //根据缩放比例，设置bounds，相当于缩放图片了
                drawable.setBounds(0,0,(int)(scale*dWidth),(int)(scale*dHeight));

                drawable.draw(drawCanvas);

                if (mMaskBitmap == null || mMaskBitmap.isRecycled())
                {
                    mMaskBitmap = getBitmap();
                }
                // Draw Bitmap.
                mPaint.reset();
                mPaint.setFilterBitmap(false);
                mPaint.setXfermode(mXfermode);
                //绘制形状
                drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
                mPaint.setXfermode(null);
                //将准备好的bitmap绘制出来
                canvas.drawBitmap(bitmap, 0, 0, null);
                //bitmap缓存起来，避免每次调用onDraw，分配内存
                mWeakBitmap = new WeakReference<Bitmap>(bitmap);
            }
        }

        //如果bitmap还存在，则直接绘制即可
        else if (bitmap != null)
        {
            MyLog.d(isDebug,TAG,"bitmap is not null");
            mPaint.setXfermode(null);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, mPaint);
            return;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        MyLog.d(isDebug,TAG,"onMeasure");
        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if(type == TYPE_CIRCLE){
            int width = Math.min(getMeasuredWidth(),getMeasuredHeight());
            setMeasuredDimension(width,width);
        }
    }

    @Override
    public void invalidate() {
        MyLog.d(isDebug,TAG,"invalidate");
        mWeakBitmap = null;
        if (mMaskBitmap != null)
        {
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        super.invalidate();
    }

    /**
     * 绘制形状
     * @return
     */
    public Bitmap getBitmap()
    {
        MyLog.d(isDebug,TAG,"getBitmap");
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        if (type == TYPE_ROUND)
        {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                    mBorderRadius, mBorderRadius, paint);
        } else
        {
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2,
                    paint);
        }

        return bitmap;
    }
}

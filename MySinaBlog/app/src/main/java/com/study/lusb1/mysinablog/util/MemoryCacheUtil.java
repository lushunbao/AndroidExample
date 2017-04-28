package com.study.lusb1.mysinablog.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * Created by lusb1 on 2017/3/15.
 * 内存缓存工具
 */

public class MemoryCacheUtil {

    private boolean isDebug = false;
    public static final String TAG = "MySinaBlog.MemoryCacheUtil";

    private LruCache<String,Bitmap> lruCache;
    private Context context;

    public MemoryCacheUtil(Context context){
        this.context = context;
        //获取ActivityManager
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        //获取应用的内存信息Mb
        int memoryClass = am.getMemoryClass();
        int memorySizeInByte = memoryClass * 1024 * 1024 / 8;
        lruCache = new LruCache<String,Bitmap>(memorySizeInByte){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 保存bitmap到内存缓存
     * @param imgUrl
     * @param imgBitmap
     */
    public void putBitmap(String imgUrl,Bitmap imgBitmap){
        MyLog.d(isDebug,TAG,"putBitmap to the LruCache");
        lruCache.put(imgUrl,imgBitmap);
    }

    /**
     * 从LruCache中读取bitmap
     * @param imgUrl
     * @return
     */
    public Bitmap getBitmap(String imgUrl){
        MyLog.d(isDebug,TAG,"getBitmap from LruCache");
        return lruCache.get(imgUrl);
    }
}

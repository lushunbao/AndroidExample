package com.study.lusb1.mysinablog.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lusb1 on 2017/3/15.
 * get bitmap from the given imgUrl
 */

public class NetCacheUtil {

    public static final int FAIL = 1;
    public static final int SUCCESS = 2;

    private Handler handler;
    private DiskCacheUtil diskCacheUtil;
    private MemoryCacheUtil memoryCacheUtil;
    private ExecutorService service;

    public NetCacheUtil(Handler handler,DiskCacheUtil diskCacheUtil,MemoryCacheUtil memoryCacheUtil){
        this.handler = handler;
        this.diskCacheUtil = diskCacheUtil;
        this.memoryCacheUtil = memoryCacheUtil;
        service = Executors.newFixedThreadPool(10);
    }

    /**
     * thread to do in the thread pool
     */
    private class MyRunnable implements Runnable{
        private String imgUrl;
        private int position;

        private MyRunnable(String imgUrl,int position){
            this.imgUrl = imgUrl;
            this.position = position;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(imgUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = conn.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Message msg = handler.obtainMessage();
                    msg.what = SUCCESS;
                    msg.obj = bitmap;
                    msg.arg1 = position;
                    handler.sendMessage(msg);
                    memoryCacheUtil.putBitmap(imgUrl,bitmap);
                    diskCacheUtil.putBitmap(imgUrl,bitmap);
                }
                conn.disconnect();
            } catch (Exception e) {
                Message msg = handler.obtainMessage();
                msg.what = FAIL;
                handler.sendMessage(msg);
                e.printStackTrace();
            }
        }
    }

    /** get bitmap from the network by httpurlconnection
     * @param imgUrl
     * @param position
     */
    public void getBitmap(String imgUrl,int position){
        service.execute(new MyRunnable(imgUrl,position));
    }
}

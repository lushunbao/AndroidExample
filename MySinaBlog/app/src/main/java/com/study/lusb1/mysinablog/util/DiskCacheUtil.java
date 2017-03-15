package com.study.lusb1.mysinablog.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by lusb1 on 2017/3/15.
 * 硬盘缓存工具
 */

public class DiskCacheUtil {

    private Context context;
    private MemoryCacheUtil memoryCacheUtil;
    private static final String FILE_PATH_SUFFIX = "/cache";


    public DiskCacheUtil(MemoryCacheUtil memoryCacheUtil){
        this.memoryCacheUtil = memoryCacheUtil;
    }

    /** put bitmaps in the diskcache
     * @param imgUrl
     * @param bitmap
     */
    public void putBitmap(String imgUrl,Bitmap bitmap){
        String fileName = MD5.md5(imgUrl);
        String filePath = getFilePath();
        File file = new File(filePath,fileName);
        try{
            File parentFile = file.getParentFile();
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /** get bitmap from diskcache
     * @param imgUrl
     * @return
     */
    public Bitmap getBitmap(String imgUrl){
        String fileName = MD5.md5(imgUrl);
        String filePath = getFilePath();
        File file = new File(filePath,fileName);
        if(file.exists()){
            try{
                FileInputStream fis = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                fis.close();
                memoryCacheUtil.putBitmap(imgUrl,bitmap);
                return bitmap;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /** get the store path of file
     * @return
     */
    public String getFilePath(){
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return (context.getExternalCacheDir() + FILE_PATH_SUFFIX);
        }
        else{
            return (context.getCacheDir() + FILE_PATH_SUFFIX);
        }
    }

}

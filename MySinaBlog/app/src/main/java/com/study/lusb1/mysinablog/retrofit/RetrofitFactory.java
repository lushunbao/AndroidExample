package com.study.lusb1.mysinablog.retrofit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.study.lusb1.mysinablog.beans.Constants;
import com.study.lusb1.mysinablog.beans.MyUser;
import com.study.lusb1.mysinablog.retrofit.model.UserHeadApi;
import com.study.lusb1.mysinablog.retrofit.model.UserInfoApi;
import com.study.lusb1.mysinablog.retrofit.model.UserTimelineApi;
import com.study.lusb1.mysinablog.util.MyLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by lusb1 on 2017/4/28.
 */

public class RetrofitFactory {

    private boolean isDebug = true;
    public static final String TAG = "MySinaBlog.RetrofitFactory";

    private static Retrofit retrofit;
    private Context context;
    private static RetrofitFactory retrofitFactory = null;

    public static RetrofitFactory getInstance(Context context){
        if(retrofitFactory == null){
            synchronized (RetrofitFactory.class){
                if(retrofitFactory == null){
                    retrofitFactory = new RetrofitFactory(context);
                }
            }
        }
        return retrofitFactory;
    }

    private RetrofitFactory(Context context){
        this.context = context;
    }

    //retrofit 获取用户信息
    public MyUser readUserInfo(String access_token, String uid){
        MyLog.d(isDebug,TAG,"readUserInfo");
        MyUser res = null;
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.WEIBO_URL_BASE)
                .build();
        UserInfoApi userInfoApi = retrofit.create(UserInfoApi.class);
        Call<ResponseBody> userFromRetrofitCall = userInfoApi.readUserInfo(access_token,uid);
        try{
            Response<ResponseBody> response = userFromRetrofitCall.execute();
            //sync request
            String sourceFromHttp = response.body().string();
            MyLog.d(isDebug,TAG,"user info request result:"+sourceFromHttp);

            //decode the string to json and get useful information
            //json解析
            JSONObject jsonObject = new JSONObject(sourceFromHttp);
            //用户名
            String userName = jsonObject.getString("screen_name");
            //获取头像
            String user_head = jsonObject.getString("avatar_large");
            BitmapDrawable user_head_drawable = readUserHead(user_head);
            res = new MyUser(uid,userName,access_token,null,"1",user_head_drawable);
        }
        catch(Exception e){
            MyLog.d(isDebug,TAG,"read user info sync request failed");
            e.printStackTrace();
        }
        return res;
    }

    //retrofit获取用户头像信息
    public BitmapDrawable readUserHead(String head_url){
        BitmapDrawable res = null;
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.WEIBO_URL_BASE)
                .build();
        UserHeadApi userInfoApi = retrofit.create(UserHeadApi.class);
        Call<ResponseBody> userHeadCall = userInfoApi.readUserHead(head_url);
        try{
            Response<ResponseBody> response = userHeadCall.execute();
            Bitmap user_head_image = BitmapFactory.decodeStream(response.body().byteStream());
            res = new BitmapDrawable(context.getResources(),user_head_image);
        }
        catch(Exception e){
            MyLog.d(isDebug,TAG,"read user head sync request failed");
            e.printStackTrace();
        }
        return res;
    }

    //retrofit获取用户自己的timeline
    public ArrayList<String> readUserTimeline(String access_token,String uid){
        ArrayList<String> res = new ArrayList<>();

        retrofit = new Retrofit.Builder().baseUrl(Constants.WEIBO_URL_BASE)
                .build();
        UserTimelineApi userTimelineApi = retrofit.create(UserTimelineApi.class);
        Call<ResponseBody> userTimelineCall = userTimelineApi.readUserTimeline(access_token,uid);
        try{
            Response<ResponseBody> reponse = userTimelineCall.execute();
            String sourceFromHttp = reponse.body().string();
            //decode the source to json
            JSONObject statuses = new JSONObject(sourceFromHttp);
            JSONArray statusArray = statuses.getJSONArray("statuses");
            for(int i=0;i<statusArray.length();i++){
                JSONObject status = statusArray.getJSONObject(i);
                String msg = status.getString("text");
                MyLog.d(isDebug,TAG,msg);
                res.add(msg);
            }
        }
        catch(Exception e){
            MyLog.d(isDebug,TAG,"read user timeline sync request failed");
            e.printStackTrace();
        }
        return res;
    }

}

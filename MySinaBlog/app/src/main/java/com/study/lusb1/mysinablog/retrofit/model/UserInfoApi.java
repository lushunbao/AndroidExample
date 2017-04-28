package com.study.lusb1.mysinablog.retrofit.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by lusb1 on 2017/4/28.
 */

public interface UserInfoApi {
    @GET("users/show.json")
    Call<ResponseBody> readUserInfo(@Query("access_token") String access_token, @Query("uid") String uid);
}

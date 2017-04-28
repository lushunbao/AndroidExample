package com.study.lusb1.mysinablog.retrofit.model;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lusb1 on 2017/4/28.
 */

public interface UserTimelineApi {
    @GET("statuses/user_timeline.json")
    Call<ResponseBody> readUserTimeline(@Query("access_token") String access_token, @Query("uid") String uid);
}

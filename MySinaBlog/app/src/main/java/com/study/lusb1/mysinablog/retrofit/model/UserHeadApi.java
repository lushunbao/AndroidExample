package com.study.lusb1.mysinablog.retrofit.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by lusb1 on 2017/4/28.
 */

public interface UserHeadApi {
    @GET()
    Call<ResponseBody> readUserHead(@Url String url);
}

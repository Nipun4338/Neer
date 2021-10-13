package com.example.nogor.Notification;

import com.example.nogor.BuildConfig;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApiService {

    @Headers({
            "Authorization: key="+BuildConfig.FCM_SERVER_KEY ,
            "Content-Type: application/json"
    })
    //@FormUrlEncoded
    @POST("fcm/send")
    //Call<ResultMain> sendNotification(@Body Result to);
    Call<Result> sendNotification(@Body Result to);
}


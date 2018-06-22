package com.sharvari.weatherreporter.network;

import android.content.Context;
import android.text.TextUtils;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sharvari on 20-Jun-18.
 */

public class ApiClient {

    private static String URL = "http://192.168.0.209:8081";

    private static Retrofit retrofit = null;
    private static int REQ_TIMEOUT = 60;
    private static OkHttpClient okHttpClient;

    public static Retrofit getClient(Context context){
        if(okHttpClient == null){
            initOkHttp(context);
        }

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .connectTimeout(REQ_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQ_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder = original.newBuilder()
                        .addHeader("Accept","application/json")
                        .addHeader("Content-Type","application/json");
               /* if(!TextUtils.isEmpty(PrefUtils.getApiKey(context))){
                    builder.addHeader("Authorization",PrefUtils.getApiKey(context));
                }*/
                Request request = builder.build();
                return chain.proceed(request);
            }
        });
        okHttpClient = httpClient.build();
    }
}

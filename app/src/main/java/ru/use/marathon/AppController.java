package ru.use.marathon;

import android.app.Application;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilyas on 09-Jun-18.
 */

public class AppController extends Application{

    private static AppController mInstance;
    private Retrofit retrofit;
    private API api;

    public static synchronized Application getInstance() {
        return mInstance;
    }

    public API getApi () { return api;}

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://cordi.space/exam/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        api = retrofit.create(API.class);
    }
}

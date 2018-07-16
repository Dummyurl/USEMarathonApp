package ru.use.marathon;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
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
    private static API api;

    public static synchronized Application getInstance() {
        return mInstance;
    }

    public static API getApi () { return api;}

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Fabric.with(this, new Crashlytics());

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

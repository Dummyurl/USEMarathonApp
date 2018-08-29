package ru.use.marathon;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.IOException;

import io.fabric.sdk.android.Fabric;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.use.marathon.utils.InternetConnectionListener;
import ru.use.marathon.utils.NetworkConnectionInterceptor;

/**
 * Created by ilyas on 09-Jun-18.
 */

public class AppController extends Application{

    public static final int DISK_CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    private static AppController mInstance;
    private Retrofit retrofit;
    private static API api;

    public static synchronized Application getInstance() {
        return mInstance;
    }

    InternetConnectionListener mInternetConnectionListener;

    public static API getApi () { return api;}

    public static boolean startTimer;
    public static int time;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //Fabric.with(this, new Crashlytics());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(getCache())
                .addInterceptor(interceptor)
                .build();


        // AppController.class
        retrofit = new Retrofit.Builder()
                .baseUrl("https://cordi.space/exam/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build(); // тут выстраивается основная часть ссылки


        api = retrofit.create(API.class);
    }

    public static String startTimer(){
        startTimer = true;
        final String[] time_formatted = new String[1];
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int minutes = (time % 3600) / 60;
                int secs = time % 60;

                time_formatted[0] = String.format("%02d:%02d", minutes, secs);

                if (startTimer) {
                    time++;
                }

                handler.postDelayed(this, 1000);
            }
        });

        return time_formatted[0];
    }

    public static void stopTimer(){
        startTimer = false;
    }

    public Cache getCache() {
        File cacheDir = new File(getCacheDir(), "cache");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        return cache;
    }

    public void setInternetConnectionListener(InternetConnectionListener listener) {
        mInternetConnectionListener = listener;
    }

    public void removeInternetConnectionListener() {
        mInternetConnectionListener = null;
    }



}


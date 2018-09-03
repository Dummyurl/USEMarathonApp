package ru.use.marathon;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKAccessTokenTracker;
import com.vk.sdk.VKSdk;

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
import ru.use.marathon.activities.auth.VkAuthActivity;
import ru.use.marathon.fragments.LoginStudentFragment;
import ru.use.marathon.utils.InternetConnectionListener;
import ru.use.marathon.utils.NetworkConnectionInterceptor;

/**
 * Created by ilyas on 09-Jun-18.
 */

public class AppController extends MultiDexApplication{

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


    VKAccessTokenTracker vkAccessTokenTracker = new VKAccessTokenTracker() {
        @Override
        public void onVKAccessTokenChanged(VKAccessToken oldToken, VKAccessToken newToken) {
            if (newToken == null) {
                Intent intent = new Intent(ru.use.marathon.AppController.this,VkAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        vkAccessTokenTracker.startTracking();
        VKSdk.initialize(this);

        mInstance = this;
        Fabric.with(this, new Crashlytics());

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
                .build();


        api = retrofit.create(API.class);
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


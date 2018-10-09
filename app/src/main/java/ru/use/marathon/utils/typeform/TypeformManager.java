package ru.use.marathon.utils.typeform;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ilyas on 01-Oct-18.
 */

public class TypeformManager  {

    public static final String ACCESS_TOKEN = "Bearer Gf4z49zYcwRRCUWMMDgNhfwz5pJpsaHaqjwFy4p1jw3R";
    private static TypeformManager apiManager;
    private static TypeformAPI service;

    private TypeformManager() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", ACCESS_TOKEN)
                        .build();
                return chain.proceed(request);
            }
        }).addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.typeform.com/workspaces/5wVUCw/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TypeformAPI.class);

    }

    public static TypeformManager getInstance() {
        if (apiManager == null) {
            apiManager = new TypeformManager();
        }
        return apiManager;
    }

    public static Call<JsonObject> getForms(){
        return service.getForms();
    }

//    public void getForms(Callback<DictionaryInfo> callback) {
//
//        Call<DictionaryInfo> dictionaryEntries = service.getDictionaryEntries(ACCESS_TOKEN);
//        dictionaryEntries.enqueue(callback);
//
//    }

}

package com.smartpantrymanager.activities.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static SupaBaseAPI api;

    private APIClient() {
        // Use getApi()
    }

    public static synchronized SupaBaseAPI getApi() {
        if (api == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                    // Interceptor: adds the key to every request in one place
                    .addInterceptor(chain -> {
                        Request withKey = chain.request().newBuilder()
                                .header("apikey", SupaBaseConfig.PUBLISHABLE_KEY)
                                .header("Accept", "application/json")
                                .build();
                        return chain.proceed(withKey);
                    })
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build();
            // serializeNulls() so that clearing an expiry date sends "expiry_date": null
            Gson gson = new GsonBuilder().serializeNulls().create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(SupaBaseConfig.PROJECT_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            api = retrofit.create(SupaBaseAPI.class);
        }
        return api;

    }
}
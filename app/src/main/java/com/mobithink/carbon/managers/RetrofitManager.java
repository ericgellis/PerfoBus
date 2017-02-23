package com.mobithink.carbon.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mobithink.carbon.BuildConfig;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jpaput on 06/02/2017.
 */

public class RetrofitManager {

    private static final String BASE_URL = "https://mobithink.herokuapp.com/";

    private static final String BASE_URL_AXEL = "http://172.20.70.72:8080/";

    private static final String SECURITY_TOKEN = "Basic ZXJpYzptb2JpMjAxN3RoaW5r";

    static Retrofit mRetrofitInstance;

    public static Retrofit build()
    {
        if(mRetrofitInstance == null){

            String baseURL;
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
                builder.addInterceptor(interceptor);
                //Change this if you want to try an other server in integration or developement need
                baseURL = BASE_URL;
            } else {
                baseURL = BASE_URL;
            }

            builder.connectTimeout(60, TimeUnit.SECONDS);
            builder.readTimeout(60, TimeUnit.SECONDS);
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {

                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Accept", "application/json")
                            .header("Authorization", SECURITY_TOKEN)
                            .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            OkHttpClient client = builder.build();

            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    return new Date(json.getAsJsonPrimitive().getAsLong());
                }
            }).create();

            mRetrofitInstance = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return mRetrofitInstance;
    }
}

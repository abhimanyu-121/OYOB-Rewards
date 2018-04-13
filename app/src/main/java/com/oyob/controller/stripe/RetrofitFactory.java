package com.oyob.controller.stripe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Factory instance to keep our Retrofit instance.
 */
public class RetrofitFactory {

    // Put your Base URL here. Unless you customized it, the URL will be something like
    // https://hidden-beach-12345.herokuapp.com/
    public static final String BASE_URL = "https://www.myrewards.com.au/newapp/";
    private static Retrofit mInstance = null;

    public static final String BASE_URL_STRIPE = "https://api.stripe.com/v1/";

    public static Gson gson;
    public static Retrofit getInstance(String baseURL) {
       // if (mInstance == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // Set your desired log level. Use Level.BODY for debugging errors.
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

        gson = new GsonBuilder()
                    .setLenient()
                    .create();

            // Adding Rx so the calls can be Observable, and adding a Gson converter with
            // leniency to make parsing the results simple.
            mInstance = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(baseURL)
                    .client(httpClient.build())
                    .build();
       // }
        return mInstance;
    }
}

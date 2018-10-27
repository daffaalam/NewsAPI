package com.daffaalam.newsapi.network;

import android.support.annotation.NonNull;

import com.daffaalam.newsapi.model.RootNews;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class ClientAPI {

    private static final String BASE_URL = "https://newsapi.org/";

    @NonNull
    public static Retrofit retrofit() {
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }

    public interface EndPoint {

        @GET("v2/top-headlines")
        Call<RootNews> newsAPI_topheadlines(
                @Query("q") String q,
                @Query("country") String country,
                @Query("page") Integer page,
                @Header("X-Api-Key") String key);

        @GET("v2/everything")
        Call<RootNews> newsAPI_everything(
                @Query("q") String q,
                @Query("language") String lang,
                @Query("page") Integer page,
                @Query("apiKey") String key);

    }
}

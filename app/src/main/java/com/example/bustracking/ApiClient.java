package com.example.bustracking; // Use your package name

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.openai.com/v1/"; // API base URL
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) // Set the base URL for the API
                    .addConverterFactory(GsonConverterFactory.create()) // Use Gson to convert JSON responses
                    .build();
        }
        return retrofit;
    }
}

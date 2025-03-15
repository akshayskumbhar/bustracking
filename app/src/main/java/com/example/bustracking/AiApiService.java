package com.example.bustracking; // Use your package name

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AiApiService {
    // Define the API request. This example assumes you're using OpenAI's GPT model.
    @Headers("Authorization: Bearer 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") // Replace with your API key
    @POST("chat/completions") // This is the OpenAI API endpoint for text completion
    Call<AiResponse> getAiResponse(@Body AiRequest aiRequest); // Sends the AI request
}

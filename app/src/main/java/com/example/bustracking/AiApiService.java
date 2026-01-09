package com.example.bustracking; // Use your package name

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AiApiService {
    // Define the API request. This example assumes you're using OpenAI's GPT model.
    @Headers("Authorization: Bearer sk-xxxx-qx-xxxxxxxxxxxxxxxxxxxjMFlQ2PRINTdHAnSk3CMkz27rxxxxxxxxxxxp2-zWQcdL-6kzEtnUae9HUxxxxxxxxxxxxx") // Replace with your API key
    @POST("chat/completions") // This is the OpenAI API endpoint for text completion
    Call<AiResponse> getAiResponse(@Body AiRequest aiRequest); // Sends the AI request
}

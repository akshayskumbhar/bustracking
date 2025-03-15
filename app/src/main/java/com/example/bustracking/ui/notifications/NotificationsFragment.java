package com.example.bustracking.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bustracking.AiApiService;
import com.example.bustracking.AiRequest;
import com.example.bustracking.AiResponse;
import com.example.bustracking.ApiClient;
import com.example.bustracking.ChatAdapter;
import com.example.bustracking.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private List<String> messages;
    private ChatAdapter chatAdapter; // Custom adapter for RecyclerView

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);

        // Initialize message list and RecyclerView
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(messages); // Custom adapter
        binding.recyclerGchat.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerGchat.setAdapter(chatAdapter);

        // Set up send button click listener
        binding.buttonGchatSend.setOnClickListener(v -> {
            String message = binding.editGchatMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                sendMessage(message);
                binding.editGchatMessage.setText(""); // Clear input field
            } else {
                Toast.makeText(getContext(), "Enter a message", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }

    private void sendMessage(String message) {
        // Add the user's message to the list
        messages.add("You: " + message);
        chatAdapter.notifyItemInserted(messages.size() - 1);

        // Create the AI request
        AiRequest.Message userMessage = new AiRequest.Message("user", message);
        List<AiRequest.Message> messageList = new ArrayList<>();
        messageList.add(userMessage);

        AiRequest aiRequest = new AiRequest("gpt-4o", true, messageList);

        // Get the API service
        AiApiService apiService = ApiClient.getRetrofitInstance().create(AiApiService.class);

        // Make the API call asynchronously
        apiService.getAiResponse(aiRequest).enqueue(new Callback<AiResponse>() {
            @Override
            public void onResponse(Call<AiResponse> call, Response<AiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String aiResponse = response.body().getChoices().get(0).getMessage().getContent();
                    messages.add("Bus Chat AI: " + aiResponse);
                    chatAdapter.notifyItemInserted(messages.size() - 1);
                    binding.recyclerGchat.scrollToPosition(messages.size() - 1);
                } else {
                    Toast.makeText(getContext(), "Failed to get response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.example.bustracking.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.bustracking.ChatAdapter;
import com.example.bustracking.R;
import com.example.bustracking.databinding.FragmentNotificationsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private TextToSpeech textToSpeech;
    private static final int SPEECH_REQUEST_CODE = 100;
    private ImageButton micButton, sendButton;
    private EditText messageInput;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<String> messages = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        micButton = binding.micButton;
        sendButton = binding.sendButton;
        messageInput = binding.editGchatMessage;
        chatRecyclerView = binding.recyclerGchat;

        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setAdapter(chatAdapter);

        LottieAnimationView voiceAnimation = binding.voiceAnimation;

        micButton.setOnClickListener(v -> {
            if (voiceAnimation.isAnimating()) {
                voiceAnimation.pauseAnimation();  // Use pauseAnimation instead of cancelAnimation
            } else {
                voiceAnimation.playAnimation();
            }
        });


        // Initialize Text-to-Speech
        textToSpeech = new TextToSpeech(getContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US);
            }
        });

        // Set up microphone button for voice input
        micButton.setOnClickListener(v -> startVoiceInput());

        // Set up send button to send message
        sendButton.setOnClickListener(v -> {
            String userMessage = messageInput.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                sendMessage(userMessage);
                messageInput.setText("");
            }
        });

        return root;
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Voice Input Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                messageInput.setText(result.get(0)); // Set recognized text to input field
                sendMessage(result.get(0)); // Send recognized message
            }
        }
    }

    private void sendMessage(String userMessage) {
        messages.add("You: " + userMessage);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);

        // Simulating AI response (replace this with actual API call)
        String aiResponse = "Hello! How can I assist you today?";
        messages.add("Bus Chat AI: " + aiResponse);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);

        // Speak the AI response aloud
        textToSpeech.speak(aiResponse, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        binding = null;
    }
}
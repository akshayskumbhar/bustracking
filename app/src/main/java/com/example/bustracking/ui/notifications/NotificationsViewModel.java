package com.example.bustracking.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<List<String>> chatMessages;

    public NotificationsViewModel() {
        chatMessages = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<String>> getChatMessages() {
        return chatMessages;
    }

    public void addMessage(String message) {
        List<String> currentMessages = chatMessages.getValue();
        if (currentMessages != null) {
            currentMessages.add(message);
            chatMessages.setValue(currentMessages);
        }
    }
}

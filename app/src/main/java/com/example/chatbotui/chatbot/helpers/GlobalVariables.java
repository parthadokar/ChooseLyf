package com.example.chatbotui.chatbot.helpers;

import com.example.chatbotui.chatbot.db.Chat;
import com.example.chatbotui.chatbot.db.User;

import java.util.Optional;


public class GlobalVariables {
    private static final GlobalVariables INSTANCE = new GlobalVariables();

    private User currentUser;
    private Chat currentChat;

    private GlobalVariables() {
    }

    public static GlobalVariables getInstance() {
        return INSTANCE;
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(this.currentUser);
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Optional<Chat> getCurrentChat() {
        return Optional.ofNullable(this.currentChat);
    }

    public void setCurrentChat(Chat currentChat) {
        this.currentChat = currentChat;
    }
}
package com.example.chatbotui.chatbot.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.example.chatbotui.R;
import com.example.chatbotui.chatbot.db.ChatSQLiteDBHelper;


public class SharedPreferencesHelper {

    public static void loadUser(@NonNull Context context) {
        int id = context.getSharedPreferences(context.getResources().getString(R.string.user_settings), Context.MODE_PRIVATE).
                getInt(context.getResources().getString(R.string.user_id), 1000);
        GlobalVariables.getInstance().setCurrentUser(ChatSQLiteDBHelper.getUserByID(context, id));
    }

    public static void saveUserId(@NonNull Context context, int userId) {
        SharedPreferences.Editor editor = context.getSharedPreferences(context.getResources().getString(R.string.user_settings),
                Context.MODE_PRIVATE).edit();
        editor.putInt(context.getResources().getString(R.string.user_id), userId);
        editor.apply();
    }

}

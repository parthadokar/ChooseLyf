package com.example.chatbotui.chatbot;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.chatbotui.chatbot.db.Chat;
import com.example.chatbotui.chatbot.db.ChatSQLiteDBHelper;
import com.example.chatbotui.chatbot.helpers.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class MakeParseRequest {

    private final InfermedicaApiClass infermedicaApiClass;
    private final ApiRequestQueue apiRequestQueue;
    private final String url;
    private final Response.ErrorListener errorListener;
    private final Context context;
    private final String userMessage;
    private final RequestUtil.ChatRequestListener chatRequestListener;
    private final Response.Listener<JSONObject> successListener;
    private final ChatActivity chatActivity;


    public MakeParseRequest(ChatActivity chatActivity, String text) {
        this.chatActivity = chatActivity;
        this.chatRequestListener = chatActivity;
        this.context = chatActivity;
        this.infermedicaApiClass = InfermedicaApiClass.getInstance(context);
        this.apiRequestQueue = ApiRequestQueue.getInstance(context);
        this.url = this.infermedicaApiClass.getUrl() + "/v3" + "/parse";
        this.userMessage = text;

        this.errorListener = error -> {
            chatActivity.onRequestFailure();
            System.out.println(error.networkResponse.toString());
        };

        this.successListener = response -> {
            try {
                JSONArray jsonArrayFromResponse = response.getJSONArray("mentions");
                JSONArray jsonArrayToRequest = new JSONArray();
                for (int i = 0; i < jsonArrayFromResponse.length(); i++) {
                    JSONObject jsonObject = jsonArrayFromResponse.getJSONObject(i);
                    if (jsonObject.getString("type").equals("symptom")) {
                        JSONObject clearJsonObject = new JSONObject();
                        clearJsonObject.put("id", jsonObject.getString("id"));
                        clearJsonObject.put("choice_id", jsonObject.getString("choice_id"));
                        clearJsonObject.put("name", jsonObject.getString("name"));
                        clearJsonObject.put("source", "initial");
                        jsonArrayToRequest.put(clearJsonObject);
                    }
                }
                RequestUtil.getInstance().addToEvidenceArray(jsonArrayToRequest);
                Optional<Chat> chat = GlobalVariables.getInstance().getCurrentChat();
                if (!chat.isPresent()) {
                    Chat currentChat = Chat.builder(ChatSQLiteDBHelper.getNextChatIdAvailable(context), GlobalVariables.getInstance().getCurrentUser().get().getId())
                            .conditionArray(null)
                            .date(new Date())
                            .lastRequest(RequestUtil.getInstance().getStringFromEvidenceArray())
                            .build();
                    GlobalVariables.getInstance().setCurrentChat(currentChat);
                    ChatSQLiteDBHelper.saveChatDataToDB(context, currentChat);
                } else {
                    chat.get().setLastRequest(RequestUtil.getInstance().getStringFromEvidenceArray());
                    ChatSQLiteDBHelper.saveChatDataToDB(MakeParseRequest.this.context, chat.get());
                }
                new MakeDiagnoseRequest(chatActivity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };

        GlobalVariables globalVariables = GlobalVariables.getInstance();
        if (!globalVariables.getCurrentUser().isPresent()) {
            System.out.println("User not found");
        }

        Map<String, String> headers = RequestUtil.getDefaultHeaders(context);
        if (!Locale.getDefault().getLanguage().equals("pl")) {
            JSONObject jsonObject = new JSONObject();
            try {
                RequestUtil.addUserDataToJsonObject(jsonObject);
                jsonObject.put("text", text);
                RequestUtil.addAgeToJsonObject(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            chatRequestListener.addUserMessage(text);
            this.apiRequestQueue.addToRequestQueue(new JSONObjectRequestWithHeaders(Request.Method.POST, this.url, headers, jsonObject, this.successListener, this.errorListener));
        }
    }
}

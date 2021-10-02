package com.example.chatbotui.chatbot;

import androidx.annotation.Nullable;

import org.json.JSONException;

public class MakeDiagnoseRequest extends DiagnoseRequest {

    public MakeDiagnoseRequest(ChatActivity chatActivity, @Nullable String userAnswer) {
        super(chatActivity, userAnswer);

        String url = InfermedicaApiClass.getInstance(chatActivity).getUrl() + "/v3" + "/diagnosis";
        try {
            this.addAgeToRequestBody(RequestUtil::addAgeToJsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ApiRequestQueue.getInstance(chatActivity).addToRequestQueue(new JSONObjectRequestWithHeaders(1, url, this.getHeaders(), this.getRequestBody(), this.getSuccessListener(), this.getErrorListener()));
    }

    public MakeDiagnoseRequest(ChatActivity chatActivity) {
        this(chatActivity, null);
    }
}

package com.example.chatbotui.chatbot;


import android.content.Context;
import android.os.Build;

import com.example.chatbotui.chatbot.db.User;
import com.example.chatbotui.chatbot.helpers.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RequestUtil {

    private final static RequestUtil INSTANCE = new RequestUtil();
    private JSONArray evidenceArray;
    private JSONArray conditionsArray;

    private RequestUtil() {
        evidenceArray = new JSONArray();
        conditionsArray = new JSONArray();
    }

    public static RequestUtil getInstance() {
        return INSTANCE;
    }

    public static void addUserDataToJsonObject(JSONObject jsonObject) throws JSONException {
        if (!GlobalVariables.getInstance().getCurrentUser().isPresent()) {
            throw new RuntimeException();
        }
        User user = GlobalVariables.getInstance().getCurrentUser().get();
        try {
            jsonObject.put("sex", user.getFullGenderName());
        } catch (JSONException e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                throw new JSONException(e);
            }
        }
    }

    public static void addAgeToJsonObject(JSONObject jsonObject) throws JSONException {
        if (!GlobalVariables.getInstance().getCurrentUser().isPresent()) {
            throw new RuntimeException();
        }
        User user = GlobalVariables.getInstance().getCurrentUser().get();
        JSONObject ageJson = new JSONObject();
        ageJson.put("value", user.getAge());
        jsonObject.put("age", ageJson);
    }

    public static Map<String, String> getDefaultHeaders(Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("App-Id", InfermedicaApiClass.getInstance(context).getId());
        headers.put("App-Key", InfermedicaApiClass.getInstance(context).getKey());
        headers.put("Model", "infermedica-en");
        headers.put("Content-Type", "application/json");
        return headers;
    }


    public static void addLanguageToInfermedicaHeaders(Map<String, String> map) {
        map.put("Model", "infermedica-" + Locale.getDefault().getLanguage());
    }


    public JSONArray getConditionsArray() {
        return conditionsArray;
    }

    public void setConditionsArray(JSONArray conditionsArray) {
        this.conditionsArray = conditionsArray;
    }

    public void resetConditionsArray() {
        conditionsArray = new JSONArray();
    }

    public JSONArray getEvidenceArray() {
        return evidenceArray;
    }

    public void addToEvidenceArray(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            evidenceArray.put(jsonArray.getJSONObject(i));
        }
    }

    public void setEvidenceArrayFromString(String string) throws JSONException {
        this.evidenceArray = new JSONArray(string);
    }

    public String getStringFromEvidenceArray() {
        return evidenceArray.toString();
    }

    public void addToEvidenceArray(JSONObject jsonObject) {
        evidenceArray.put(jsonObject);
    }

    public void resetEvidenceArray() {
        evidenceArray = new JSONArray();
    }

    public interface ChatRequestListener {
        void onDoctorMessage(String msg);

        void addUserMessage(String msg);

        void hideMessageBox();

        void onDoctorQuestionReceived(String id, JSONArray msg, String name);

        boolean finishDiagnose();

        void onRequestFailure();
    }
}

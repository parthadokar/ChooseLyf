package com.example.chatbotui.hospital;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;


public class GeometryController {

    public static boolean loading;
    public static ArrayList<NearbyHospitalsDetail> detailArrayList = new ArrayList();

    public static void manipulateData(StringBuffer buffer){

        loading = true;
        try {
            detailArrayList.clear();

            JSONObject jsonpObject = new JSONObject(buffer.toString());
            JSONArray array = jsonpObject.getJSONArray("results");

            for(int i=0; i<array.length(); i++){
                try {
                    JSONObject jsonObject = array.getJSONObject(i);
                    NearbyHospitalsDetail hospitalsDetail = new NearbyHospitalsDetail();

                    jsonObject.getString("name");
                    hospitalsDetail.setHospitalName(jsonObject.getString("name"));

                    try {
                        hospitalsDetail.setRating(String.valueOf(jsonObject.getDouble("rating")));
                    }catch (Exception e){
                        hospitalsDetail.setRating("Not Available");
                    }

                    try {
                        if (jsonObject.getJSONObject("opening_hours").getBoolean("open_now"))  hospitalsDetail.setOpeningHours("Opened");
                        else hospitalsDetail.setOpeningHours("closed");
                    } catch (Exception e) {
                        hospitalsDetail.setOpeningHours("Not Available");
                    }

                    hospitalsDetail.setAddress(jsonObject.getString("vicinity"));
                    hospitalsDetail.setGeometry(new double[]{jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat"),
                            jsonObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng")});

                    detailArrayList.add(hospitalsDetail);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        loading = false;
        Log.d("Array Loaded with size ", "Size of "+detailArrayList.size());
    }
}

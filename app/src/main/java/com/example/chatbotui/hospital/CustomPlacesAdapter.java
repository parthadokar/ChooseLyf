package com.example.chatbotui.hospital;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chatbotui.R;

import java.util.ArrayList;


public class CustomPlacesAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<String> placeName;
    private final ArrayList<String> ratingText;
    private final ArrayList<String> openNowText;

    public CustomPlacesAdapter(Context context, ArrayList<String> placeName, ArrayList<String> ratingText, ArrayList<String> openNowText) {
        this.context = context;
        this.placeName = placeName;
        this.ratingText = ratingText;
        this.openNowText = openNowText;
    }

    @Override
    public int getCount() {
        return placeName.size();
    }

    @Override
    public Object getItem(int i) {
        return placeName.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 3232;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view == null) view = View.inflate(context, R.layout.health_centers_result_view, null);

        TextView placeTextView = view.findViewById(R.id.placeNameTextView);
        TextView ratingTextView = view.findViewById(R.id.ratingTextView);
        TextView openNowTextView = view.findViewById(R.id.openingTime);

        placeTextView.setText(placeName.get(i));
        ratingTextView.setText(ratingText.get(i));
        openNowTextView.setText("Open now: " + openNowText.get(i));

        return view;
    }
}

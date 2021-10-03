package com.example.chatbotui.hospital.GMap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.chatbotui.R;
import com.example.chatbotui.hospital.GeometryController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);

        findViewById(R.id.zoomInButton).setOnClickListener(view -> mMap.animateCamera(CameraUpdateFactory.zoomIn()));

        findViewById(R.id.zoomOutButton).setOnClickListener(view -> mMap.animateCamera(CameraUpdateFactory.zoomOut()));

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        findViewById(R.id.refreshButton).setOnClickListener(view -> mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude))));

        final CheckBox checkBox = findViewById(R.id.satelliteViewCheckBox);

        checkBox.setOnClickListener(view -> {
            if (checkBox.isChecked()) mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            else mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);

        this.runOnUiThread(this::setUpCameraAndMarkers);
    }


    void setUpCameraAndMarkers() {

        LatLng latLng;
        for (int i = 0; i < GeometryController.detailArrayList.size(); i++) {
            latLng = new LatLng(GeometryController.detailArrayList.get(i).getGeometry()[0], GeometryController.detailArrayList.get(i).getGeometry()[1]);
            mMap.addMarker(new MarkerOptions().position(latLng).title(GeometryController.detailArrayList.get(i).getHospitalName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }
}

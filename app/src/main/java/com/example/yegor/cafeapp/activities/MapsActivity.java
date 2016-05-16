package com.example.yegor.cafeapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.example.yegor.cafeapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private Marker currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapView = (MapView) findViewById(R.id.nav_map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_maps;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            mMap.setMyLocationEnabled(true);
        }

        for (LatLng latLng : geLatLangList())
            mMap.addMarker(new MarkerOptions().position(latLng).title("Some title"));

    }

    private List<LatLng> geLatLangList() {

        List<LatLng> latLngs = new ArrayList<>(50);

        Random lat = new Random();
        Random lang = new Random();

        for (int i = 0; i < 50; i++) {
            latLngs.add(new LatLng(lat.nextInt(180) - 90, lang.nextInt(360) - 180));
        }

        return latLngs;
    }

}

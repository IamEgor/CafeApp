package com.example.yegor.cafeapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.yegor.cafeapp.App;
import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.Utils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private MapView mapView;

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
        } else {
            mMap.setMyLocationEnabled(true);
        }

        (new LoadMarkers(4)).execute();

    }

    class LoadMarkers extends AsyncTask<Void, MarkerOptions, CameraUpdate> {

        private Geocoder geocoder;
        private List<Address> fromLocation;

        private int padding;

        public LoadMarkers(int paddingDp) {
            this.padding = Utils.dp2pixel(paddingDp);
        }

        @Override
        protected void onPreExecute() {
            geocoder = new Geocoder(App.getContext(), Locale.getDefault());
        }

        @Override
        protected CameraUpdate doInBackground(Void... params) {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            LatLng latLng;
            String title;

            Random random = new Random();

            for (int i = 0; i < 25; i++) {
                try {
                    latLng = new LatLng(random.nextInt(30) + 36, random.nextInt(60) + 16);
                    title = getAddress(latLng);
                    builder.include(latLng);
                    publishProgress(new MarkerOptions().position(latLng).title(title));
                } catch (IOException e) {
                    Log.e("LoadMarkers doInBackg", e.getMessage());
                    throw new RuntimeException();
                }

            }

            return CameraUpdateFactory.newLatLngBounds(builder.build(), padding);
        }

        @Override
        protected void onProgressUpdate(MarkerOptions... values) {
            mMap.addMarker(values[0]);
        }

        @Override
        protected void onPostExecute(CameraUpdate cameraUpdate) {
            mMap.animateCamera(cameraUpdate);
        }

        private String getAddress(LatLng latLng) throws IOException {
            fromLocation = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (fromLocation.size() > 0)
                return fromLocation.get(0).getAddressLine(0);
            else
                return getString(R.string.no_address);
        }

    }

}

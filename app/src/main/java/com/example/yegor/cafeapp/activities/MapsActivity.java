package com.example.yegor.cafeapp.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.yegor.cafeapp.App;
import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.Utils;
import com.example.yegor.cafeapp.exceptions.NoConnectionException;
import com.example.yegor.cafeapp.models.adapter.ContentWrapper;
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

    private View errorView;
    private TextView errorMessage;

    private GoogleMap mMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorView = findViewById(R.id.error_view);
        errorMessage = (TextView) findViewById(R.id.error_message);
        mapView = (MapView) findViewById(R.id.nav_map);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);

        findViewById(R.id.retry_btn).setOnClickListener(v -> (new LoadMarkers(4)).execute());

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

    private void setStatus(Status status) {

        switch (status) {
            case OK:
                mapView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                break;
            case FAILED:
                errorView.setVisibility(View.VISIBLE);
                mapView.setVisibility(View.GONE);
                break;
        }

    }

    class LoadMarkers extends AsyncTask<Void, MarkerOptions, ContentWrapper<CameraUpdate>> {

        private Geocoder geocoder;
        private List<Address> fromLocation;

        private int padding;

        public LoadMarkers(int paddingDp) {
            this.padding = Utils.dp2pixel(paddingDp);
        }

        @Override
        protected ContentWrapper<CameraUpdate> doInBackground(Void... params) {

            if (!Utils.hasConnection())
                return new ContentWrapper<>(new NoConnectionException());


            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            geocoder = new Geocoder(App.getContext(), Locale.getDefault());

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
                    return new ContentWrapper<>(e);
                }

            }

            return new ContentWrapper<>(CameraUpdateFactory.newLatLngBounds(builder.build(), padding));
        }

        @Override
        protected void onProgressUpdate(MarkerOptions... values) {
            if (mapView.getVisibility() == View.GONE)
                setStatus(BaseActivity.Status.OK);
            mMap.addMarker(values[0]);
        }

        @Override
        protected void onPostExecute(ContentWrapper<CameraUpdate> content) {
            if (content.getException() == null && content.getContent() != null) {
                mMap.animateCamera(content.getContent());
                setStatus(BaseActivity.Status.OK);
            } else if (content.getException() != null
                    && content.getException() instanceof NoConnectionException) {
                errorMessage.setText(content.getException().getMessage());
                setStatus(BaseActivity.Status.FAILED);
            } else {
                //throw new RuntimeException(content.getException().getMessage());
                errorMessage.setText("Smth went wrong. Please retry.");
                setStatus(BaseActivity.Status.FAILED);
            }

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

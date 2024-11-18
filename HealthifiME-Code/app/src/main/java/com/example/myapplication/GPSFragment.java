package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

public class GPSFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private PolylineOptions polylineOptions;
    private Polyline polyline;
    private List<LatLng> latLngList = new ArrayList<>();
    private Marker startMarker;
    private LatLng latLng;
    private boolean isTracking = false;

    private TextView distanceDisplay;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    private LocationListener locationListener;
    private LocationManager locationManager;

    public GPSFragment() {
    }

    public static GPSFragment newInstance() {
        return new GPSFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_g_p_s, container, false);

        distanceDisplay = view.findViewById(R.id.distance);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);


        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);



        Button startTrackingButton = view.findViewById(R.id.startTrackingButton);
        startTrackingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onStartTrackingClicked();
            }
        });

        return view;
    }

    private void onStartTrackingClicked() {
        Button startTrackingButton = getView().findViewById(R.id.startTrackingButton);

        if (!isTracking) {
            startTrackingButton.setText("Stop Tracking");
        } else {
            startTrackingButton.setText("Start Tracking");
        }

        isTracking = !isTracking;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        polylineOptions = new PolylineOptions().width(5).color(Color.BLUE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());

                if (startMarker == null) {
                    startMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Start"));
                }

                latLngList.add(latLng);

                if (polyline == null) {
                    polyline = mMap.addPolyline(polylineOptions.addAll(latLngList));
                } else {
                    polyline.setPoints(latLngList);
                }

                if (latLngList.size() > 10) {
                    double totalDistance = SphericalUtil.computeLength(latLngList);
                    totalDistance = totalDistance / 10;
                    updateUI(totalDistance);
                }

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
            }
        };

        locationManager = (LocationManager) requireActivity().getSystemService(requireActivity().LOCATION_SERVICE);

        try {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void updateUI(double totalDistance) {
        String distance = String.format("Distance: %.2f meters", totalDistance);
        distanceDisplay.setText(distance);
    }
}

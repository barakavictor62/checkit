package com.example.baraka62.checkit;


import android.*;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * Created by baraka62 on 10/30/2017.
 */

public class FragmentMap extends Fragment implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private static final String TAG = FragmentMap.class.getSimpleName();
    GoogleApiClient googleApiClient;
    FusedLocationProviderClient locationClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    GoogleMap myMap;
    LatLng latLng;
    Location last_location;
    Marker marker;
    private final  static int LOCATION_REQUEST_CODE = 44;
    private final  static int LOCATION_REQUEST_INTERVAL = 10000;
    private final  static int FASTEST_LOCATION_INTERVAL = LOCATION_REQUEST_INTERVAL/2;

    private  boolean AccessLocationAllowed;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.map_layout, container, false);
        Bundle my_data = getArguments();
        ((Home)getActivity()).setActionBarTitle(my_data.getString("storename"));
        SupportMapFragment supportMapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        locationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        thisLocationCallback();
        thisLocationRequest();

        return myView;
    }
    private void thisLocationRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(LOCATION_REQUEST_CODE);
        locationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }
    private  void thisLocationCallback(){
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                last_location= locationResult.getLastLocation();
                latLng = new LatLng(last_location.getLatitude(), last_location.getLongitude());
                myMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                if(marker!= null){
                    marker.remove();
                }
                marker = myMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Marker"));
            }
        };

    }

    private void getDeviceLocation(){
        try {
            if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_REQUEST_CODE);
            }
            else {
                locationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }

        }
        catch (Exception e){
            Log.e("Exception: %s" ,e.getMessage());

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST_CODE:
            {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                }
                else {

                }
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        googleApiClient.disconnect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (googleApiClient.isConnected()){
            getDeviceLocation();


        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

    }
}

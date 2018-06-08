package com.example.baraka62.checkit;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResponse;
import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.app.Activity.RESULT_OK;

/**
 * Created by baraka62 on 11/17/2017.
 */

public class PlacePickerClass extends Fragment {
    private int PLACE_PICKER_REQUEST_CODE = 1;

    Bundle place_bundle;

    GeoDataClient geoDataClient;

    TextView place_name, place_address,place_id,place_website_url, place_phone_number;
    ImageView place__img;
    Bitmap place_img;
    Button get_location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle my_data = getArguments();

         ((Home)getActivity()).setActionBarTitle(my_data.getString("storename"));
        return inflater.inflate(R.layout.place_info,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        place_name = (TextView) getActivity().findViewById(R.id.place_name);
        place_id = (TextView) getActivity().findViewById(R.id.place_id);
        place_website_url = (TextView) getActivity().findViewById(R.id.place_website_url);
        place_phone_number = (TextView) getActivity().findViewById(R.id.place_phone_number);
        place__img = (ImageView) getActivity().findViewById(R.id.place_img);
        place_address = (TextView) getActivity().findViewById(R.id.place_address);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        geoDataClient = Places.getGeoDataClient(getActivity(), null);
        getLocation();

    }

    public void getLocation(){
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            int PLACE_PICKER_LOCATION_REQUEST_CODE = 234;
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PLACE_PICKER_LOCATION_REQUEST_CODE);
        }
        else {

            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(intentBuilder.build(getActivity()), PLACE_PICKER_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PLACE_PICKER_REQUEST_CODE){
            if(resultCode == RESULT_OK){
                final Place place = PlacePicker.getPlace(getActivity(),data);

                place_name.setText(place.getName().toString());
                place_address.setText(place.getAddress().toString());
                place_id.setText(place.getId());
                place_website_url.setText(String.valueOf(place.getWebsiteUri()));
                place_phone_number.setText(place.getPhoneNumber().toString());
                geoDataClient.getPlacePhotos(String.valueOf(place.getId()));

               Task<PlacePhotoMetadataResponse>placePhotoMetadataResponse = geoDataClient.getPlacePhotos(place.getId())
                       .addOnCompleteListener(new OnCompleteListener<PlacePhotoMetadataResponse>() {
                   @Override
                   public void onComplete(@NonNull Task<PlacePhotoMetadataResponse> task) {
                           PlacePhotoMetadataResponse photos = task.getResult();
                           PlacePhotoMetadataBuffer buffer = photos.getPhotoMetadata();
                           if(buffer.get(0)!=null){
                           PlacePhotoMetadata placePhotoMetadata = buffer.get(0);
                           CharSequence attributions = placePhotoMetadata.getAttributions();

                           Task<PlacePhotoResponse> placePhotoResponseTask = geoDataClient.getPhoto(placePhotoMetadata)
                                   .addOnCompleteListener(new OnCompleteListener<PlacePhotoResponse>() {
                                       @Override
                                       public void onComplete(@NonNull Task<PlacePhotoResponse> task) {
                                           if(task.getResult()!= null) {
                                               PlacePhotoResponse photoResponse = task.getResult();
                                               place_img = photoResponse.getBitmap();
                                               if(place_img!=null){
                                                   place__img.setImageBitmap(place_img);
                                               }
                                           }
                                       }
                                   });
                       }
                       else{

                       }

                   }
               });
            }
        }
    }

}

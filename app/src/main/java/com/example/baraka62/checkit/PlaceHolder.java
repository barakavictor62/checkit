package com.example.baraka62.checkit;

import android.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by baraka62 on 11/23/2017.
 */

public class PlaceHolder extends AppCompatActivity {

    TextView place_name, place_address,place_id,place_website_url, place_phone_number;
    ImageView place_img;
    Button get_location;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.place_info);

        Bundle place_data = getIntent().getExtras();


    }

    private void getLocation() {
    }


}

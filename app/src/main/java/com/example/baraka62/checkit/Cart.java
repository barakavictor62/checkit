package com.example.baraka62.checkit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by baraka62 on 12/2/2017.
 */

public class Cart extends ListFragment implements AdapterView.OnItemClickListener {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference my_fire_db;
    ProductTemplate product;
    Bundle mydata;
    Bundle my_data;
    final ArrayList<ProductTemplate> data= new ArrayList<ProductTemplate>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        my_data = getArguments();
        ((Home)getActivity()).setActionBarTitle(my_data.getString("storename"));
        return inflater.inflate(R.layout.community, container,false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        my_data = getArguments();
        mydata = getActivity().getIntent().getExtras();
        if (mydata != null && mydata.getString("name") != null) {
            my_fire_db = firebaseDatabase.getReference().child("Users").child(my_data.getString("name")).child("Cart");
            my_fire_db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        product = child.getValue(ProductTemplate.class);
                        data.add(product);


                    }
                    ArrayAdapter adapter = new CartAdapter(getActivity(), R.layout.cart, data);
                    setListAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}

package com.example.baraka62.checkit;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by baraka62 on 11/3/2017.
 */

public class Community extends ListFragment implements AdapterView.OnItemClickListener {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference my_fire_db;
    ProductTemplate product;
    Bundle mydata;
    Bundle my_data;
    ArrayList<ProductTemplate> data;
    FirebaseAuth auth = FirebaseAuth.getInstance();


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
        data = new ArrayList<ProductTemplate>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        my_data = getArguments();
        mydata = getActivity().getIntent().getExtras();
        if (mydata != null && mydata.getString("name") != null) {
            String cart;
            if(my_data.getString("name")!= null){
                cart = my_data.getString("name");
                if (cart != null) {
                    final ArrayAdapter adapter = new ProductAdapter(getActivity(),my_data.getString("do"), R.layout.list_products, data);
                    my_fire_db = firebaseDatabase.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Cart");
                    my_fire_db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            data.clear();
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                product = child.getValue(ProductTemplate.class);
                                data.add(product);
                               adapter.notifyDataSetChanged();
                            }
                            setListAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }
            else{
                 cart = my_data.getString("storename");
                if (cart != null) {
                    my_fire_db = firebaseDatabase.getReference().child("Stores").child(cart);
                    my_fire_db.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                product = child.getValue(ProductTemplate.class);
                                data.add(product);
                            }
                            ArrayAdapter adapter = new ProductAdapter(getActivity(),my_data.getString("do"), R.layout.list_products, data);
                            setListAdapter(adapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

}
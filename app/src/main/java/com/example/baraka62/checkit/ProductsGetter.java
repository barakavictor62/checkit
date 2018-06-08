package com.example.baraka62.checkit;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by baraka62 on 11/28/2017.
 */

public class ProductsGetter extends IntentService{

    FirebaseDatabase firebaseDatabase;
    DatabaseReference my_fire_db;
    ProductTemplate product;
    final List<ProductTemplate> templates = new ArrayList<ProductTemplate>();

    private final IBinder mBinder = new LocalBinder();

    public ProductsGetter(String name) {
        super(name);
    }
    public class LocalBinder extends Binder {
        ProductsGetter getService() {
            return ProductsGetter.this;
        }
    }
    public void readFireDatabase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        my_fire_db = firebaseDatabase.getReference();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        my_fire_db.child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    product = child.getValue(ProductTemplate.class);
                    templates.add(product);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
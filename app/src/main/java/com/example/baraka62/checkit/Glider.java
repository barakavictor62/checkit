package com.example.baraka62.checkit;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

/**
 * Created by baraka62 on 11/30/2017.
 */
@GlideModule
public class Glider extends AppGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.append(StorageReference.class, InputStream.class, new FirebaseImageLoader.Factory());
    }
}

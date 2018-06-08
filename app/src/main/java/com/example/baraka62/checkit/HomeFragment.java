package com.example.baraka62.checkit;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static android.app.Activity.RESULT_OK;

/**
 * Created by baraka62 on 10/30/2017.
 */

public class HomeFragment extends Fragment {
    TextView email;
    GetUser getUser;
    ImageView my_profile;
    int RESULT_PROFILE_IMG_CODE = 23;
    TextView my_name;
    String profile_pic;
    Bundle usrdata;
    String my_username;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Bundle my_data = getArguments();
        ((Home)getActivity()).setActionBarTitle("Home");
        return inflater.inflate(R.layout.home_layout, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        email = (TextView) getActivity().findViewById(R.id.logged_in_email);
        my_name =(TextView) getActivity().findViewById(R.id.my_name);
        my_profile = (ImageView) getActivity().findViewById(R.id.my_profile);
        usrdata = getActivity().getIntent().getExtras();

        //my_name.setText(usrdata.getString("name"));
        FirebaseDatabase my_profile_database = FirebaseDatabase.getInstance();
        DatabaseReference my_profile_data = my_profile_database.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Profile");
        my_profile_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    User user;
                    user = dataSnapshot.getValue(User.class);
                    if(user!=null) {
                        my_username = user.getUserName();
                        my_name.setText(my_username);
                        if (user.getImgUrl() != null) {
                            Glide.with(getContext()).load(user.getImgUrl()).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(my_profile);
                        }
                    }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //my_name.setText(my_username);
        if (usrdata != null) {
            String myemail = usrdata.getString("email");
            email.setText(myemail);
        }
        my_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfilePic();
            }
        });
    }

    private void updateProfilePic() {
        Intent getImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getImage, RESULT_PROFILE_IMG_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_PROFILE_IMG_CODE){
            if(resultCode == RESULT_OK){
                Uri my_img = data.getData();
                uploadProfile(my_img);
            }
        }
    }

    private void uploadProfile(Uri uri) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        UploadTask uploadTask = storageReference.child("users").child(auth.getCurrentUser().getUid()).putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Upload failed.Please try again", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profile_pic = String.valueOf(taskSnapshot.getDownloadUrl());
                User user = new User();
                user.setName(String.valueOf(my_name.getText()));
                user.setImgUrl(profile_pic);
                FirebaseDatabase profile_db = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = profile_db.getReference().child("Users").child(String.valueOf(auth.getCurrentUser().getUid())).child("Profile");
                databaseReference.child("imgUrl").setValue(profile_pic);
                Toast.makeText(getActivity(), "Profile successfully update", Toast.LENGTH_LONG).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });

    }
    interface GetUser{
        void sendUserName(String the_user_name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            getUser = (GetUser) getActivity();
        }
        catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()
                    + " must implement GetUser");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.getCurrentUser();
    }
}

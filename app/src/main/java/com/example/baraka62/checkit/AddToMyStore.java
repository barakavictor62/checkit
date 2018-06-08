package com.example.baraka62.checkit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * Created by baraka62 on 11/3/2017.
 */

public class AddToMyStore extends Fragment {
    EditText product_name, product_price, product_description;
    TextView username;
    Button save, add_img;
    ImageView product_img;
    Spinner item_category;
    Bitmap bitmap;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    Uri uri;
    String img_url,gotten_username;
    Bundle mydata;
    Bundle my_data;
    FirebaseAuth auth = FirebaseAuth.getInstance();





    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        product_name = (EditText) getActivity().findViewById(R.id.product_name);
        product_description = (EditText) getActivity().findViewById(R.id.product_description);
        product_price = (EditText) getActivity().findViewById(R.id.product_price);
        save = (Button) getActivity().findViewById(R.id.save);
        add_img = (Button) getActivity().findViewById(R.id.add_img);
        product_img = (ImageView) getActivity().findViewById(R.id.product_img);
        item_category = (Spinner) getActivity().findViewById(R.id.item_category);
        username = (TextView) getActivity().findViewById(R.id.my_name);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct();

            }
        });

        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImg();
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.item_category_options,
                android.R.layout.simple_spinner_dropdown_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        item_category.setAdapter(spinnerAdapter);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mydata = getActivity().getIntent().getExtras();



    }

    private void addImg() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE );
    }

    private void saveProduct() {
        if(uri!=null){
            //bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
           // byte[] data = byteArrayOutputStream.toByteArray();
            //Uri file = Uri.fromFile(new File(uri.toString()));
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());

            progressDialog.setTitle("uploading....");
            progressDialog.show();


            UploadTask uploadTask = storageReference.child("images").child(auth.getCurrentUser().getUid()).child("My Store").putFile(uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    img_url = String.valueOf(taskSnapshot.getDownloadUrl());
                    FirebaseDatabase productdb = FirebaseDatabase.getInstance();
                    DatabaseReference myreference = productdb.getReference().child("Stores").child(username.getText().toString());
                    ProductTemplate productTemplate = new ProductTemplate(
                            product_name.getText().toString(),
                            product_description.getText().toString(),
                            product_price.getText().toString(),
                            img_url);

                    myreference.push().setValue(productTemplate);
                    Toast.makeText(getActivity(), product_name.getText().toString(),Toast.LENGTH_LONG).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.setTitle("uploading....");
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    progressDialog.dismiss();
                }
            });
        }
        else {
            Toast.makeText(getActivity(),"An Image is required",Toast.LENGTH_LONG).show();
        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         my_data = getArguments();
        ((Home)getActivity()).setActionBarTitle(my_data.getString("title"));
        return inflater.inflate(R.layout.online_search, container, false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                uri = data.getData();
                bitmap = (Bitmap) bundle.get("data");
                product_img.setImageBitmap(bitmap);
                //productTemplate.setImage(bitmap);
            }
        }
    }
    public void getUsername(String the_user_name){
        gotten_username = the_user_name;
    }
}

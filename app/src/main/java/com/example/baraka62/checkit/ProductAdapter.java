package com.example.baraka62.checkit;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by baraka62 on 11/28/2017.
 */

public class ProductAdapter extends ArrayAdapter<ProductTemplate> {
    Context context;
    int layoutResource;
    ArrayList<ProductTemplate> data;
    Uri img_url;
    String which_to_load;
    FirebaseAuth auth = FirebaseAuth.getInstance();


    public ProductAdapter(@NonNull Context context,String which_to_load, int resource, @NonNull List<ProductTemplate> objects) {
        super(context, resource, objects);
        this.layoutResource = resource;
        this.context = context;
        this.data = (ArrayList) objects;
        this.which_to_load = which_to_load;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProductViewHolder holder = new ProductViewHolder();
        View view = convertView;
        final ProductTemplate productTemplate = getItem(position);

        if(convertView == null) {

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view= inflater.inflate(layoutResource, parent,false);

            //holder.product_image = (ImageView) view.findViewById(R.id.list_product_img);

            holder.product_name = (TextView) view.findViewById(R.id.list_product_name);

            holder.product_description=(TextView) view.findViewById(R.id.list_product_description);

            holder.product_Price = (TextView) view.findViewById(R.id.list_product_price);
            holder.buy = (Button) view.findViewById(R.id.buy);
           // holder.contact_seller = (Button) view.findViewById(R.id.contact_seller);


            holder.product_image = (ImageView) view.findViewById(R.id.list_product_img);

           // convertView.setTag(holder);
            view.setTag(holder);
        }
        else {
            holder=(ProductViewHolder) view.getTag();
        }
        if(which_to_load!=null && which_to_load.equals("load_cart")){
            holder.buy.setText(R.string.remove_from_my_cart);
        }
            holder.product_name.setText(productTemplate.getName());
            holder.product_description.setText(productTemplate.getDescription());
            holder.product_Price.setText(productTemplate.getPrice());
            GlideApp.with(getContext()).load(productTemplate.getImgUrl()).into(holder.product_image);
            holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences myprefs = getContext().getSharedPreferences("com.example.baraka62.checkit.preferences",Context.MODE_PRIVATE);
                String my_name = myprefs.getString("username", "no user");
                if(my_name.equals("no user")){
                    Toast.makeText(getContext(),"error. please try again", Toast.LENGTH_LONG).show();

                }
                else {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference cart = firebaseDatabase.getReference().child("Users").child(auth.getCurrentUser().getUid()).child("Cart").child(productTemplate.getName());
                    if(which_to_load!=null && which_to_load.equals("load_cart")){
                        cart.setValue(null);
                        Toast.makeText(getContext(), productTemplate.getName() + " " + "removed from cart Successfully", Toast.LENGTH_LONG).show();
                    }
                    else {
                        cart.setValue(productTemplate);
                        Toast.makeText(getContext(), productTemplate.getName() + " " + "added to cart", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        //holder.product_image.setImageBitmap(R.drawable.ic_launcher_foreground);

        return  view;
    }

    public static class ProductViewHolder{
        TextView product_name,product_description,product_Price;
        Button buy,contact_seller;
        ImageView product_image;
    }
}

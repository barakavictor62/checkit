package com.example.baraka62.checkit;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by baraka62 on 11/5/2017.
 */

class DrawerAdapter extends ArrayAdapter<String> {
    private TypedArray nav_icons;
    private Activity context;
    private String[] resource;

    public DrawerAdapter(Activity context, String[] resource, TypedArray nav_icons) {
        super(context,R.layout.my_nav_layout, resource);
        this.context = context;
        this.nav_icons = nav_icons;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DrawerViewHolder drawerViewHolder = new DrawerViewHolder();
        String nav_drawer_items = getItem(position);

        View view = convertView;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.my_nav_layout,parent, false);

            drawerViewHolder.email = (TextView) view.findViewById(R.id.logged_in_email);
            drawerViewHolder.separator=(TextView) view.findViewById(R.id.nav_separator);
            drawerViewHolder.textView = (TextView) view.findViewById(R.id.nav_text);
            drawerViewHolder.imageView=(ImageView) view.findViewById(R.id.nav_icons);

            view.setTag(drawerViewHolder);
        }
        else {
            drawerViewHolder=(DrawerViewHolder) view.getTag();
        }
        if (nav_drawer_items != null) {
            if(position==4){
                drawerViewHolder.separator.setVisibility(View.VISIBLE);
                drawerViewHolder.separator.setOnClickListener(null);
                drawerViewHolder.separator.setFocusable(false);
                drawerViewHolder.separator.setLongClickable(false);
            }
            else {
                drawerViewHolder.separator.setVisibility(View.GONE);
            }

            drawerViewHolder.textView.setText(nav_drawer_items);
            drawerViewHolder.imageView.setImageResource(nav_icons.getResourceId(position, -1));
        }

        return view;
    }

    public class DrawerViewHolder{
        TextView email, textView,separator;
        ImageView imageView;
    }
}

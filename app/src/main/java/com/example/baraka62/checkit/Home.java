package com.example.baraka62.checkit;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Constructor;

public class Home extends AppCompatActivity implements HomeFragment.GetUser {




    TextView username, email,result;
    String my_username;
    Bundle storename;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String clicked;
    TypedArray my_nav_icons;

    private String[] myNavItems;
    private DrawerLayout mDrawerLayout;
    private ListView mySideNav;
    private ActionBarDrawerToggle toggleSide;
    FragmentTransaction transaction;

    String me_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Intent productGetter = new Intent(this, ProductsGetter.class);
        //bindService(productGetter);
        me_name = getIntent().getExtras().getString("name");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();



        //......Retrieving resources to populate dramer layout.......................//

        my_nav_icons = getResources().obtainTypedArray(R.array.my_nav_icons);
        myNavItems = getResources().getStringArray(R.array.my_nav_items);
        mySideNav = findViewById(R.id.list);
        View header = getLayoutInflater().inflate(R.layout.list_header, mySideNav, false );
        mySideNav.addHeaderView(header);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        //..............................................................................//

        //...preferences...............................................................//

        SharedPreferences sharedPreferences= this.getSharedPreferences("com.example.baraka62.checkit.preferences",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",me_name);
        editor.apply();

        //.............................................................................//

        toggleSide = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }

        };

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ProductTemplate value = dataSnapshot.getValue(ProductTemplate.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mDrawerLayout.addDrawerListener(toggleSide);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //......Linking resources to list view to form drawer......................................//
        mySideNav.setAdapter(new DrawerAdapter(this,myNavItems, my_nav_icons));
        //........................................................................................//

       // email = (TextView) findViewById(R.id.logged_in_email);
       // result = (TextView) findViewById(R.id.result);


        //Toast.makeText(this,String.valueOf(usrdata.getString("email")),Toast.LENGTH_LONG).show();

        ItemOnNavClicked();
        if(savedInstanceState == null) {
            loadHome();
        }
    }
    //.............initial user home fragment....................................................//

    private void loadHome(){
        transaction = getSupportFragmentManager().beginTransaction();
        Fragment homeFragment = new HomeFragment();
        transaction.replace(R.id.fragment_holder, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    //............................................................................................//

    //.......when an item from the drawer is clicked.............................................//

    public void ItemOnNavClicked(){
        mySideNav.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                clicked = String.valueOf(adapterView.getItemAtPosition(i));
                //Toast.makeText(getApplicationContext(),clicked, Toast.LENGTH_LONG).show();
                selectItem(clicked, i);
            }
        });
    }
    public void selectItem(String clicked, int i){
        transaction = getSupportFragmentManager().beginTransaction();
        storename = new Bundle();
        switch (clicked) {
            case "Home":
                loadHome();
                pullBackNav(i);
                break;
            case "Featured Stores":
                storename.putString("storename", clicked);
                Fragment featured = new Featured();
                transaction.replace(R.id.fragment_holder, featured);
                transaction.addToBackStack(null);
                transaction.commit();
                pullBackNav(i);
                break;
            case "Nearby":
                storename.putString("storename", clicked);
                Fragment map = new FragmentMap();
                map.setArguments(storename);
                transaction.replace(R.id.fragment_holder,map);
                transaction.addToBackStack(null);
                transaction.commit();
                pullBackNav(i);
                break;
            case "Take Picture":
                Fragment camera = new Camera();
                transaction.replace(R.id.fragment_holder, camera);
                transaction.addToBackStack(null);
                transaction.commit();
                pullBackNav(i);
                break;
            case "Add to Store":
                storename.putString("storename", me_name);
                storename.putString("title", clicked);
                Fragment onlineSearch = new AddToMyStore();
                onlineSearch.setArguments(storename);
                transaction.replace(R.id.fragment_holder, onlineSearch);
                transaction.addToBackStack(null);
                transaction.commit();
                sendUserName(my_username);
                pullBackNav(i);
                break;
            case "My Store":
                storename.putString("storename", me_name);
                Fragment community = new Community();
                community.setArguments(storename);
                transaction.replace(R.id.fragment_holder,community);
                transaction.addToBackStack(null);
                transaction.commit();
                //sendUserName(mydata.getString("name"));
                pullBackNav(i);
                break;
            case "Help & Feedback":
                storename.putString("storename", clicked);
                Fragment helpandfeedback = new HelpAndFeedback();
                helpandfeedback.setArguments(storename);
                transaction.replace(R.id.fragment_holder, helpandfeedback);
                transaction.addToBackStack(null);
                transaction.commit();
                pullBackNav(i);
                break;
            case "Places":
                storename.putString("storename", clicked);
                Fragment placespicker = new PlacePickerClass();
                placespicker.setArguments(storename);
                transaction.replace(R.id.fragment_holder, placespicker);
                transaction.addToBackStack(null);
                transaction.commit();
                pullBackNav(i);
                break;
            case "Scanner":
                //storename.putString("storename", clicked);
                Intent community_intent = new Intent(this, Scanner.class);
                startActivityForResult(community_intent, 45);
                pullBackNav(i);
                break;
            case "Encoder":
                storename.putString("storename", clicked);
                Fragment encoder = new Encoder();
                encoder.setArguments(storename);
                transaction.replace(R.id.fragment_holder, encoder);
                transaction.addToBackStack(null);
                transaction.commit();
                pullBackNav(i);
                break;

        }
    }
//.................................................................................................//

    //.....Writing to firebase database............................................................//
    public void loadFireStore(View view){
        Intent intent = new Intent(this, Community.class);
        startActivity(intent);
    }

    //...... After nav item selection.................................................

    public void pullBackNav(int i){
        mySideNav.setItemChecked(i, true);
        mDrawerLayout.closeDrawer(mySideNav);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 45){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    //Menu menu = newMenuInstance(this);
                    //SearchView searchView = (SearchView) menu.findItem(R.id.search);
                    Barcode barcode = data.getParcelableExtra("Barcode");
                   // Toast.makeText(getApplicationContext(),String.valueOf(barcode.displayValue),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEARCH);
                    intent.setClass(this, Search.class);
                    intent.putExtra(SearchManager.QUERY, barcode.displayValue);
                    startActivity(intent);


                }
            }
        }

        else  {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onPostCreate( Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggleSide.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggleSide.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                showSettings();
                return true;
            case R.id.log_out:
                logout();
                return true;
            case R.id.search:
                search();
                return true;
            case R.id.shopping_cart:
                transaction= getSupportFragmentManager().beginTransaction();
                storename = new Bundle();
                storename.putString("storename", me_name);
                storename.putString("name", me_name);
                storename.putString("do","load_cart");
                Fragment community = new Community();
                community.setArguments(storename);
                transaction.replace(R.id.fragment_holder,community);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            default:
                return toggleSide.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        }
    }
    //..............Methods acivated by options menu click events..........................
    private void showSettings(){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, new Settings())
                .addToBackStack(null)
                .commit();
    }
    private void logout(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);


    }

    private void search() {
        Toast.makeText(this, "search clicked", Toast.LENGTH_LONG).show();

    }
    //....................................................................................

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public void sendUserName(String the_user_name) {
        //AddToMyStore onlineSearch = new AddToMyStore();
       // onlineSearch.getUsername(the_user_name);
    }

    public void loadStore(View view) {
        transaction = getSupportFragmentManager().beginTransaction();
        Fragment storeFragment= new Community();
        Button store = (Button) view;
        storename = new Bundle();
        String store_name = store.getText().toString();
        switch(store_name){
            case "Store 1":
                storename.putString("storename", store_name);
                storeFragment.setArguments(storename);
                transaction.replace(R.id.fragment_holder, storeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case "Store 2":
                storename.putString("storename", store_name);
                storeFragment.setArguments(storename);
                transaction.replace(R.id.fragment_holder, storeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case "Store 3":
                storename.putString("storename", store_name);
                storeFragment.setArguments(storename);
                transaction.replace(R.id.fragment_holder, storeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

        }
    }
    public void setActionBarTitle(String my_title){
        getSupportActionBar().setTitle(my_title);
    }

    protected Menu newMenuInstance(Context context) {
        try {
            Class<?> menuBuilderClass = Class.forName("com.android.internal.view.menu.MenuBuilder");

            Constructor<?> constructor = menuBuilderClass.getDeclaredConstructor(Context.class);

            return (Menu) constructor.newInstance(context);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}

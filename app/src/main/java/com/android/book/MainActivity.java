package com.android.book;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.book.fragment.HomeFragment;
import com.android.book.fragment.MessageFragment;
import com.android.book.fragment.SearchFragment;
import com.android.book.fragment.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    BottomNavigationView bottomNavigationView ;
    Fragment fragment;
    FragmentTransaction transaction;
    Toolbar toolbar;
    String userId = "1" ;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this,R.style.Toolbar_LogoName);
        toolbar.setPadding(toolbar.getPaddingLeft(),toolbar.getPaddingTop(),20,toolbar.getPaddingBottom());
        toolbar.setTitle("Home");

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green_700));


        sharedPreferences = getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();
        userId = sharedPreferences.getString("user_id","");

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragment    =   new HomeFragment();
        transaction =   getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);


      changeActionBar("Home");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_icons, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = new Intent(this,AddBook.class);
        startActivity(intent);
        return true ;
    }

    public void changeActionBar(String title) {

        toolbar.setTitle(title);
        if (title.equalsIgnoreCase("Home")){
            toolbar.getMenu().setGroupVisible(0,true);
        }else {
            toolbar.getMenu().setGroupVisible(0,false);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {



        int id = item.getItemId();
       fragment=null;

        if (id == R.id.navigation_home){
            fragment = new HomeFragment();

        }else  if (id == R.id.navigation_search) {
            fragment = new SearchFragment();

        } else  if (id == R.id.navigation_message) {
            fragment = new MessageFragment();

        } else  if (id == R.id.navigation_profile) {
            fragment = new UserFragment();

        } if (fragment != null){
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container , fragment);
            transaction.commit();

        }
        return true;
    }
}
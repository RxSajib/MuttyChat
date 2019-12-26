package com.muttychat.muttychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;

public class LoginActivty extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LoginPagerAdapter loginPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);

        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        tabLayout = findViewById(R.id.LoginTabLayoutID);
        viewPager = findViewById(R.id.ViewPagerID);

        loginPagerAdapter = new LoginPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(loginPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}

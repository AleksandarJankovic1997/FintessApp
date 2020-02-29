package com.example.myapplication.myapplication.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;

import com.example.myapplication.R;
import com.example.myapplication.myapplication.Fragments.GuestFragment;
import com.example.myapplication.myapplication.Fragments.LoginFragment;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.security.MessageDigest;


public class LoginActivity extends FragmentActivity {
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private LoginFragment loginFragment;
    private GuestFragment guestFragment;


    //private  int color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        loginFragment=new LoginFragment();
        fragmentTransaction.add(R.id.login_frame_layout,loginFragment,"login");
        fragmentTransaction.commit();
        //color=1;
    }

    @Override
    protected void onResume() {
        if(fragmentManager==null){
            fragmentManager=getSupportFragmentManager();
        }
        if(fragmentTransaction==null){
            fragmentTransaction=fragmentManager.beginTransaction();
        }
        if(loginFragment==null){
            loginFragment=new LoginFragment();
            fragmentTransaction.add(R.id.login_frame_layout,loginFragment);
        }
        super.onResume();
    }
    public void replaceFragment(){
        fragmentTransaction=fragmentManager.beginTransaction();
        if(guestFragment==null){
            guestFragment=new GuestFragment();
        }
        fragmentTransaction.replace(R.id.login_frame_layout,guestFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void fragmentBack(){
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_frame_layout,loginFragment);
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.login_frame_layout);
        fragment.onActivityResult(requestCode,resultCode,data);
    }
}

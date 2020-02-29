package com.example.myapplication.myapplication.Activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.myapplication.Fragments.MobileActivityFragment;
import com.example.myapplication.myapplication.Fragments.StatisticFragment;
import com.example.myapplication.myapplication.Fragments.TabletActivityFragment;

import com.example.myapplication.myapplication.Fragments.TabletMapFragment;
import com.example.myapplication.myapplication.Fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;


public class ApplicationMainActivity extends FragmentActivity {
    private TabLayout tabLayout;
    private MobileActivityFragment mobileActivityFragment;
    private UserProfileFragment userProfileFragment;
    private StatisticFragment statisticFragment;
    private TabletActivityFragment tabletActivityFragment;
    private TabletMapFragment tabletMapFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int mode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getSupportFragmentManager();
        setContentView(R.layout.activity_application_main);
        isTablet=isTablet();
        iniciateFragments();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i=tab.getPosition();
                if(isTablet){
                    if(mode!=i){
                        if(i==0){
                            profileback();
                            mode=0;
                        }else if(i==1){
                            replaceAloneTeam(false);
                            mode=1;
                        }else if(i==2){
                            replaceAloneTeam(true);
                            mode=2;
                        }
                    }

                }else {

                    if (mode != i) {
                        if (i == 0) {
                            profileback();
                            Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_LONG).show();
                            mode = 0;
                        } else if (i == 1) {
                            replaceAloneTeam(false);
                            if (mode == 2) {
                                mobileActivityFragment.setAlone();
                            }
                            mode = 1;
                        } else if (i == 2) {
                            replaceAloneTeam(true);
                            mobileActivityFragment.setTeam();
                            mode = 2;

                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    public void profileback(){
        fragmentTransaction=fragmentManager.beginTransaction();
        if (isTablet){
            if(userProfileFragment==null){
                userProfileFragment=new UserProfileFragment();
            }
            if(statisticFragment==null){
                statisticFragment=new StatisticFragment();
            }

            fragmentTransaction.replace(R.id.tablet_framelayout1,userProfileFragment);
            fragmentTransaction.replace(R.id.table_framelayout2,statisticFragment);
            fragmentTransaction.commit();
        }
        else{
            if(userProfileFragment==null){
                userProfileFragment=new UserProfileFragment();
            }
            fragmentTransaction.replace(R.id.mobile_framelayout1,userProfileFragment);
            fragmentTransaction.commit();

        }
    }
    public void iniciateFragments(){
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        userProfileFragment=new UserProfileFragment();
        if(isTablet){
            statisticFragment=new StatisticFragment();
            fragmentTransaction.add(R.id.tablet_framelayout1,userProfileFragment);
            fragmentTransaction.add(R.id.table_framelayout2,statisticFragment);
            fragmentTransaction.commit();
        }
        else{
            fragmentTransaction.add(R.id.mobile_framelayout1,userProfileFragment);
            fragmentTransaction.commit();
        }
    }
    public void replaceMobileStatistics(){
        statisticFragment=new StatisticFragment();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mobile_framelayout1,statisticFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void replaceAloneTeam(boolean bool){
        fragmentTransaction=fragmentManager.beginTransaction();
        if(isTablet){
            if(tabletActivityFragment==null) {
                tabletActivityFragment = new TabletActivityFragment(bool);
            }

            if(tabletMapFragment==null) {
                tabletMapFragment = new TabletMapFragment();
            }
            fragmentTransaction.replace(R.id.tablet_framelayout1,tabletActivityFragment);
            fragmentTransaction.replace(R.id.table_framelayout2,tabletMapFragment);
            fragmentTransaction.commit();

        }
        else{
            if(mobileActivityFragment==null){
               mobileActivityFragment=new MobileActivityFragment(bool);
            }
            fragmentTransaction.replace(R.id.mobile_framelayout1,mobileActivityFragment);
            fragmentTransaction.commit();

        }
    }

    private boolean isTablet;

    public boolean isTablet(){
        if(this.findViewById(R.id.tablet_framelayout1)!=null){
            tabLayout=findViewById(R.id.tablet_tab_layout);
            return true;
        }
        else{
            tabLayout=findViewById(R.id.mobile_tab_layout);
        }

        return false;

    }
}

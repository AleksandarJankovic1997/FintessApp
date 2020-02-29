package com.example.myapplication.myapplication.Fragments;


import android.location.Location;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.myapplication.Activities.ApplicationMainActivity;
import com.example.myapplication.myapplication.Models.User;
import com.example.myapplication.myapplication.Models.UserModelSingleton;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {
    private ImageView profilePicture;
    private TextView firstName;
    private TextView lastName;
    private TextView gender;
    private TextView age;
    private TextView nickname;
    private TextView country;
    private TextView city;
    private TextView totalStepsToday;
    private Button statistic;


    public UserProfileFragment() {
    }
    public void instanciateComponentrs(View view){
        this.profilePicture=view.findViewById(R.id.profile_imageview);
        this.firstName=view.findViewById(R.id.profile_first_name);
        this.lastName=view.findViewById(R.id.profile_last_name);
        this.gender=view.findViewById(R.id.profile_gender);
        this.nickname=view.findViewById(R.id.profile_nickname);
        this.country=view.findViewById(R.id.profile_country);
        this.city=view.findViewById(R.id.profile_city);
        this.age=view.findViewById(R.id.profile_age);
        this.totalStepsToday=view.findViewById(R.id.profile_total_steps_today);
        this.statistic=view.findViewById(R.id.user_profile_statistic_button);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Toast.makeText(getContext(),"fragment",Toast.LENGTH_LONG).show();
        View view=inflater.inflate(R.layout.fragment_user_profile, container, false);
        instanciateComponentrs(view);
        populateFields();
        if(((ApplicationMainActivity)getActivity()).isTablet()){
            statistic.setVisibility(View.GONE);
        }

        else{

            statistic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   ((ApplicationMainActivity)getActivity()).replaceMobileStatistics();
                }
            });
        }

        return view;
    }
    public void populateFields(){
        User user=UserModelSingleton.getInstance().getUser();
        if(user.getFirstname()!=null){
            firstName.setText(user.getFirstname()); }
        if(user.getSurname()!=null){
            lastName.setText(user.getSurname()); }
        if(user.getGender()!=null){
            gender.setText(user.getGender()); }
        if(user.getAge()!=null){
            age.setText(user.getAge().toString()); }
        if(user.getCountry()!=null){
            country.setText(user.getCountry()); }
        if(user.getCity()!=null){
            city.setText(user.getCity()); }
        if(user.getNickname()!=null){
            nickname.setText(user.getNickname()); }
        if(user.isGuest()){
            Picasso.get().load("http://aux2.iconspalace.com/uploads/user-icon-256-535406120.png")
                    .resize(30,30)
                    .centerCrop()
                    .error(R.drawable.padlock)
                    .into(profilePicture);
        }
        else{
            Picasso.get().load(user.getPhotoadress()).resize(300,300).centerCrop().into(profilePicture);
        }

    }
}

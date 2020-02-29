package com.example.myapplication.myapplication.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.myapplication.Activities.ApplicationMainActivity;
import com.example.myapplication.myapplication.Activities.LoginActivity;
import com.example.myapplication.myapplication.Models.User;
import com.example.myapplication.myapplication.Models.UserModelSingleton;


import butterknife.BindView;
import butterknife.ButterKnife;


public class GuestFragment extends Fragment {
    private EditText guest_first_name;
    private EditText guest_last_name;
    private EditText guest_age;
    private EditText guest_nickname;
    private EditText guest_weight;
    private EditText guest_height;
    private Spinner guest_spinner;
    private Button back;
    private Button goasguest;

    public void instanciateCompoments(View v){
        guest_first_name= v.findViewById(R.id.guest_entered_first_name);
        guest_last_name=v.findViewById(R.id.guest_entered_last_name);
        guest_nickname=v.findViewById(R.id.guest_entered_nickname);
        guest_age=v.findViewById(R.id.guest_entered_ages);
        guest_spinner=v.findViewById(R.id.guest_spinner);
        back=v.findViewById(R.id.guest_return);
        goasguest=v.findViewById(R.id.guest_go_as_guest);
        guest_height=v.findViewById(R.id.guest_entered_height);
        guest_weight=v.findViewById(R.id.guest_entered_weight);
    }

    UserModelSingleton userModelSingleton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_guest, container, false);
        instanciateCompoments(view);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guest_first_name.setText("");
                guest_last_name.setText("");
                guest_age.setText("");
                guest_nickname.setText("");
                guest_weight.setText("");
                guest_height.setText("" );
                ((LoginActivity)getActivity()).fragmentBack();
            }
        });
        goasguest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean pom=true;
                if(guest_first_name.getText().toString().equalsIgnoreCase("")){
                    guest_first_name.setError("This field cannot be blank");
                    pom=false;
                }
                if(guest_last_name.getText().toString().equalsIgnoreCase("")){
                    guest_last_name.setError("This field cannot be blank");
                    pom=false;
                }
                if(guest_nickname.getText().toString().equalsIgnoreCase("")){
                    guest_nickname.setError("This field cannot be blank");
                    pom=false;
                }
                if(guest_age.getText().toString().equalsIgnoreCase("")) {
                    guest_age.setError("This field cannot be blank");
                    pom = false;
                }
                else if (Integer.parseInt(guest_age.getText().toString()) < 0 || Integer.parseInt(guest_age.getText().toString())>120){
                    guest_age.setError("Not a valid input");
                    pom=false;
                }
                if(guest_height.getText().toString().equalsIgnoreCase("")){
                    guest_height.setError("This field cannot be balnk");
                    pom=false;
                }
                else if (Integer.parseInt(guest_height.getText().toString())<80||Integer.parseInt(guest_height.getText().toString())>240){
                    guest_height.setError("Not a valid input");
                    pom=false;
                }
                if(guest_weight.getText().toString().equalsIgnoreCase("")){
                    guest_weight.setError("This field cannot be blank");
                    pom=false;
                }
                else if(Integer.parseInt(guest_weight.getText().toString())<35||Integer.parseInt(guest_weight.getText().toString())>250){
                    guest_weight.setError("Not a valid input");
                    pom=false;
                }

                if(pom){
                    User u=new User();
                    u.setGuest(true);
                    u.setNickname(guest_nickname.getText().toString());
                    u.setFirstname(guest_first_name.getText().toString());
                    u.setSurname(guest_last_name.getText().toString());
                    u.setAge(Integer.parseInt(guest_age.getText().toString()));
                    u.setColor(1);
                    u.setWeight(Integer.parseInt(guest_weight.getText().toString()));
                    u.setHeight(Integer.parseInt(guest_height.getText().toString()));
                    userModelSingleton=UserModelSingleton.getInstance();
                    userModelSingleton.setUser(u);
                    Intent intent=new Intent(getActivity(), ApplicationMainActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

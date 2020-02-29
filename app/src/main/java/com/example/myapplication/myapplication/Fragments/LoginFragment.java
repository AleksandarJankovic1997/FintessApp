package com.example.myapplication.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.myapplication.Activities.ApplicationMainActivity;
import com.example.myapplication.myapplication.Activities.LoginActivity;
import com.example.myapplication.myapplication.Models.User;
import com.example.myapplication.myapplication.Models.UserModelSingleton;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment extends Fragment {

   private LoginButton facebookLoginButton;
   private SignInButton googleLoginButton;
   private Button goAsGuestButton;
   private UserModelSingleton userModelSingleton;

   private CallbackManager callbackManager;
    private AccessToken accessToken;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmen
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        instanciateComponents(view);

        callbackManager=CallbackManager.Factory.create();

        facebookLoginButton.setPermissions(Arrays.asList("email","public_profile"));
        facebookLoginButton.setFragment(this);
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        userModelSingleton=UserModelSingleton.getInstance();
        goAsGuestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LoginActivity)getActivity()).replaceFragment();
            }
        });

        return view;
    }




    public void instanciateComponents(View view){
        this.goAsGuestButton=view.findViewById(R.id.go_as_guest_button);
        this.googleLoginButton=view.findViewById(R.id.sign_in_button);
        this.facebookLoginButton=view.findViewById(R.id.facebook_login_button);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken==null){
                Log.d("user loged out","aa");
            }
            else{
                loaduser(currentAccessToken);
            }
        }
    };
    private void loaduser(AccessToken accessToken){
        GraphRequest request=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    User u=new User();
                    u.setFirstname(object.getString("first_name"));
                    u.setSurname(object.getString("last_name"));
                    u.setNickname(object.getString("short_name"));
                    String id=object.getString("id");
                    u.setPhotoadress("https://graph.facebook.com/"+id+"/picture?type=normal");

                    UserModelSingleton.getInstance().setUser(u);
                    Intent intent=new Intent(getActivity(),ApplicationMainActivity.class);
                    startActivity(intent);
                }catch(Exception e){

                }
            }
        });
        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,short_name,id");
        request.setParameters(parameters);
        request.executeAsync();
    }
}

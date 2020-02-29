package com.example.myapplication.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.myapplication.R;
import com.example.myapplication.myapplication.Models.Training;
import com.example.myapplication.myapplication.Retrofit.FitnessAppService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.SensorsClient;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import retrofit2.Retrofit;


public class TabletActivityFragment extends Fragment {

    private Button startButton;

    private Button pauseButton;

    private Button finishButton;

    private ImageButton lockButton;

    private TextView totalDistance;

    private TextView totaltime;

    private TextView totalSteps;

    private TextView totalCalories;

    private TextView members;

    private LinearLayout linearLayout;

    private FitnessAppService fitnessAppService;
    private SensorsClient sensorsClient;
    OnDataPointListener onDataPointListener;



    private Retrofit retrofit;

    private boolean pom;
    public TabletActivityFragment(boolean b){
        this.pom=b;

    }
    public TabletActivityFragment() {
        // Required empty public constructor
    }
    public void registerfitnes(){
        GoogleSignInOptionsExtension fitnessOptions= FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA,FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED,FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_SPEED,FitnessOptions.ACCESS_READ).build();
        GoogleSignInAccount googleSignInAccount= GoogleSignIn.getAccountForExtension(getContext(),fitnessOptions);
        onDataPointListener=new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                List<Field> lista=dataPoint.getDataType().getFields();
                Value step=dataPoint.getValue(lista.get(0));
                Value distance=dataPoint.getValue(lista.get(1));
                Value calories=dataPoint.getValue(lista.get(2));
                Value speed=dataPoint.getValue(lista.get(3));
            }
        };
        sensorsClient= Fitness.getSensorsClient(getActivity(),googleSignInAccount);
        Task<Void> response=sensorsClient.add(new SensorRequest.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setDataType(DataType.TYPE_SPEED)
                .setDataType(DataType.TYPE_CALORIES_EXPENDED)
                .setDataType(DataType.TYPE_DISTANCE_DELTA)
                .setSamplingRate(1, TimeUnit.SECONDS)
                .build(),onDataPointListener);

    }
    public void unregisterFitness(){
        sensorsClient.remove(onDataPointListener);

    }
    public void instanciateComponentrs(View view){
        this.startButton=view.findViewById(R.id.tablet_start_button);
        this.pauseButton=view.findViewById(R.id.table_pause_button);
        this.finishButton=view.findViewById(R.id.tablet_finish_button);
        this.lockButton=view.findViewById(R.id.table_lock_button);
        totalDistance=view.findViewById(R.id.tablet_totaldistance);
        totaltime=view.findViewById(R.id.table_totaltime);
        totalSteps=view.findViewById(R.id.tablet_totalsteps);
        totalCalories=view.findViewById(R.id.table_totalcalories);
        linearLayout=view.findViewById(R.id.tablet_pausefinishlayout);
        this.members=view.findViewById(R.id.tablet_members);
        if(!pom){
            members.setVisibility(View.VISIBLE);
        }else{
            members.setVisibility(View.GONE);
        }
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tablet_activity, container, false);
        instanciateComponentrs(view);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
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


    public void setAlone(){

    }
    public void setTeam(){

    }
}

package com.example.myapplication.myapplication.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.myapplication.Activities.ApplicationMainActivity;
import com.example.myapplication.myapplication.Database.FeedReederDbHelper;
import com.example.myapplication.myapplication.Models.Training;
import com.example.myapplication.myapplication.Models.UserModelSingleton;
import com.example.myapplication.myapplication.Retrofit.FitnessAppService;
import com.example.myapplication.myapplication.Retrofit.Informacija;
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
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MobileActivityFragment extends Fragment implements PermissionsListener {
    private Button startButton;
    private Button pauseButton;
    private Button finishButton;
    private ImageButton lockButton;
    private LinearLayout linearLayout;
    private TextView totaltime;
    private TextView totalsteps;
    private TextView totaldistance;
    private TextView totalcalories;
    private boolean pom = false;
    private TextView members;

    private MapView mapView;
    private MapboxMap mapboxMap;

    private LocationListeningCallback callback = new LocationListeningCallback((ApplicationMainActivity)getActivity());

    private static Location lastLocation;

    private LocationEngine locationEngine;
    private PermissionsManager permissionsManager;

    private Retrofit retrofit;
    private FitnessAppService fitnessAppService;
    private SensorsClient sensorsClient;
    OnDataPointListener onDataPointListener;


    private boolean start=false;
    public MobileActivityFragment(boolean b) {
        pom = b;
    }

    public MobileActivityFragment() {
        // Required empty public constructor

    }

    public void setAlone() {
        members.setVisibility(View.GONE);
    }

    public void registerfitnes(){
        GoogleSignInOptionsExtension fitnessOptions=FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA,FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA,FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED,FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_SPEED,FitnessOptions.ACCESS_READ).build();
        GoogleSignInAccount googleSignInAccount=GoogleSignIn.getAccountForExtension(getContext(),fitnessOptions);
        onDataPointListener=new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                Toast.makeText(getContext(),"fasfa",Toast.LENGTH_LONG).show();
                List<Field> lista=dataPoint.getDataType().getFields();
                Value step=dataPoint.getValue(lista.get(0));
                Value distance=dataPoint.getValue(lista.get(1));
                Value calories=dataPoint.getValue(lista.get(2));
                Value speed=dataPoint.getValue(lista.get(3));
                totalsteps.setText(step.toString());
                totaldistance.setText(distance.toString());
                totalcalories.setText(calories.toString());
                totaltime.setText(speed.toString());
            }
        };
        sensorsClient=Fitness.getSensorsClient(getActivity(),googleSignInAccount);
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


    public void setTeam() {
        this.members.setVisibility(View.VISIBLE);
        Log.d("setteam","setteam");
        retrofit = new Retrofit.Builder().
                baseUrl("http://10.0.2.2:8080/location/").
                addConverterFactory(GsonConverterFactory.create())
                .build();
        fitnessAppService = retrofit.create(FitnessAppService.class);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                Informacija informacija;
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (Exception e) {
                    }
                    Log.d("ide","ide");
                    Call<List<Informacija>> call = fitnessAppService.traziInformacije();
                    call.enqueue(new Callback<List<Informacija>>() {
                        @Override
                        public void onResponse(Call<List<Informacija>> call, Response<List<Informacija>> response) {
                            if (!response.isSuccessful()) {
                            }
                            Log.d("informacije","informacije");
                            List<Informacija> lista = response.body();
                            mapboxMap.clear();
                            for (Informacija i : lista) {
                                Log.d("lokacija",i.toString());
                                mapboxMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(i.getLatitude(),i.getLongitude()))
                                        .title(i.getIme()));
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Informacija>> call, Throwable t) {
                        }
                    });
                     informacija = new Informacija(UserModelSingleton.getInstance().getUser().getFirstname() +
                            UserModelSingleton.getInstance().getUser().getSurname()
                            , lastLocation.getLatitude(), lastLocation.getLongitude());
                    Call<Informacija> call2 = fitnessAppService.posaljiInformaciju(informacija);
                    call2.enqueue(new Callback<Informacija>() {
                        @Override
                        public void onResponse(Call<Informacija> call, Response<Informacija> response) {
                            if (!response.isSuccessful()) {
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<Informacija> call, Throwable t) {

                        }
                    });
                }
            }
        };
        Thread t=new Thread(run);
        t.start();

        members.setVisibility(View.VISIBLE);

    }

    public void instanciateComponentrs(View view) {

        startButton = view.findViewById(R.id.mobile_start_button);
        pauseButton = view.findViewById(R.id.mobile_pause_button);
        finishButton = view.findViewById(R.id.mobile_finish_button);
        lockButton = view.findViewById(R.id.mobile_lock_button);
        linearLayout = view.findViewById(R.id.mobile_pausefinishlayout);
        totaltime = view.findViewById(R.id.mobile_totaltime);
        totalsteps = view.findViewById(R.id.mobile_totalsteps);
        totaldistance = view.findViewById(R.id.mobile_totaldistance);
        totalcalories = view.findViewById(R.id.mobile_totalcalories);
        members = view.findViewById(R.id.mobile_members);
        if(!this.pom){
            members.setVisibility(View.GONE);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Mapbox.getInstance(getContext(),"pk.eyJ1IjoiYWxla3NhbmRhcnIiLCJhIjoiY2swcDFqN29sMGZicjNmcXU4Y2hvY3piZCJ9.TyvnzCWq9FfYE6TUGeIH3w");
        View view = inflater.inflate(R.layout.fragment_mobile_activity, container, false);
        instanciateComponentrs(view);
        if(!pom){
            members.setVisibility(View.GONE);
        }
        mapView = view.findViewById(R.id.mobile_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                MobileActivityFragment.this.mapboxMap = mapboxMap;
                mapboxMap.setStyle(Style.DARK, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);

                    }
                });
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                finishButton.setClickable(false);
                finishButton.setBackgroundColor(getResources().getColor(R.color.blueformap));
                pauseButton.setBackgroundColor(getResources().getColor(R.color.blueformap));

                pauseButton.setClickable(false);

                start=true;
                registerfitnes();
            }
        });
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start){
                    finishButton.setClickable(true);
                    pauseButton.setClickable(true);
                    finishButton.setBackgroundColor(getResources().getColor(R.color.BluePrimary));
                    pauseButton.setBackgroundColor(getResources().getColor(R.color.BluePrimary));
                }
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startButton.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
                unregisterFitness();
                Training t=new Training();
                Date d=new Date();
                String sql="INSERT INTO training(date,distance,calories,duration,steps) values('"
                        +d.toString()+"','"
                        +totaldistance.getText().toString()+"','"
                        +totalcalories.getText().toString()+"','"
                        +totaltime.getText().toString()+"','"
                        +totalsteps.getText().toString()+"')";
                FeedReederDbHelper.getInstance(getContext()).insert(sql);
                start=false;
            }
        });
        return view;
    }

    private void enableLocationComponent(@NonNull Style style) {
        PermissionsManager permissionsManager=new PermissionsManager(this);
        if (PermissionsManager.areLocationPermissionsGranted(getContext())) {
            LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(getContext())
                    .bearingTintColor(R.color.BluePrimary)
                    .accuracyAlpha(0.5f)
                    .backgroundTintColor(R.color.BlueAccent)
                    .build();

            LocationComponentActivationOptions locationComponentActivationOptions = LocationComponentActivationOptions
                    .builder(getContext(), style)
                    .locationComponentOptions(locationComponentOptions)
                    .build();

            LocationComponent locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
        }else {
            permissionsManager=new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
            long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
            long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;

            locationEngine = LocationEngineProvider.getBestLocationEngine(getContext());

            LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                    .setPriority(LocationEngineRequest.PRIORITY_NO_POWER)
                    .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME)
                    .build();

            locationEngine.requestLocationUpdates(request, callback, Looper.getMainLooper());
            locationEngine.getLastLocation(callback);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!pom){
            members.setVisibility(View.GONE);
        }
        else{
            members.setVisibility(View.VISIBLE);
        }
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates(callback);
        }
        mapView.onStop();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }


    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(), "Potrebna je dozvola", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(getContext(), "nema dozvole", Toast.LENGTH_LONG).show();
        }
    }

    private static class LocationListeningCallback implements LocationEngineCallback<LocationEngineResult> {
        private final WeakReference<ApplicationMainActivity> mobileActivityFragmentWeakReference;

        LocationListeningCallback(ApplicationMainActivity mobileActivityFragment) {
            this.mobileActivityFragmentWeakReference = new WeakReference<>(mobileActivityFragment);
        }

        @Override
        public void onSuccess(LocationEngineResult result) {
            Location lastLocation = result.getLastLocation();

            MobileActivityFragment.setLastLocation(lastLocation);
        }
        @Override
        public void onFailure(@NonNull Exception exception) { }
    }

    public static void setLastLocation(Location lastLocation) {
        MobileActivityFragment.lastLocation = lastLocation;
    }
}





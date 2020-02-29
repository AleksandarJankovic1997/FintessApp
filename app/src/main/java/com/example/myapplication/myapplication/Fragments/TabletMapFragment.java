package com.example.myapplication.myapplication.Fragments;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.myapplication.Activities.ApplicationMainActivity;
import com.example.myapplication.myapplication.Models.UserModelSingleton;
import com.example.myapplication.myapplication.Retrofit.FitnessAppService;
import com.example.myapplication.myapplication.Retrofit.Informacija;
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
import com.mapbox.mapboxsdk.maps.MapFragment;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabletMapFragment extends Fragment implements PermissionsListener {

    private ApplicationMainActivity mainActivity;

    private MapView mapView;
    private MapboxMap mapboxMap;
    private static Location lastLocation;

    private LocationEngine locationEngine;
    private LocationListeningCallback callback=new LocationListeningCallback((ApplicationMainActivity)getActivity());
    private PermissionsManager permissionsManager;
    private Retrofit retrofit;
    private FitnessAppService fitnessAppService;
    public TabletMapFragment() {
        // Required empty public constructor
    }
    public void setTeam() {
        Log.d("setteam","setteam");
        retrofit = new Retrofit.Builder().
                baseUrl("10.0.2.2:8080/location").
                addConverterFactory(GsonConverterFactory.create())
                .build();
        fitnessAppService = retrofit.create(FitnessAppService.class);

        Runnable run = new Runnable() {
            @Override
            public void run() {
                Informacija informacija;
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (Exception e) {
                    }
                    Log.d("ide","ide");
                    Call<List<Informacija>> call = fitnessAppService.traziInformacije();
                    call.enqueue(new Callback<List<Informacija>>() {
                        @Override
                        public void onResponse(Call<List<Informacija>> call, Response<List<Informacija>> response) {
                            if (!response.isSuccessful()) {
                                Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(getContext(), "something went wrong", Toast.LENGTH_LONG).show();
                        }
                    });
                    informacija = new Informacija(UserModelSingleton.getInstance().getUser().getFirstname() +
                            UserModelSingleton.getInstance().getUser().getSurname()
                            , lastLocation.getLatitude(), lastLocation.getLongitude());
                    Toast.makeText(getContext(),lastLocation.toString(),Toast.LENGTH_SHORT).show();
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

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity=(ApplicationMainActivity)getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Mapbox.getInstance((ApplicationMainActivity)getActivity(),"pk.eyJ1IjoiYWxla3NhbmRhcnIiLCJhIjoiY2swcDFqN29sMGZicjNmcXU4Y2hvY3piZCJ9.TyvnzCWq9FfYE6TUGeIH3w");
        View view= inflater.inflate(R.layout.fragment_tablet_map, container, false);
        mapView=view.findViewById(R.id.tablet_mapview);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                TabletMapFragment.this.mapboxMap=mapboxMap;
                mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }



    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(getContext(),"potrebna je dozvola",Toast.LENGTH_LONG);
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
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
            TabletMapFragment.setLastLocation(lastLocation);
        }
        @Override
        public void onFailure(@NonNull Exception exception) { }
    }
    public static void setLastLocation(Location lastLocation) {
        TabletMapFragment.lastLocation = lastLocation;
    }
}

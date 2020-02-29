package com.example.myapplication.myapplication.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.myapplication.Database.FeedReederDbHelper;
import com.example.myapplication.myapplication.Models.Training;

import java.util.ArrayList;

public class StatisticFragment extends Fragment {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Training> list;

    private TextView today;
    private TextView week;
    private TextView motnch;
    private TextView year;
    private LinearLayout linearLayout;

    private Button personalstats;
    private Button teamsstats;
    private Button personaloverall;

    private int mode=1;

    private FeedReederDbHelper instance;

    public StatisticFragment() {
        // Required empty public constructor
    }
    public void disableTextView(){
        today.setVisibility(View.GONE);
        week.setVisibility(View.GONE);
        motnch.setVisibility(View.GONE);
        year.setVisibility(View.GONE);
    }
    public void enableTextview(){
        today.setVisibility(View.VISIBLE);
        week.setVisibility(View.VISIBLE);
        motnch.setVisibility(View.VISIBLE);
        year.setVisibility(View.VISIBLE);
    }
    public void instanciateComponentrs(View view){
        personalstats=view.findViewById(R.id.profile_stats_personal_button);
        teamsstats=view.findViewById(R.id.profile_stats_team_stats);
        personaloverall=view.findViewById(R.id.profile_stats_overall_stats);
        recyclerView=view.findViewById(R.id.profile_stats_recyclerview);
        today=view.findViewById(R.id.profile_statistics_steps_today);
        week=view.findViewById(R.id.profile_statistics_steps_week);
        motnch=view.findViewById(R.id.profile_statistics_steps_motch);
        year=view.findViewById(R.id.profile_statistics_steps_year);
        linearLayout=view.findViewById(R.id.profile_statistics_linear_layout);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_statistic, container, false);
       instanciateComponentrs(view);
       instance=FeedReederDbHelper.getInstance(getContext());
       String sql="INSERT INTO training(date,distance,calories,duration,steps) values('1.1.1991.','10','500','44','1500')";
       instance.insert(sql);
       personaloverall.setClickable(true);
       personaloverall.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(mode!=1){
                    recyclerView.setVisibility(View.GONE);
                    enableTextview();
                    mode=1;
               }
           }
       });
       personalstats.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(mode!=2){
                   disableTextView();
                   ArrayList<Training> lista=instance.getAll();
                   adapter=new ExampleAdapter(lista);
                   layoutManager=new LinearLayoutManager(getContext());
                   recyclerView.setHasFixedSize(true);
                   recyclerView.setLayoutManager(layoutManager);
                   recyclerView.setAdapter(adapter);
                   recyclerView.setVisibility(View.VISIBLE);
                   mode=2;
               }
           }
       });
       teamsstats.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(mode!=3){
                   disableTextView();
                   recyclerView.setVisibility(View.VISIBLE);
                   if((((ExampleAdapter)adapter)!=null))
                       ((ExampleAdapter)adapter).clearList();
                   mode=3;
               }
           }
       });

       return view;
    }


}

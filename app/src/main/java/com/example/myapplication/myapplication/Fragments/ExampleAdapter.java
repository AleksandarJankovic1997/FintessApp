package com.example.myapplication.myapplication.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.myapplication.Models.Training;
import com.example.myapplication.myapplication.Retrofit.Informacija;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.MyViewHolder> {
    private ArrayList<Training> lista;

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView textView1;
        public  TextView textView2;
        public  TextView textView3;
        public  TextView textView4;
        public  TextView textView5;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1=itemView.findViewById(R.id.user_stats_date);
            textView2=itemView.findViewById(R.id.user_stats_calories);
            textView3=itemView.findViewById(R.id.user_stats_distance);
            textView4=itemView.findViewById(R.id.user_stats_duration);
            textView5=itemView.findViewById(R.id.user_stats_koraci);
        }
    }
    public ExampleAdapter(ArrayList<Training> lista){
        this.lista=lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewlayout,parent,false);
        MyViewHolder  myViewHolder=new MyViewHolder(v);
        return myViewHolder;
    }
    public void clearList(){
        this.lista.clear();
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Training training=lista.get(position);
        holder.textView1.setText("Date:"+training.getDate());
        holder.textView2.setText(" total calories:"+training.getTotalCalories());
        holder.textView3.setText(" total distance:"+training.getTotaldistance());
        holder.textView4.setText(" total Duration:"+training.getTotalduration());
        holder.textView5.setText(" total steps:"+training.getTotalsteps());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}

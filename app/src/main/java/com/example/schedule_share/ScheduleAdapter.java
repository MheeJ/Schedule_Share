package com.example.schedule_share;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {


    private ArrayList<String> Data;

    public ScheduleAdapter(ArrayList<String> dData){
        this.Data = dData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_schedule.setText(Data.get(position));
    }

    @Override
    public int getItemCount() {
        return Data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_schedule;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_schedule = itemView.findViewById(R.id.schedule);
        }
    }
}
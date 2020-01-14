package com.example.schedule_share;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {


    private ArrayList<Schedule_info> arrayList;
    private Context context;

    public ScheduleAdapter(ArrayList<Schedule_info> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_list_item,parent,false);

        ViewHolder holder = new ViewHolder(view);

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.schedule.setText(arrayList.get(position).getSchedule());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView schedule;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.schedule = itemView.findViewById(R.id.schedule);

        }
    }
}

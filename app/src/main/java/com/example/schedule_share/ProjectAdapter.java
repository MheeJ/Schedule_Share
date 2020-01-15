package com.example.schedule_share;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.CustomViewHolder> {

    private ArrayList<Project_item> data;

    public ProjectAdapter(ArrayList<Project_item> list){
        this.data = list;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.CustomViewHolder holder, int position) {
        holder.tv_projectname.setText(data.get(position).getProject_name());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        public final TextView tv_projectname;
        public CustomViewHolder(View itemView) {
            super(itemView);
            this.tv_projectname = itemView.findViewById(R.id.project_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Context context = v.getContext();
                    Intent intent = new Intent(context, Schedule_list.class);
                    intent.putExtra("number", data.get(pos).getProject_date());
                    intent.putExtra("project_name", data.get(pos).getProject_name());
                    context.startActivity(intent);
                }
            });
        }
    }
}
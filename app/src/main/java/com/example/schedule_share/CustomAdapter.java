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

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<Project_info> mData;
    private ArrayList<String> sData;
    private Context context;

    public CustomAdapter(ArrayList<Project_info> list, Context context){
        this.mData = list;
        this.context = context;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
        holder.tv_projectname.setText(mData.get(position).getProject_name());
    }

    @Override
    public int getItemCount() {
        return mData.size();
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
//                    if(pos != RecyclerView.NO_POSITION){
//                        Toast.makeText(context, mData.get(pos).getProject_date() + "", Toast.LENGTH_LONG).show();
//                    }

                    Context context = v.getContext();
                    Intent intent4 = new Intent(context,Project_main.class);
                    intent4.putExtra("number", mData.get(pos).getProject_date());
                    intent4.putExtra("project_name", mData.get(pos).getProject_name());
                    context.startActivity(intent4);
                }
            });
        }
    }
}
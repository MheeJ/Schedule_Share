package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Project_week extends AppCompatActivity implements View.OnClickListener {

    private TextView week;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.LayoutManager layoutManager2;
    private ArrayList<Schedule_info> arrayList2;
    private Button s_add;
    public int w;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.project_weeks);

        s_add = (Button)findViewById(R.id.make);
        s_add.setOnClickListener(this);
        week = findViewById(R.id.week);
        layoutManager2 = new LinearLayoutManager(this);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(layoutManager2);
        arrayList2 = new ArrayList<Schedule_info>(); //schedule_info 를 담을 어레이 리스트

        //아마 여기 파이어베이스 부분 넣는 곳


        w = intent.getIntExtra("week",0);
        week.setText(w+"주차");

        adapter2 = new ScheduleAdapter(arrayList2,this);
        recyclerView2.setAdapter(adapter2);

    }

    @Override
    public void onClick(View v) {

    }
}

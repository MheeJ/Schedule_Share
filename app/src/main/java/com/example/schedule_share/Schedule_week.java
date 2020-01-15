package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
public class Schedule_week extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_week;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> arrayList;
    private Button btn_add;
    private int week;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String project_name;
    static public HashMap<String, Object> result = new HashMap<>();
    static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.project_weeks);

        set();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<String>(); //schedule_info 를 담을 어레이 리스트

        week = intent.getIntExtra("week",0);
        project_name = intent.getStringExtra("project_name");
        tv_week.setText(week +"주차");

        firebasedatabase();

        adapter = new ScheduleAdapter(arrayList);
        recyclerView.setAdapter(adapter);
    }
    private void Map(){
        for(int k = 0;k<arrayList.size();k++) {
            result.put("schedule"+(k+1),arrayList.get(k));
            int a = 0;
        }
    }
    private void firebasedatabase(){
        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("Schedule_Share").child("project_list").child(project_name).child(week +"주차");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String schedule_info = snapshot.getValue().toString();
                    arrayList.add(schedule_info);
                }
                Map();
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }
        });
    }

    private void set() {
        btn_add = findViewById(R.id.s_add);
        btn_add.setOnClickListener(this);
        tv_week = findViewById(R.id.week);
        recyclerView = findViewById(R.id.recyclerView2);
    }

    @Override
    public void onClick(View v) {
        Intent addintent = new Intent(this,Schedule_add.class);
        addintent.putExtra("schedule_week", week);
        addintent.putExtra("project_name",project_name);
        startActivityForResult(addintent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1) {
            if (resultCode == 1) {
                firebasedatabase();
            }
//        }
    }

    @Override
    public void onBackPressed(){
        i = 0;
        result = new HashMap<>();
        super.onBackPressed();
        return;
    }
}

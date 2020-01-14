package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
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

public class Project_week extends AppCompatActivity implements View.OnClickListener {

    private TextView week;
    private RecyclerView recyclerView2;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.LayoutManager layoutManager2;
    private ArrayList<String> arrayList2;
    public Button s_add;
    public int w;
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

        s_add = findViewById(R.id.s_add);
        s_add.setOnClickListener(this);
        week = findViewById(R.id.week);
        layoutManager2 = new LinearLayoutManager(this);
        recyclerView2 = findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(layoutManager2);
        arrayList2 = new ArrayList<String>(); //schedule_info 를 담을 어레이 리스트

        //아마 여기 파이어베이스 부분 넣는 곳


        w = intent.getIntExtra("week",0);
        project_name = intent.getStringExtra("project_name");
        week.setText(w+"주차");

        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("project_list").child(project_name).child(w+"주차");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList2.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String schedule_info = snapshot.getValue().toString();
                    arrayList2.add(schedule_info);
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }

        });
        adapter2 = new ScheduleAdapter(arrayList2,this);
        recyclerView2.setAdapter(adapter2);

    }

    @Override
    public void onClick(View v) {
        Intent addintent = new Intent(this,Schedule_add.class);
        addintent.putExtra("schedule_week",w);
        addintent.putExtra("project_name",project_name);
        startActivity(addintent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("result");

            }
        }
    }

    @Override
    public void onBackPressed(){
        Project_week.i = 0;
        Project_week.result = new HashMap<>();
        super.onBackPressed();
        return;
    }
}

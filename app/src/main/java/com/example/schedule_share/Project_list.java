package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

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

public class Project_list extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Project_info> arrayList;
    ArrayList<String> listup;
    private Button btn_make;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    public String string;
    public String LogID;
    public String [] Team_list;
    public String [] Member_list;
    ArrayList<String> notice_list;
    ArrayAdapter<String> notice_adapter;
    String name_member = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);

        set();
       // setMember();


        database = FirebaseDatabase.getInstance();


        name_member = getIntent().getStringExtra("name_member");


        arrayList = new ArrayList<Project_info>();

        // 어댑터 생성
        notice_adapter = new ArrayAdapter<String>(Project_list.this,
                android.R.layout.simple_list_item_single_choice, notice_list);

        adapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        Search_ID();


    }

    public void Search_ID(){
        Intent intent = getIntent();
        LogID = intent.getStringExtra("logID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference("Schedule_Share").child("id_list");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 클래스 모델이 필요?
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String strname = (String) fileSnapshot.child("id").getValue();

                        if (strname.equals(LogID)) {
                            String strteam = (String) fileSnapshot.child("team").getValue();
                            Team_list = strteam.split(",");
                            Search_list();
                        }

                }
                databaseRef.removeEventListener(this);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });
    }


    public void Search_list(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference("Schedule_Share");
        databaseRef.child("project_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String strname = (String) snapshot.child("project_name").getValue();

                    //arrayList.clear();
                    for(int i=0; i<Team_list.length;i++) {
                        if (strname.equals(Team_list[i])) {

                                Project_info project_info = snapshot.getValue(Project_info.class);
                                arrayList.add(project_info);
                            }
                            adapter.notifyDataSetChanged();


                        
                    }

                }databaseRef.removeEventListener(this);

                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
               /* arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Project_info project_info = snapshot.getValue(Project_info.class);
                    arrayList.add(project_info);
                }
                adapter.notifyDataSetChanged();*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.make :
                Intent intent = new Intent(this,Make_project.class);

                startActivity(intent);
                break;

        }
    }

    public void  set(){
        btn_make = (Button)findViewById(R.id.make);
        btn_make.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
    }
}

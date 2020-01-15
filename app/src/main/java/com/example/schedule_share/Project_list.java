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
    private ArrayList<Project_item> arrayList;
    private Button btn_make;
    private String string, LogID;
    private String [] Team_list;
    private ArrayList<String> notice_list;
    private ArrayAdapter<String> notice_adapter;
    private String name_member = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);

        set();

        name_member = getIntent().getStringExtra("name_member");
        arrayList = new ArrayList<Project_item>();
        notice_adapter = new ArrayAdapter<String>(Project_list.this,android.R.layout.simple_list_item_single_choice, notice_list);
        adapter = new ProjectAdapter(arrayList);
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
                arrayList.clear();
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
                    for(int i=0; i<Team_list.length;i++) {
                        if (strname.equals(Team_list[i])) {
                                Project_item project_item = snapshot.getValue(Project_item.class);
                                arrayList.add(project_item);
                            }
                            adapter.notifyDataSetChanged();
                    }
                }databaseRef.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.make) {
            Intent intent = new Intent(this, Project_add.class);
            startActivity(intent);
        }
    }

    private void  set(){
        btn_make = (Button)findViewById(R.id.make);
        btn_make.setOnClickListener(this);
        layoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
    }
}

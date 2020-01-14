package com.example.schedule_share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.GoalRow;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Project_main extends AppCompatActivity implements View.OnClickListener {

    public ArrayList<String> list = new ArrayList<>();
    int j;
    long i = 0;
    String project_name = "";
    String notice_project;
    TextView Project_Tiltle;
    Button GoNotice;
    ListView listView;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_main);
        Intent intent = getIntent();
        set();
        GoNotice.setOnClickListener(this);

        project_name = intent.getStringExtra("project_name");
        Project_Tiltle.setText(project_name);

        i = intent.getLongExtra("number",0);
        for( j = 1; j<i+1;j++){
            list.add(j+"주차");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);

    }



    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent weekintent = new Intent(Project_main.this,Project_week.class);
            weekintent.putExtra("week",position+1);
            weekintent.putExtra("project_name",project_name);
            startActivity(weekintent);
        }
    };



    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notice:
                Intent intent = new Intent(this,Project_Notice.class);
                intent.putExtra("Go_project", project_name);
                startActivity(intent);
        }
    }


    public void set(){
        listView = (ListView)findViewById(R.id.listview);
        Project_Tiltle = (TextView)findViewById(R.id.project_title);
        GoNotice = (Button)findViewById(R.id.notice);
    }
}
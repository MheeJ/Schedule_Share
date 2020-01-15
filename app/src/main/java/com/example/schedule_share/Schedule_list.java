package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class Schedule_list extends AppCompatActivity implements View.OnClickListener {

    public ArrayList<String> list = new ArrayList<>();
    private int j;
    private long week = 0;
    private String project_name = "";
    private TextView Project_Tiltle;
    private Button GoNotice;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_main);
        Intent intent = getIntent();
        set();

        project_name = intent.getStringExtra("project_name");
        Project_Tiltle.setText(project_name);

        week = intent.getLongExtra("number",0);
        for(j = 1; j< week +1; j++){
            list.add(j+"주차");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(listener);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent weekintent = new Intent(Schedule_list.this, Schedule_week.class);
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
        GoNotice.setOnClickListener(this);
    }
}
package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Project_main extends AppCompatActivity {

    public ArrayList<String> list = new ArrayList<>();
    long i = 0;
    String project_name;
    TextView Project_Tiltle;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_main);
        Intent intent = getIntent();
        ListView listView = (ListView)findViewById(R.id.listview);
        Project_Tiltle = (TextView)findViewById(R.id.project_title);

        project_name = intent.getStringExtra("project_name");
        Project_Tiltle.setText(project_name);
        i = intent.getLongExtra("number",0);
        for(int j = 0; j<i;j++){
            list.add("item"+j);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
    }
}

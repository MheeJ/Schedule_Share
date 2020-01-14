package com.example.schedule_share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Project_main extends AppCompatActivity{

    public ArrayList<String> list = new ArrayList<>();
    int j;
    long i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_main);
        Intent intent = getIntent();
        ListView listView = (ListView)findViewById(R.id.listview);

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
            startActivity(weekintent);
        }
    };
}

package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Project_Notice extends AppCompatActivity implements View.OnClickListener {

    Button Add_Notice, Delete_Notice, Finish_Notice;
    ListView listView1;
    EditText Edit_Notice;
    String text1 = "";
    String grup_notice = "";
    ArrayList<String> notice_list;
    ArrayAdapter<String> notice_adapter;
    private DatabaseReference mPostReference;
    String project = "";
    String mini;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_notice);

        setting();


        notice_list = new ArrayList<String>();

        // 어댑터 생성
        notice_adapter = new ArrayAdapter<String>(Project_Notice.this,
                android.R.layout.simple_list_item_single_choice, notice_list);

        // 어댑터 설정
        listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setAdapter(notice_adapter);
        listView1.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 하나의 항목만 선택할 수 있도록 설정

        Intent intent = getIntent();
        String getNotice = intent.getStringExtra("notice");
        grup_notice = getNotice;




        updateNotice();

    }

    public void updateNotice(){
        Search_Notice();
        notice_adapter.notifyDataSetChanged();
    }

    public void Search_Notice(){
        Intent intent = getIntent();
        project = intent.getStringExtra("Go_project");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference("Schedule_Share");
        databaseRef.child("project_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 클래스 모델이 필요?
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String strname = (String) fileSnapshot.child("project_name").getValue();
                    String strnotice = (String) fileSnapshot.child("project_notice").getValue();

                    Log.v("TAG: value is ", strname);

                    if(strname.equals(project)){
                        String[] array = strnotice.split(",");
                        for (int i = 0; i < array.length; i++) {
                            notice_list.add(array[i]);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });
    }

    private void startToast(String msg){
        Toast.makeText(Project_Notice.this, msg, Toast.LENGTH_SHORT).show();
    }

    public void addNotice(){
        if (!text1.isEmpty()) {                        // 입력된 text 문자열이 비어있지 않으면
            notice_list.add(text1);                          // items 리스트에 입력된 문자열 추가
            Edit_Notice.setText("");                           // EditText 입력란 초기화
            notice_adapter.notifyDataSetChanged();// 리스트 목록 갱신
           // searchID ="no";
            grup_notice = String.join(",",notice_list);
            startToast(grup_notice);
        }
    }

    public void deleteNotice(){
        int pos = listView1.getCheckedItemPosition(); // 현재 선택된 항목의 첨자(위치값) 얻기
        if (pos != ListView.INVALID_POSITION) {      // 선택된 항목이 있으면
            notice_list.remove(pos);                       // items 리스트에서 해당 위치의 요소 제거
            listView1.clearChoices();                 // 선택 해제
            notice_adapter.notifyDataSetChanged();
            grup_notice = String.join(",",notice_list);
            startToast(grup_notice);
            // 어답터와 연결된 원본데이터의 값이 변경된을 알려 리스트뷰 목록 갱신
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_notice:
                text1 = Edit_Notice.getText().toString();
                addNotice();
                break;
            case R.id.delete_notice:
                deleteNotice();
                break;
            case R.id.finish_notice:
                String notice_data = grup_notice;
                mRootRef.child("Schedule_Share").child("/project_list/").child(project).child("project_notice").setValue(grup_notice);
                Intent intent10 = new Intent();
                intent10.putExtra("edit_notice",notice_data);
                setResult(RESULT_OK,intent10);

                finish();
                break;
        }
    }


    public void setting(){
        Edit_Notice = (EditText)findViewById(R.id.edit_notice);
        Add_Notice = (Button)findViewById(R.id.add_notice);
        Add_Notice.setOnClickListener(this);
        Delete_Notice = (Button)findViewById(R.id.delete_notice);
        Delete_Notice.setOnClickListener(this);
        Finish_Notice = (Button)findViewById(R.id.finish_notice);
        Finish_Notice.setOnClickListener(this);
    }

}

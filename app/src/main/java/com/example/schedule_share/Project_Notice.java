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

public class Project_Notice extends AppCompatActivity implements View.OnClickListener {

    private Button btn_Add_Notice, btn_Delete_Notice, btn_Finish_Notice;
    private ListView listView;
    private EditText Edit_Notice;
    private String text = "";
    private String grup_notice = "";
    private ArrayList<String> notice_list;
    private ArrayAdapter<String> notice_adapter;
    private String project = "";
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_notice);

        findviewlist();
        onclicklist();

        notice_list = new ArrayList<String>();
        notice_adapter = new ArrayAdapter<String>(Project_Notice.this, android.R.layout.simple_list_item_single_choice, notice_list);

        listView.setAdapter(notice_adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 하나의 항목만 선택할 수 있도록 설정

        intent = getIntent();
        String getNotice = intent.getStringExtra("notice");
        grup_notice = getNotice;

        updateNotice();
    }

    private void updateNotice(){
        Search_Notice();
        notice_adapter.notifyDataSetChanged();
    }

    private void Search_Notice(){
        project = intent.getStringExtra("Go_project");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference("Schedule_Share");
        databaseRef.child("project_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String strname = (String) fileSnapshot.child("project_name").getValue();
                    String strnotice = (String) fileSnapshot.child("project_notice").getValue();
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

    private void addNotice(){
        if (!text.isEmpty()) {                        // 입력된 text 문자열이 비어있지 않으면
            notice_list.add(text);                          // items 리스트에 입력된 문자열 추가
            Edit_Notice.setText("");                           // EditText 입력란 초기화
            notice_adapter.notifyDataSetChanged();// 리스트 목록 갱신
            grup_notice = String.join(",",notice_list);
            startToast(grup_notice);
        }
    }

    private void deleteNotice(){
        int pos = listView.getCheckedItemPosition(); // 현재 선택된 항목의 첨자(위치값) 얻기
        if (pos != ListView.INVALID_POSITION) {      // 선택된 항목이 있으면
            notice_list.remove(pos);                       // items 리스트에서 해당 위치의 요소 제거
            listView.clearChoices();                 // 선택 해제
            notice_adapter.notifyDataSetChanged();
            grup_notice = String.join(",",notice_list);
            startToast(grup_notice);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_notice:
                text = Edit_Notice.getText().toString();
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


    private void findviewlist(){
        Edit_Notice = (EditText)findViewById(R.id.edit_notice);
        btn_Add_Notice = (Button)findViewById(R.id.add_notice);
        btn_Delete_Notice = (Button)findViewById(R.id.delete_notice);
        btn_Finish_Notice = (Button)findViewById(R.id.finish_notice);
        listView = (ListView) findViewById(R.id.listView1);
    }

    private void onclicklist(){
        btn_Add_Notice.setOnClickListener(this);
        btn_Delete_Notice.setOnClickListener(this);
        btn_Finish_Notice.setOnClickListener(this);
    }

}

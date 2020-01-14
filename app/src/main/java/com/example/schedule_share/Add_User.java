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
public class Add_User extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private Button Btn_serch,Btn_UserAdd,Btn_UserDelete,Btn_Finish;
    private EditText ed;
    private String text = "";
    private String searchID = "no";
    private String adapter_list ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);

        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(Add_User.this, android.R.layout.simple_list_item_single_choice, items);

        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // 하나의 항목만 선택할 수 있도록 설정

        findviewlist();
        onclicklist();

        Intent intent=getIntent();
        String addr = intent.getStringExtra("addr");
        adapter_list = addr;
    }

    public void Add_ID(){
        if(searchID.equals("yes")) {
            if (!text.isEmpty()) {                        // 입력된 text 문자열이 비어있지 않으면
                items.add(text);                          // items 리스트에 입력된 문자열 추가
                ed.setText("");                           // EditText 입력란 초기화
                adapter.notifyDataSetChanged();// 리스트 목록 갱신
                searchID ="no";
                adapter_list = String.join(",",items);
                startToast(adapter_list);
            }
        }
        else {
            startToast("해당 정보와 동일한 닉네임이 없습니다. 재검색 하십시오.");
        }
    }

    public void Delete_ID(){
        int pos = listView.getCheckedItemPosition(); // 현재 선택된 항목의 첨자(위치값) 얻기
        if (pos != ListView.INVALID_POSITION) {      // 선택된 항목이 있으면
            items.remove(pos);                       // items 리스트에서 해당 위치의 요소 제거
            listView.clearChoices();                 // 선택 해제
            adapter.notifyDataSetChanged();
        }
    }

    public void Search_ID(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference("Schedule_Share");
        databaseRef.child("id_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 클래스 모델이 필요?
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String strname = (String) fileSnapshot.child("name").getValue();
                    Log.v("TAG: value is ", strname);
                    if(strname.equals(text)){
                        startToast("해당 정보와 동일한 닉네임이 있습니다.");
                        searchID = "yes";
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
        Toast.makeText(Add_User.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search:
                text = ed.getText().toString();
                Search_ID();
                break;
            case R.id.btn_userAdd:
                Add_ID();
                break;
            case R.id.btn_userDelete:
                Delete_ID();
                break;
            case R.id.btn_finish:
                String data = adapter_list;
                Intent intent = new Intent();
                intent.putExtra("editAddr",data);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }

    private void findviewlist(){
        Btn_serch= findViewById(R.id.btn_search);
        ed = findViewById(R.id.newitem);
        Btn_UserAdd = findViewById(R.id.btn_userAdd);
        Btn_UserDelete = findViewById(R.id.btn_userDelete);
        Btn_Finish = findViewById(R.id.btn_finish);
        listView = findViewById(R.id.listView);
    }

    private void onclicklist(){
        Btn_serch.setOnClickListener(this);
        Btn_UserAdd.setOnClickListener(this);
        Btn_UserDelete.setOnClickListener(this);
        Btn_Finish.setOnClickListener(this);
    }
}

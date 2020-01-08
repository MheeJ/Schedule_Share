package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {
    //데이터베이스에서 관심 등록 했는지 검사후, 관심 등록 데이터 베이스에 업데이트 하기



    String getID, getPW;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getID = getIntent().getStringExtra("userID");
        getPW = getIntent().getStringExtra("userPW");
        getdatapu();

       // initView();



    }


    public void getdatapu() {
        // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference("Schedule_Share");
        databaseRef.child("id_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 클래스 모델이 필요?
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String strid = (String) fileSnapshot.child("id").getValue();
                    String strpw = (String) fileSnapshot.child("pw").getValue();
                    Log.v("TAG: value is ", strid);
                    Log.v("TAG: value is ", strpw);

                    if (strid.equals(getID))
                        {
                            if(strpw.equals(getPW)) {
                                Intent intent = new Intent(Login.this, Schedule_list.class);
                                startActivity(intent);
                                /*startActivity(new Intent(Login.this, Schedule_list.class));*/
                                Toast.makeText(Login.this, "로그인 성공", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                                finish();
                            }
                        }
                    else if(!strid.equals(getID))
                        {
                            //Toast.makeText(Login.this, "로그인 실패", Toast.LENGTH_LONG).show();
                            // startActivity(new Intent(Login.this, MainActivity.class));
                            finish();
                        }
                    //처음 로그인인거 어떻게 알지? 관심사 등록 되잇으면 관심사 안하도록 하자 관심사는 데이터 베이스에 저장해야 하나?
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG: ", "Failed to read value", databaseError.toException());
            }
        });
    }


}

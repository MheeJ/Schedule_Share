package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;


public class Login extends AppCompatActivity {
    //데이터베이스에서 관심 등록 했는지 검사후, 관심 등록 데이터 베이스에 업데이트 하기


    private FirebaseAuth mAuth;
    private String getID, getPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getID = getIntent().getStringExtra("userID");
        getPW = getIntent().getStringExtra("userPW");
        mAuth = FirebaseAuth.getInstance();
        LoginActivity();
    }

    private void LoginActivity() {
        String email = getID;
        String password = getPW;

        if (email.length()>0 && password.length()>0) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startToast("로그인 성공");
                                    Intent intent = new Intent(Login.this, Project_list.class);
                                    intent.putExtra("logID",getID);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthInvalidUserException e) {
                                        startToast("존재하지 않는 id 입니다.");
                                    } catch (FirebaseNetworkException e) {
                                        startToast("Firebase NetworkException");
                                    } catch (Exception e) {
                                        startToast("Exception");
                                    }finish();
                                }
                            }
                        });
            }else {
                startToast("이메일 형식이 맞지 않습니다.");
                finish();
            }
        }else{
            startToast("이메일 또는 비밀번호를 입력해주세요.");
            finish();
        }
    }

    private void startToast(String msg){
        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
    }
}

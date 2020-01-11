package com.example.schedule_share;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mPostReference;
    private FirebaseAuth mAuth;
    private FirebaseUser current;
    private static final String TAG = "EmailPassword";

    Button btn_Update;
    Button btn_Insert;
    Button btn_Select;
    EditText edit_ID;
    EditText edit_PW;
    EditText edit_Name;
    EditText edit_Age;
    TextView text_ID;
    TextView text_Name;
    TextView text_Age;
    TextView text_Gender;
    CheckBox check_Man;
    CheckBox check_Woman;
    CheckBox check_ID;
    CheckBox check_Name;
    CheckBox check_Age;

    String ID;
    String PW;
    String name;
    long age;
    String gender = "";
    String sort = "id";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex = new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);



        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        edit_ID = (EditText) findViewById(R.id.edit_id);
        edit_PW = (EditText) findViewById(R.id.edit_pw);
        edit_Name = (EditText) findViewById(R.id.edit_name);
        edit_Age = (EditText) findViewById(R.id.edit_age);
        check_Man = (CheckBox) findViewById(R.id.check_man);
        check_Man.setOnClickListener(this);
        check_Woman = (CheckBox) findViewById(R.id.check_woman);
        check_Woman.setOnClickListener(this);
        btn_Insert.setEnabled(true);
        edit_PW.setTransformationMethod(PasswordTransformationMethod.getInstance());

        mAuth = FirebaseAuth.getInstance();
    }

    public void setInsertMode() {
        edit_ID.setText("");
        edit_PW.setText("");
        edit_Name.setText("");
        edit_Age.setText("");
        check_Man.setChecked(false);
        check_Woman.setChecked(false);
        btn_Insert.setEnabled(true);

    }


    public boolean IsExistID() {
        boolean IsExist = arrayIndex.contains(ID);
        return IsExist;
    }

    public void postFirebaseDatabase(boolean add) {
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if (add) {
            FirebasePost post = new FirebasePost(ID, PW, name, age, gender);
            postValues = post.toMap();
        }
        childUpdates.put("Schedule_Share"+"/id_list/" + name, postValues);
        mPostReference.updateChildren(childUpdates);
    }




/*
    public String setTextLength(String text, int length) {
        if (text.length() < length) {
            int gap = length - text.length();
            for (int i = 0; i < gap; i++) {
                text = text + " ";
            }
        }
        return text;
    }*/


    public void onStrart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void createAccount() {
        // [START create_user_with_email]
        ID = edit_ID.getText().toString().trim();
        PW = edit_PW.getText().toString().trim();
        name = edit_Name.getText().toString();
        age = Long.parseLong(edit_Age.getText().toString());

        String email = ID;
        String password = PW;

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }
                    }
                });
        // [END create_user_with_email]
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                /*ID = edit_ID.getText().toString().trim();
                PW = edit_PW.getText().toString().trim();
                name = edit_Name.getText().toString();
                age = Long.parseLong(edit_Age.getText().toString());*/
                createAccount();
                if (!IsExistID()) {
                    postFirebaseDatabase(true);
                    setInsertMode();
                    Intent intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUp.this, "이미 존재하는 ID 입니다. 다른 ID로 설정해주세요.", Toast.LENGTH_LONG).show();
                }
                edit_ID.requestFocus();
                edit_ID.setCursorVisible(true);
                break;


            case R.id.check_man:
                check_Woman.setChecked(false);
                gender = "Man";
                break;

            case R.id.check_woman:
                check_Man.setChecked(false);
                gender = "Woman";
                break;

            case R.id.btn_login:
                Intent intent = new Intent(this,Login.class);
                startActivity(intent);
                break;


        }
    }


}
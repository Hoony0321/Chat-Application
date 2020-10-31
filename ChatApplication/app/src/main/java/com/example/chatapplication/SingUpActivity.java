package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chatapplication.extra_class.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SingUpActivity extends AppCompatActivity {

    //View 변수 선언
    private EditText name_editText;
    private EditText email_editText;
    private EditText password_editText;
    private EditText phoneNumber_editText;
    private Button signUp_btn;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        name_editText = (EditText)findViewById(R.id.signUp_name_editText);
        email_editText = (EditText)findViewById(R.id.signUp_email_editText);
        password_editText = (EditText)findViewById(R.id.signUp_password_editText);
        phoneNumber_editText = (EditText)findViewById(R.id.signUp_phoneNumber_editText);

        signUp_btn = (Button)findViewById(R.id.signUp_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterUser();
            }
        });



    }

    private void RegisterUser(){

        //회원가입 정보 담을 String 변수 선언 및 초기화
        String name = name_editText.getText().toString().trim();
        String email = email_editText.getText().toString().trim();
        String password = password_editText.getText().toString().trim();
        String phoneNumber = phoneNumber_editText.getText().toString().trim();

        final User user = new User(name, email,phoneNumber);


        //제대로 입력되지 않은 정보가 있을 시 Toast 생성
        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "이름을 입력하세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "이메일을 입력하세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "전화번호를 입력하세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        //제대로 입력되었으면 계속 진행
        progressDialog.setMessage("등록중입니다. 기다려 주세요...");
        progressDialog.show();

        //firebase에 회원가입 정보 입력 및 유저 생성
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            ConnectDatabse(user);
                            finish();
                            startActivity(new Intent(SingUpActivity.this, LoginActitvity.class));
                            Toast.makeText(LoginActitvity.context_login, "회원가입이 완료되었습니다.",Toast.LENGTH_LONG).show();
                        }
                        else{ //에러 발생
                            Toast.makeText(SingUpActivity.this, "회원가입 실패!",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }
    private void ConnectDatabse(User user){
        // Create a new user with a first and last name
        // Add a new document with a generated ID
        firebaseFirestore.collection("users").document(user.getEmail())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("TAG", "회원가입 성공");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("TAG","회원가입 실패");
                    }
                });

    }
}

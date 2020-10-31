package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActitvity extends AppCompatActivity {

    //회원가입 성공에 관련된 변수 선언
    public static Context context_login;

    private User curUser; //현재 접속하려는 유저;

    private Button signUpBtn;
    private Button loginBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    private EditText email_editText;
    private EditText password_editText;

    private String email = "";
    private String password = "";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actitvity);

        context_login = this;
        curUser = new User();

        signUpBtn = (Button)findViewById(R.id.signUp_btn);
        loginBtn = (Button)findViewById(R.id.login_btn);
        email_editText = (EditText)findViewById(R.id.email_editText);
        password_editText = (EditText)findViewById(R.id.password_editText);

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();



        //회원가입 버튼 클릭 이벤트
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActitvity.this, SingUpActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭 이벤트
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserLogin();
            }
        });

    }

    private void UserLogin(){
        email = email_editText.getText().toString().trim();
        password = password_editText.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"이메일을 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"비밀번호를 입력해주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인중입니다. 잠시 기다려주세요...");
        progressDialog.show();

        //로그인 정보 실행
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActitvity.this,"로그인 성공!", Toast.LENGTH_SHORT).show();
                            System.out.println("로그인 성공");
                            FindUserInfo();
                            //finish();
                            //startActivity(new Intent(LoginActitvity.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(LoginActitvity.this,"로그인 실패!", Toast.LENGTH_SHORT).show();
                            System.out.println("로그인 실패");
                        }
                    }
                });


    }

    private void FindUserInfo(){
        firestore.collection("users").whereEqualTo("email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if(task.isSuccessful()){
                           //현재 접속하려는 계정 정보 임시 변수에 저장
                           for(QueryDocumentSnapshot querySnapshot : task.getResult()){
                               curUser.setEmail(querySnapshot.get("email").toString());
                               curUser.setName(querySnapshot.get("name").toString());
                               curUser.setPhoneNumber(querySnapshot.get("phoneNumber").toString());
                               curUser.setNickname(querySnapshot.get("nickname").toString());
                               curUser.setProfileUri(querySnapshot.get("profileUri").toString());
                           }

                           if(curUser.getNickname().equals("None")){ //회원가입 후 처음 접속함. -> 닉네임 설정 액티비티로 전환
                               Intent intent = new Intent(LoginActitvity.this, FirstLoginActivity.class);
                               intent.putExtra("User",curUser);
                               finish();
                               startActivity(intent);
                           }
                           else{ // 닉네임이 이미 설정함 -> 처음 접속 x -> 바로 MainActivity 이동.
                               Intent intent = new Intent(LoginActitvity.this, MainActivity.class);
                               intent.putExtra("User",curUser);
                               finish();
                               startActivity(intent);
                           }
                       }
                       else{//오류 발생 -> Authentication에는 유저 정보가 있다고 나오지만 정작 database에는 유저 관련 정보가 없음.
                           Log.e("test", "Fail");
                       }

                    }

        });
        progressDialog.dismiss();
    }

}






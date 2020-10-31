package com.example.chatapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapplication.extra_class.User;
import com.example.chatapplication.main_fragment.chatfragment;
import com.example.chatapplication.main_fragment.friendfragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private User curUser;
    private Context context;

    FirebaseFirestore firebaseFirestore;

    private friendfragment friend_fragment;
    private chatfragment chat_fragment;
    private Fragment extra_fragment;


    private TextView main_label;

    private Button add_friend_btn;

    private Button friend_btn;
    private Button chat_btn;
    private Button extra_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        Intent intent = getIntent();
        curUser = intent.getParcelableExtra("User");

        firebaseFirestore = FirebaseFirestore.getInstance();


        friend_fragment = new friendfragment();
        chat_fragment = new chatfragment();
        extra_fragment = null; //아직 설정 안 함 - 추후에

        add_friend_btn = (Button)findViewById(R.id.add_friend_btn);

        main_label = (TextView)findViewById(R.id.MainLabel);

        friend_btn = (Button)findViewById(R.id.change_friend_fragment_btn);
        chat_btn = (Button)findViewById(R.id.change_chat_fragment_btn);
        extra_btn = (Button)findViewById(R.id.change_extra_fragment_btn);


        //---   버튼 클릭 리스너 설정   ---///
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.change_friend_fragment_btn :
                        onClick_friend_fragment();
                        break;
                    case R.id.change_chat_fragment_btn :
                        onClick_chat_fragment();
                        break;
                    case R.id.change_extra_fragment_btn :
                        onClick_extra_fragment();
                        break;
                }
            }
        };

        add_friend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User friend = new User();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("친구 추가");

                final EditText email_editText = new EditText(context);
                email_editText.setHint("친구 이메일을 입력하세요.");
                builder.setView(email_editText);

                // 확인 버튼 설정
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Text 값 받아서 로그 남기기
                        final String friend_email = email_editText.getText().toString();

                        firebaseFirestore.collection("users").whereEqualTo("email",friend_email).get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){ //입력한 이메일 정보의 유저가 존재하는 경우
                                            //현재 접속한 유저 firebaseFireStore 안 friendList에 친구 이메일 추가
                                            Map<String, Object> data = new HashMap<>();
                                            data.put("Test", "Test");

                                            firebaseFirestore.collection("users").document(curUser.getEmail())
                                                    .collection("friendList").document(friend_email).set(data);

                                            Toast toast = Toast.makeText(context,"adding friend successful",Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        else{ //입력한 이메일 정보의 유저가 존재하지 않는 경우

                                            Toast toast = Toast.makeText(context,"adding friend fail",Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    }
                                });





                    }
                });


                // 취소 버튼 설정
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();     //닫기
                        // Event
                    }
                });

                builder.show();
            }
        });

        friend_btn.setOnClickListener(listener);
        chat_btn.setOnClickListener(listener);
        extra_btn.setOnClickListener(listener);

        //첫 화면은 친구 화면으로
        onClick_friend_fragment();

    }


    public void onClick_friend_fragment(){
        main_label.setText("친구");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, friend_fragment,"friend").commitAllowingStateLoss();

    }

    public void onClick_chat_fragment(){
        main_label.setText("채팅");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, chat_fragment,"chat").commitAllowingStateLoss();

    }

    public void onClick_extra_fragment(){
        main_label.setText("TEST");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainLayout, extra_fragment,"extra").commitAllowingStateLoss();

    }

    public User getUserInfo(){
        return curUser;
    }

    public void addFriend(User friend){
        firebaseFirestore.collection("users").document(curUser.getEmail()).collection("friends").document(friend.getEmail())
                .set(friend)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context,"친구 추가 성공!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context,"친구 추가 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

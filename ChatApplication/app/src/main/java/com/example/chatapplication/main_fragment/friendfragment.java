package com.example.chatapplication.main_fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chatapplication.MainActivity;
import com.example.chatapplication.R;
import com.example.chatapplication.extra_class.MessageItem;
import com.example.chatapplication.extra_class.User;
import com.example.chatapplication.friendAdapter.FriendAdapter;
import com.example.chatapplication.friendAdapter.FriendItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class friendfragment extends Fragment {
    private User user;

    private CircleImageView circleImageView;
    private TextView text_name;
    private TextView text_nickname;

    private ListView listView;
    private FriendAdapter friendAdapter;
    private ArrayList<FriendItem> friendItems = new ArrayList<>();

    private FirebaseFirestore firebaseFirestore;

    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;






    ViewGroup viewGroup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.friend_fragment,container,false);

        progressDialog = new ProgressDialog(getActivity());

        // View 변수 설정
        text_name = (TextView)viewGroup.findViewById(R.id.text_name);
        text_nickname = (TextView)viewGroup.findViewById(R.id.text_nickname);
        circleImageView = (CircleImageView)viewGroup.findViewById(R.id.curUser_profileImg);
        listView = (ListView)viewGroup.findViewById(R.id.listView);

        firebaseFirestore = FirebaseFirestore.getInstance();
        friendAdapter = new FriendAdapter(friendItems, getLayoutInflater());
        listView.setAdapter(friendAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();


        //현재 접속한 User 정보 MainActivity에서 가져오기.
        user = ((MainActivity)getActivity()).getUserInfo();



        //friendFragment 화면 세팅
        setView();

        //리스트 뷰 클릭 이벤트
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog = createDialog(friendItems.get(position));
                alertDialog.show();
            }
        });



        return viewGroup;
    }

    public void setCircleImageView(String uri){
        Picasso.get().load(uri).into(circleImageView);
    }

    public void setFriendList(){
        firebaseFirestore.collection("users").document(user.getEmail()).collection("friendList").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){

                        User friend = new User();

                        //ArrayList 초기화
                        friendItems.clear();
                        //현재 접속한 계정의 친구 이메일 알아오기.
                        for(QueryDocumentSnapshot querySnapshot : task.getResult()){
                            Log.e("test",querySnapshot.getId().toString());
                           friend.setEmail(querySnapshot.getId());
                           setFriendItem(friend);
                        }
                    }
                    else{
                        Log.e("test", "Error");
                    }
                }
            });

    }

    public void setView(){

        progressDialog.setMessage("로딩중입니다.");
        progressDialog.show();
        //현재 유저 정보 세팅

        setCircleImageView(user.getProfileUri());
        text_name.setText(user.getName());
        text_nickname.setText(user.getNickname());

        //친구 정보 세팅
        setFriendList();

    }

    public void setFriendItem(User user){
        final User tmpUser = new User();
        tmpUser.setEmail(user.getEmail());


        firebaseFirestore.collection("users").whereEqualTo("email",tmpUser.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot snapshot : task.getResult()){
                                tmpUser.setName(snapshot.get("name").toString());
                                tmpUser.setNickname(snapshot.get("nickname").toString());
                                tmpUser.setProfileUri(snapshot.get("profileUri").toString());
                            }

                            FriendItem friendItem = new FriendItem(tmpUser.getProfileUri(), tmpUser.getName(), tmpUser.getNickname(),tmpUser.getEmail());
                            friendItems.add(friendItem);
                            //리스트뷰 갱신
                            friendAdapter.notifyDataSetChanged();
                        }
                        else{
                            Log.e("setFriendList : ","Error");
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public AlertDialog createDialog(final FriendItem friendItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        //--- 안에 들어가는 view 설정 및 inflate ---//
        View itemView = getLayoutInflater().inflate(R.layout.friend_item_layout,(ViewGroup) getView().getParent(),false);

        CircleImageView circleImageView = itemView.findViewById(R.id.profileImg);
        TextView text_name = itemView.findViewById(R.id.text_name);
        TextView text_nickname = itemView.findViewById(R.id.text_nickname);

        text_name.setText(friendItem.getName());
        text_nickname.setText(friendItem.getNickname());

        Picasso.get().load(friendItem.getProfile_uri()).into(circleImageView);
        //--------------------------------------//


        builder.setTitle("Friend Setting");
        builder.setView(itemView);

        //Dialog 버튼 설정// *일단 dialog 안에 들어있는 기본 제공 버튼들로 구성 -> 추후에 custom dialog로 layout 만들고 직접 설정할것.
        builder.setPositiveButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Close Dialog
                //다이얼로그는 버튼 클릭만 하면 알아서 닫히게 설정되어 있음. 따라서 아무것도 없어도 됨.
            }
        });

        builder.setNegativeButton("Delte Friend", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //친구 삭제


                Toast myToast = Toast.makeText(getActivity(),"Delete Successful", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

        builder.setNeutralButton("Create Chat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //현재 시각 얻어오기 & message 설정
                Calendar calendar = Calendar.getInstance();
                String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
                MessageItem messageItem1 = new MessageItem(user.getNickname(),"Start Chatting",time,user.getProfileUri());

                //DataBase - Realtime Databaes에 채팅방 만들기
                //database path에 '.'이 포함될수 없으므로 . 이후 문자열 날리기
                String reference;
                if(user.getEmail().compareTo(friendItem.getEmail()) < 0){ // user_email이 사전적 순서 앞섬.
                    reference = user.getEmail().substring(0,user.getEmail().lastIndexOf("."))
                            + "_"
                            + friendItem.getEmail().substring(0,friendItem.getEmail().lastIndexOf("."));
                }
                else{ //friend_email이 사전적 순서 앞섬
                    reference = friendItem.getEmail().substring(0,friendItem.getEmail().lastIndexOf("."))
                            + "_"
                            + user.getEmail().substring(0,user.getEmail().lastIndexOf("."));
                }
                databaseReference = firebaseDatabase.getReference(reference);
                databaseReference.push().setValue(messageItem1);

                //유저 ChatList 정보업데이트
                Map<String, Object> data = new HashMap<>();
                data.put("Test", "Test");

                firebaseFirestore.collection("users").document(user.getEmail()).collection("chatList")
                        .document(reference).set(data);

                firebaseFirestore.collection("users").document(friendItem.getEmail()).collection("chatList")
                        .document(reference).set(data);


                Toast myToast = Toast.makeText(getActivity(),"Create Successful", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
        //---------------//

        return builder.create();
    }


}

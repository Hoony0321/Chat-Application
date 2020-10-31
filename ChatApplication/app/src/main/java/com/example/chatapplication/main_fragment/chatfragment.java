package com.example.chatapplication.main_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
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

import com.example.chatapplication.ChatActivity;
import com.example.chatapplication.MainActivity;
import com.example.chatapplication.R;
import com.example.chatapplication.extra_class.MessageItem;
import com.example.chatapplication.extra_class.User;
import com.example.chatapplication.friendAdapter.FriendAdapter;
import com.example.chatapplication.friendAdapter.FriendItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import org.w3c.dom.Document;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//여기서 FriendItem을 재탕하는데 원래는 새로 layout과 java class를 만들어야 함. 그냥 규격 비슷해서 대충 이용함.
//나중에 시간나면 수정하기
public class chatfragment extends Fragment {

    private User curUser;

    private ListView listView;

    public ArrayList<FriendItem> chatRooms = new ArrayList<>();
    private FriendAdapter chatRoomAdapter;

    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Toast tmpToast;
    private ProgressDialog progressDialog;

    private MessageItem messageItem;



    ViewGroup viewGroup;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.chat_fragment,container,false);
        tmpToast = null;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        curUser = ((MainActivity)getActivity()).getUserInfo();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        documentReference = firebaseFirestore.collection("users").document(curUser.getEmail());

        chatRoomAdapter = new FriendAdapter(chatRooms, getLayoutInflater());
        listView = (ListView)viewGroup.findViewById(R.id.listView);
        listView.setAdapter(chatRoomAdapter);

        messageItem = new MessageItem();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FriendItem itemClicked = chatRooms.get(position);

                //그냥 parcel 처리하기 귀찮아서 대충 형식만 맞추고 User 변수형으로 안에 내용 담은채 putExtra로 넘김.
                User tmpUser = new User(itemClicked.getEmail(), "test", "Test");
                tmpUser.setProfileUri(itemClicked.getProfile_uri());

                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("User",curUser);
                intent.putExtra("Info",tmpUser);
                startActivity(intent);


            }
        });

        SetChatRoom();
        return viewGroup;
    }


    public void SetChatRoom(){
        //ArrayList 초기화
        chatRooms.clear();
        //curUser의 chatList에서 chatRoom id 가져오기
        documentReference.collection("chatList").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot snapshot : task.getResult()){
                                String tmpRef = snapshot.getId();

                                int num = tmpRef.indexOf(curUser.getEmail().substring(0,curUser.getEmail().lastIndexOf(".")));
                                Log.e(tmpRef, curUser.getEmail().substring(0,curUser.getEmail().lastIndexOf(".")));
                                System.out.println(num);
                                if(num > 0){
                                    FriendItem friendItem = new FriendItem(curUser.getProfileUri(),tmpRef.substring(0,num),"Test",tmpRef);
                                    chatRooms.add(friendItem);
                                    chatRoomAdapter.notifyDataSetChanged();
                                }
                                else{
                                    FriendItem friendItem = new FriendItem(curUser.getProfileUri(),tmpRef.substring(curUser.getEmail().length()-3),"Chat",tmpRef);
                                    chatRooms.add(friendItem);
                                    chatRoomAdapter.notifyDataSetChanged();
                                }

                                progressDialog.dismiss();



                            }
                        }
                        else{ //chatList에서 id 정보 가져오기 실패
                            tmpToast = Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT);
                            tmpToast.show();

                            progressDialog.dismiss();
                        }


                    }
                });
    }



}

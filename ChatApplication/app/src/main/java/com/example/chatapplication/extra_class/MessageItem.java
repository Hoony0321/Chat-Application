package com.example.chatapplication.extra_class;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chatapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageItem {

    private String nickname;
    private String message;
    private String time;
    private String profileUri;

    public MessageItem(String name, String message, String time, String pofileUrl) {
        this.nickname = name;
        this.message = message;
        this.time = time;
        this.profileUri = pofileUrl;
    }



    //firebase DB에 객체로 값을 읽어올 때..
    //파라미터가 비어있는 생성자가 핑요함.
    public MessageItem() {
    }

    public String getProfileUri() {
        return profileUri;
    }

    public String getNickname() {
        return nickname;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class ChatAdapter extends BaseAdapter {

        ArrayList<MessageItem> messageItems;
        LayoutInflater layoutInflater;
        User user;

        public ChatAdapter(ArrayList<MessageItem> messageItems, LayoutInflater layoutInflater, User user) {
            this.messageItems = messageItems;
            this.layoutInflater = layoutInflater;
            this.user = user;
        }

        @Override
        public int getCount() {
            return messageItems.size();
        }

        @Override
        public Object getItem(int position) {
            return messageItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MessageItem messageItem = messageItems.get(position);

            View itemView = null;

            if (messageItem.getNickname().equals(user.getNickname())) {
                itemView = layoutInflater.inflate(R.layout.my_messagebox, parent, false);
            } else {
                itemView = layoutInflater.inflate(R.layout.other_messagebox, parent, false);
            }

            CircleImageView circleImageView = itemView.findViewById(R.id.profileImg);
            TextView tvName = itemView.findViewById(R.id.tv_name);
            TextView tvMsg = itemView.findViewById(R.id.tv_msg);
            TextView tvTime = itemView.findViewById(R.id.tv_time);

            tvMsg.setText(messageItem.getMessage());
            tvName.setText(messageItem.getNickname());
            tvTime.setText(messageItem.getTime());

            Picasso.get().load(messageItem.getProfileUri()).into(circleImageView);

            return itemView;


        }
    }
}

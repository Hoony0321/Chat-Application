package com.example.chatapplication.friendAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.chatapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends BaseAdapter {

    ArrayList<FriendItem> friendItems;
    LayoutInflater layoutInflater;

    public FriendAdapter(ArrayList<FriendItem> friendItems, LayoutInflater layoutInflater){
        this.friendItems = friendItems;
        this.layoutInflater = layoutInflater;
    }

    @Override
    public int getCount() {
        return friendItems.size();
    }

    @Override
    public Object getItem(int position) {
        return friendItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FriendItem friendItem = friendItems.get(position);

        View itemView = layoutInflater.inflate(R.layout.friend_item_layout,parent,false);


        CircleImageView circleImageView = itemView.findViewById(R.id.profileImg);
        TextView text_name = itemView.findViewById(R.id.text_name);
        TextView text_nickname = itemView.findViewById(R.id.text_nickname);

        text_name.setText(friendItem.getName());
        text_nickname.setText(friendItem.getNickname());

        Picasso.get().load(friendItem.getProfile_uri()).into(circleImageView);


        return itemView;
    }
}

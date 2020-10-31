package com.example.chatapplication.friendAdapter;

public class FriendItem {

    String profile_uri;
    String name;
    String nickname;
    String email;

    public FriendItem(String profile_uri, String name, String nickname,String email){
        this.profile_uri = profile_uri;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getProfile_uri() {
        return profile_uri;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfile_uri(String profile_uri) {
        this.profile_uri = profile_uri;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

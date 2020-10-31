package com.example.chatapplication.extra_class;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String name;
    private String email;
    private String phoneNumber;
    private String nickname;
    private String profileUri;



    public User(){
        name = "";
        email = "";
        phoneNumber = "";
        nickname = "None";
        profileUri = "";
    }

    public User(String _name, String _email, String _phoneNumber){
        name = _name;
        email = _email;
        phoneNumber = _phoneNumber;
        nickname = "None";
        profileUri = "";
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(nickname);
        dest.writeString(phoneNumber);
        dest.writeString(profileUri);

    }

    // create Parcelable
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Parcel parcel){
        this.name = parcel.readString();
        this.email = parcel.readString();
        this.nickname = parcel.readString();
        this.phoneNumber = parcel.readString();
        this.profileUri = parcel.readString();
    }

}

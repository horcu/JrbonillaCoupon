package com.horcu.apps.jrbonillacoupon;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hacz on 12/6/2015.
 */
public class User implements Parcelable {
    private String userName;
    private String phone;
    private String imageUrl;
    private String email;

    public User(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.phone);
        dest.writeString(this.email);
    }

    protected User(Parcel in) {
        this.userName = in.readString();
        this.imageUrl = in.readString();
        this.phone = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}

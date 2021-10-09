package com.android.book.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public  class User {

    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_firebase_id")
    @Expose
    private String firebaseId;
    @SerializedName("user_email")
    @Expose
    private String userEmail;


    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserPassword(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

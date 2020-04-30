package com.example.joeschat.models;

import java.io.Serializable;

public class UserModel implements Serializable {
    String phone,ImgeUrl,UserName,agee,Addarese,uid,status;

    public UserModel(String phone, String imgeUrl, String userName, String agee, String addarese, String uid, String status) {
        this.phone = phone;
        ImgeUrl = imgeUrl;
        UserName = userName;
        this.agee = agee;
        Addarese = addarese;
        this.uid = uid;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserModel() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImgeUrl() {
        return ImgeUrl;
    }

    public void setImgeUrl(String imgeUrl) {
        ImgeUrl = imgeUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getAgee() {
        return agee;
    }

    public void setAgee(String agee) {
        this.agee = agee;
    }

    public String getAddarese() {
        return Addarese;
    }

    public void setAddarese(String addarese) {
        Addarese = addarese;
    }
}

package com.example.joeschat.models;

public class ChatModel {
    String massage, name, image, senderid, receverid, time, massageid;
    private boolean isseen;

    public ChatModel(String massage, String name, String image, String senderid, String receverid, String time, String massageid, boolean isseen) {
        this.massage = massage;
        this.name = name;
        this.image = image;
        this.senderid = senderid;
        this.receverid = receverid;
        this.time = time;
        this.massageid = massageid;
        this.isseen = isseen;
    }

    public ChatModel() {
    }

    public boolean isIsseen() {
        return isseen;
    }

    public void setIsseen(boolean isseen) {
        this.isseen = isseen;
    }

    public String getMassage() {
        return massage;
    }

    public void setMassage(String massage) {
        this.massage = massage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getReceverid() {
        return receverid;
    }

    public void setReceverid(String receverid) {
        this.receverid = receverid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMassageid() {
        return massageid;
    }

    public void setMassageid(String massageid) {
        this.massageid = massageid;
    }
}

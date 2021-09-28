package com.example.voice_notes;



import com.google.firebase.database.ServerValue;

public class post {

    private String postKey;
    private String title;
    private String description;
    private String audio;
    private String userId;
    private Object timeStamp;
    private int like = 0;
    private String userName;


    public post(String title, String description, String audio, String userId,int like,String userName) {
        this.title = title;
        this.description = description;
        this.audio = audio;
        this.userId = userId;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.like = like;
        this.userName = userName;
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public post() {
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAudio() {
        return audio;
    }

    public String getUserId() {
        return userId;
    }


    public Object getTimeStamp() {
        return timeStamp;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

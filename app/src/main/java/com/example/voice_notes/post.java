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


    public post(String title, String description, String audio, String userId,int like) {
        this.title = title;
        this.description = description;
        this.audio = audio;
        this.userId = userId;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.like = like;
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
}

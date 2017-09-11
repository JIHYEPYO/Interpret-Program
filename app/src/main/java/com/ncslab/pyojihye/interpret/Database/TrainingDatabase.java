package com.ncslab.pyojihye.interpret.Database;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class TrainingDatabase {
    private String id;
    private String userName;
    private String time;
    private String wpm;
    private String text;

    public TrainingDatabase(String userName, String time, String wpm, String text) {
        this.userName = userName;
        this.time = time;
        this.wpm = wpm;
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWpm() {
        return wpm;
    }

    public void setWpm(String wpm) {
        this.wpm = wpm;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
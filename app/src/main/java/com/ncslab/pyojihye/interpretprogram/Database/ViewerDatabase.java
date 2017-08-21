package com.ncslab.pyojihye.interpretprogram.Database;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class ViewerDatabase {

    private String id;
    private String userName;
    private String time;
    private String delete;
    private String gap;
    private String text;

    public ViewerDatabase(String userName, String time, String delete, String gap, String text) {
        this.userName = userName;
        this.time = time;
        this.delete = delete;
        this.gap = gap;
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

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getGap() {
        return gap;
    }

    public void setGap(String gap) {
        this.gap = gap;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
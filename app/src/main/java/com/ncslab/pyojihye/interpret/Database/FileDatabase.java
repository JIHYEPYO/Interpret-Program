package com.ncslab.pyojihye.interpret.Database;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class FileDatabase {

    private String id;
    private String userName;
    private String time;
    private String location;
    private String fileName;

    public FileDatabase(String userName, String time, String location, String fileName) {
        this.userName = userName;
        this.time = time;
        this.location = location;
        this.fileName = fileName;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
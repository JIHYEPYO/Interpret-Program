package com.ncslab.pyojihye.interpret.Database;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class ModeDatabase {

    private String id;
    private String mode;
    private String userName;
    private String time;

    public ModeDatabase(String time, String userName, String mode) {

        this.time = time;
        this.userName = userName;
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
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
}

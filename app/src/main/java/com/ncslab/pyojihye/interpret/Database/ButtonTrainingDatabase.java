package com.ncslab.pyojihye.interpret.Database;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class ButtonTrainingDatabase {

    private String id;
    private String userName;
    private String time;
    private String pushButton;

    public ButtonTrainingDatabase(String userName, String time, String pushButton) {
        this.userName = userName;
        this.time = time;
        this.pushButton = pushButton;
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

    public String getPushButton() {
        return pushButton;
    }

    public void setPushButton(String pushButton) {
        this.pushButton = pushButton;
    }
}

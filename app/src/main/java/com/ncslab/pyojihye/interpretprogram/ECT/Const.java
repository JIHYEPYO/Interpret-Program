package com.ncslab.pyojihye.interpretprogram.ECT;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class Const {
    public static final String FIRSTRUN = "FirstRun";

    public static final String ANONYMOUS = "ANONYMOUS";

    public static final String MESSAGES_CHILD_FILE = "File";
    public static final String MESSAGES_CHILD_MODE = "Mode";
    public static final String MESSAGES_CHILD_TRAINING = "Training";
    public static final String MESSAGES_CHILD_VIEWER = "Viewer";

    public static final String MESSAGES_BUTTON_TRAINING = "ButtonTraining";
    public static final String MESSAGES_BUTTON_VIEWER = "ButtonViewer";
    public static final String MESSAGE_MENU="ButtonMenu";

    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseUser mFirebaseUser;
    public static DatabaseReference mFirebaseDatabaseReference;

    public static String mUsername;

    public static String FileName;
    public static String FilePath;

    public static boolean screenOFF;
    public static int Max;
    public static int LineNum;

    public static int wpm;
    public static int gap;
    public static List<String> delete = new ArrayList<String>();
    public static int delete_num;
    public static List<String> replace = new ArrayList<String>();


    public static boolean checkLoginAccount(){
        mUsername = ANONYMOUS;

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            return true;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
        }

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

        return false;
    }

    public static String currentTime(){
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return dayTime.format(new Date(time));
    }
}

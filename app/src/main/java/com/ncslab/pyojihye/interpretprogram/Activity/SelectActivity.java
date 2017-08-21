package com.ncslab.pyojihye.interpretprogram.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.ncslab.pyojihye.interpretprogram.Database.ButtonMenuDatabase;
import com.ncslab.pyojihye.interpretprogram.Database.ModeDatabase;
import com.ncslab.pyojihye.interpretprogram.R;

import static com.ncslab.pyojihye.interpretprogram.ECT.Const.ANONYMOUS;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.FileName;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.FilePath;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.MESSAGES_CHILD_MODE;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.MESSAGE_MENU;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.checkLoginAccount;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.currentTime;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.delete;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mFirebaseAuth;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mFirebaseDatabaseReference;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mUsername;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class SelectActivity extends AppCompatActivity{
    private final String TAG = "SelectModeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Select Mode");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
//        Log.d(TAG, "onCreate()");

        if (checkLoginAccount()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        if(FileName.equals(null) || FilePath.equals(null)){
            startActivity(new Intent(this, PathActivity.class));
            finish();
        }
    }

    public void onImageTrainingClick(View v) {
        ModeDatabase dataBase = new ModeDatabase(currentTime(), mUsername, "Training Mode");
        mFirebaseDatabaseReference.child(MESSAGES_CHILD_MODE).push().setValue(dataBase);

        Intent intentTrainingOption = new Intent(this, TrainingOptionActivity.class);
        startActivity(intentTrainingOption);
//        Log.d(TAG, "onImageTrainingClick()");
    }

    public void onImageViewerClick(View v) {
        delete.clear();

        ModeDatabase dataBase = new ModeDatabase(currentTime(), mUsername, "Viewer Mode");
        mFirebaseDatabaseReference.child(MESSAGES_CHILD_MODE).push().setValue(dataBase);

        Intent intentViewerOption = new Intent(this, ViewerOptionActivity.class);
        startActivity(intentViewerOption);
//        Log.d(TAG, "onImageViewerClick()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                ButtonMenuDatabase dataBase = new ButtonMenuDatabase(mUsername, currentTime(), "Sign Out", TAG);
                mFirebaseDatabaseReference.child(MESSAGE_MENU).push().setValue(dataBase);

                mFirebaseAuth.signOut();
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.developer:
                ButtonMenuDatabase dataBase2 = new ButtonMenuDatabase(mUsername, currentTime(), "Developer Info", TAG);
                mFirebaseDatabaseReference.child(MESSAGE_MENU).push().setValue(dataBase2);
                Intent intent = new Intent(getApplicationContext(), DeveloperActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
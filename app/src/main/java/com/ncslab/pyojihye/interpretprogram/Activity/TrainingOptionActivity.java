package com.ncslab.pyojihye.interpretprogram.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ncslab.pyojihye.interpretprogram.Database.ButtonMenuDatabase;
import com.ncslab.pyojihye.interpretprogram.R;

import static com.ncslab.pyojihye.interpretprogram.ECT.Const.ANONYMOUS;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.MESSAGE_MENU;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.checkLoginAccount;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.currentTime;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mFirebaseAuth;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mFirebaseDatabaseReference;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mUsername;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.screenOFF;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.wpm;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class TrainingOptionActivity extends AppCompatActivity {
    private final String TAG = "TrainingOptionActivity";

    public EditText editTextWPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate()");

        setTitle("Training Option");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_option);

        editTextWPM = (EditText) findViewById(R.id.editTextWPM);

        screenOFF = false;

        if (checkLoginAccount()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    public void onButtonStartClick(View v) {
//        Log.d(TAG, "onButtonStartClick()");

        if (editTextWPM.getText().toString().equals("")) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.editTextWPM), R.string.snack_bar_wpm, Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.parseColor("#FF0000")).setAction("YES", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            editTextWPM.setText("100");
                        }
                    });

            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else if (Integer.parseInt(editTextWPM.getText().toString()) <= 0) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.editTextWPM), R.string.snack_bar_wpm_minus, Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else if (Integer.parseInt(editTextWPM.getText().toString()) > 500) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.editTextWPM), R.string.snack_bar_wpm_plus, Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        } else {
            wpm = Integer.parseInt(editTextWPM.getText().toString());
            Intent intentStart = new Intent(this, TrainingActivity.class);
            startActivity(intentStart);
        }
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
                ButtonMenuDatabase database = new ButtonMenuDatabase(mUsername, currentTime(), "Sign Out", TAG);
                mFirebaseDatabaseReference.child(MESSAGE_MENU).push().setValue(database);

                mFirebaseAuth.signOut();
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, LoginActivity.class));
                return true;
            case R.id.developer:
                ButtonMenuDatabase database2 = new ButtonMenuDatabase(mUsername, currentTime(), "Developer Info", TAG);
                mFirebaseDatabaseReference.child(MESSAGE_MENU).push().setValue(database2);
                Intent intent = new Intent(getApplicationContext(), DeveloperActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
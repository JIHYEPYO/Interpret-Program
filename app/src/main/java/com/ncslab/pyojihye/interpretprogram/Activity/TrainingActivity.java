package com.ncslab.pyojihye.interpretprogram.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncslab.pyojihye.interpretprogram.Database.ButtonMenuDatabase;
import com.ncslab.pyojihye.interpretprogram.Database.ButtonTrainingDatabase;
import com.ncslab.pyojihye.interpretprogram.Database.TrainingDatabase;
import com.ncslab.pyojihye.interpretprogram.ECT.ModeTextView;
import com.ncslab.pyojihye.interpretprogram.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.ncslab.pyojihye.interpretprogram.ECT.Const.ANONYMOUS;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.FilePath;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.LineNum;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.MESSAGES_BUTTON_TRAINING;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.MESSAGES_CHILD_TRAINING;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.MESSAGE_MENU;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.Max;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.checkLoginAccount;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.currentTime;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mFirebaseAuth;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mFirebaseDatabaseReference;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mUsername;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.replace;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.screenOFF;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.wpm;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class TrainingActivity extends AppCompatActivity {

    private final String TAG = "TrainingActivity";

    private ModeTextView modeTextViewTraining;
    private ImageView imageViewStart;
    private ImageView imageViewRewind;
    private ImageView imageViewForward;
    private TextView numberPercent;
    private boolean startChange = false;

    private int currentPosition;
    private int startPosition;
    private int endPosition;

    private int currentLineNum;

    public String replaceTextView;
    private int num;
    public double percent;
    private boolean threadStart = false;
    private List<String> origin = new ArrayList<>();
    private TrainingThread trainingThread;

    private int twoScreenPage;
    private int substitute;
    private int wordLine;

    private boolean firstSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate()");
        if (!screenOFF) {
            setTitle("Training Mode");
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_training);

            modeTextViewTraining = (ModeTextView) findViewById(R.id.ModeTextViewTraining);
            Typeface face = Typeface.createFromAsset(getAssets(), "D2Coding.ttc");
            modeTextViewTraining.setTypeface(face);
            imageViewStart = (ImageView) findViewById(R.id.imageViewStart);
            imageViewRewind = (ImageView) findViewById(R.id.imageViewRewind);
            imageViewForward = (ImageView) findViewById(R.id.imageViewForward);
            imageViewStart.setImageResource(R.drawable.start);
            numberPercent = (TextView) findViewById(R.id.numberPercent);
            modeTextViewTraining.setVisibility(View.INVISIBLE);

            trainingThread = new TrainingThread();

//        Log.d(TAG, "onCreate()");
            if (checkLoginAccount()) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
//        Log.d(TAG, "onResume()");
        super.onResume();
        if (!screenOFF) {
            replace.clear();
            BufferedReader bufferedReader = null;
            FileInputStream fileInputStream;
            String text = null;
            StringBuffer buf;

            setControlBoxStart();
            imageViewStart.setImageResource(R.drawable.start);

            try {
                File path = new File(FilePath);
                fileInputStream = new FileInputStream(path);

                if (fileInputStream != null) {
                    bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                    buf = new StringBuffer();

                    while ((text = bufferedReader.readLine()) != null) {
                        buf.append(text + '\n');
                    }

                    int point = 1;

                    replace.clear();
                    origin.clear();

                    for (int i = 0; i < buf.length(); i++) {
                        if (buf.charAt(i) == ' ') {
                            replace.add(buf.substring(point, i + 1));
                            origin.add(buf.substring(point, i + 1));
                            point = i + 1;
                        }
                        if (buf.charAt(i) == '\n') {
                            replace.add(buf.substring(point, i));
                            replace.set(replace.size() - 1, replace.get(replace.size() - 1) + "\n");

                            origin.add(buf.substring(point, i));
                            origin.set(replace.size() - 1, origin.get(origin.size() - 1) + "\n");
                            point = i + 1;
                        }
                    }

                    fileInputStream.close();

                    text = "";

                    if (replace.size() > 20) {
                        for (int i = 0; i < 20; i++) {
                            text += replace.get(i);
                        }
                    } else {
                        for (int i = 0; i < replace.size(); i++) {
                            text += replace.get(i);
                        }
                    }

                    modeTextViewTraining.setText(text);
                    firstSet = true;

                    TrainingDatabase dataBase = new TrainingDatabase(mUsername, currentTime(), wpm+"", buf.toString());
                    mFirebaseDatabaseReference.child(MESSAGES_CHILD_TRAINING).push().setValue(dataBase);
                }
                PositionInit();
                num = 100;
                handler.sendEmptyMessage(1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void FirstSet() {
        if (firstSet) {
            int position = 1;
            currentLineNum = 0;
            firstSet = false;

            for (int i = 1; i < origin.size(); i++) {
                if (position + origin.get(i).length() < Max) {
                    position += origin.get(i).length();
                } else {
                    if (origin.get(i).contains(" ")) {
                        position = origin.get(i).length();
                    } else if (origin.get(i).contains("\n")) {
                        position = 0;
                    }
                    replace.set(i - 1, origin.get(i - 1) + "\n");
                    origin.set(i - 1, origin.get(i - 1) + "\n");
                }
            }

            if (origin.size() > 20) {
                for (int i = 0; i < 20; i++) {
                    if (origin.get(i).contains("\n")) {
                        currentLineNum++;
                    }
                }
                Log.d(TAG, "currentLineNum : " + currentLineNum);

                wordLine = 20 / currentLineNum;
                twoScreenPage = (wordLine * LineNum) * 2;
            } else {
                for (int i = 0; i < origin.size(); i++) {
                    if (origin.get(i).contains("\n")) {
                        currentLineNum++;
                    }
                }
                wordLine = replace.size() / currentLineNum;
                twoScreenPage = (wordLine * LineNum) * 2;
            }
        }
    }

    public void onButtonStartClick(View v) {
//        Log.d(TAG, "onButtonStartClick()");

        if (!startChange) {
            ButtonTrainingDatabase dataBase = new ButtonTrainingDatabase(mUsername, currentTime(), "Start Button(▶)");
            mFirebaseDatabaseReference.child(MESSAGES_BUTTON_TRAINING).push().setValue(dataBase);

            FirstSet();
            String str = "";

            if (replace.size() / twoScreenPage >= 1) {
                substitute = twoScreenPage;
            } else {
                substitute = replace.size();
            }

            for (int i = startPosition; i < substitute; i++) {
                str += replace.get(i);
            }
            modeTextViewTraining.setText(str);

            modeTextViewTraining.setVisibility(View.VISIBLE);
            startChange = true;
            setControlBoxStart();

            if (threadStart) {
                trainingThread.restart();
            } else {
                trainingThread.start();
            }
        } else {
            ButtonTrainingDatabase dataBase = new ButtonTrainingDatabase(mUsername, currentTime(), "Pause Button(||)");
            mFirebaseDatabaseReference.child(MESSAGES_BUTTON_TRAINING).push().setValue(dataBase);

            startChange = false;
//            change = false;
            setControlBoxPause();
        }
    }

    public void onButtonRewindClick(View v) {
//        Log.d(TAG, "onButtonRewindClick()");

        ButtonTrainingDatabase dataBase = new ButtonTrainingDatabase(mUsername, currentTime(), "Rewind Button(<<)");
        mFirebaseDatabaseReference.child(MESSAGES_BUTTON_TRAINING).push().setValue(dataBase);

        if (currentPosition != 0) {
            replace.set(currentPosition - 1, origin.get(currentPosition - 1));

            boolean change = false;
            for (int i = currentPosition - 2; i > 0; i--) {
                if (origin.get(i).contains("\n")) {
                    startPosition = i + 1;
                    change = true;
                    break;
                }
            }
            if (!change) {
                startPosition = 0;
            }

            String str = "";
            for (int i = startPosition; i < substitute; i++) {
                str += replace.get(i);
            }

            currentPosition--;
            replaceTextView = str;
            PercentCalculate();
            handler.sendEmptyMessage(0);
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.numberPercent), R.string.snack_bar_training, Snackbar.LENGTH_LONG);

            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    public void onButtonForwardClick(View v) {
//        Log.d(TAG, "onButtonForwardClick()");

        ButtonTrainingDatabase dataBase = new ButtonTrainingDatabase(mUsername, currentTime(), "Forward Button(>>)");
        mFirebaseDatabaseReference.child(MESSAGES_BUTTON_TRAINING).push().setValue(dataBase);

//        change = true;
        Training();
    }

    private void PositionInit() {
//        Log.d(TAG, "PositionInit()");

        currentPosition = 0;
        startPosition = 0;
        endPosition = replace.size();
        currentLineNum = 0;

//        Log.v("Word count: ", endPosition + "");
    }

    private void Training() {
//        Log.d(TAG, "Training()");
//        //전원버튼
//        IntentFilter offfilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(screenoff, offfilter);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        if (!pm.isScreenOn()) {
//            onButtonStartClick(view);
            trainingThread.pause();
            screenOFF = true;
            setControlBoxPause();
        } else {
            if (currentPosition < endPosition) {
                String text = "";

                String spaceBar = "";
                text = replace.get(currentPosition);

                for (int i = 0; i < text.length(); i++) {
                    if (text.startsWith("\n")) {
                        spaceBar = "\n";
                        currentLineNum++;
                    }
                    if (text.substring(i, text.length()).equals("\n") && !text.equals("\n")) {
                        spaceBar = spaceBar + "\n";
                        LineCalculate();
                        startPosition = currentPosition + 1;
                        break;
                    } else if (Character.getType(text.toCharArray()[i]) == 5) {
                        spaceBar = spaceBar + "  ";
                    } else {
                        spaceBar = spaceBar + " ";
                    }
                }

                replace.set(currentPosition, spaceBar);

                String str = "";
                for (int i = startPosition; i < substitute; i++) {
                    str += replace.get(i);
                }
                if (str.startsWith("\n")) {
                    str = str.substring(1, str.length());
                }
                replaceTextView = str;

                handler.sendEmptyMessage(0);
                currentPosition++;
            } else {
                startChange = true;
                trainingThread.pause();
            }
            PercentCalculate();
        }
    }

    private void PercentCalculate() {
        percent = ((double) currentPosition / (double) endPosition) * 100;
        if (percent < 99 && startChange) {
            percent++;
        }
        num = 100 - (int) percent;
//        Log.v("num: ", num + "");
        handler.sendEmptyMessage(1);
    }


    class TrainingThread extends Thread {
        private boolean pause = false;

        void pause() {
            pause = true;
        }

        synchronized void restart() {
            notify();
            pause = false;
//            Log.d(TAG, "restart()");
        }

        @Override
        public void run() {
            threadStart = true;
//            Log.d(TAG, "run()");

            while (true) {
                try {
                    if (startChange) {
                        sleep(60000 / wpm);
                        Training();
                        if (percent >= 100) {
                            pause();
                            handler.sendEmptyMessage(2);
                        }
                        synchronized (this) {
                            if (pause) {
                                wait();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    modeTextViewTraining.setText(replaceTextView);
                    break;
                case 1:
                    numberPercent.setText(num + "%");
                    break;
                case 2:
                    startChange = false;
                    AlertDialog.Builder d = new AlertDialog.Builder(TrainingActivity.this);
                    d.setTitle(getString(R.string.dialog_restart));
                    d.setMessage(getString(R.string.dialog_contents_restart));
                    d.setIcon(R.mipmap.ic_launcher);

                    d.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            screenOFF = false;
                            PositionInit();
                            replace.clear();
                            origin.clear();
                            onResume();
                            modeTextViewTraining.setVisibility(View.INVISIBLE);
                        }
                    });

                    d.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    d.show();
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG, "onKeyDown()");

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (trainingThread.isAlive()) {
                trainingThread.pause();
                trainingThread.interrupt();
            }
            if (screenOff.isOrderedBroadcast()) {
                unregisterBroadcast();
            }
        }
        return super.onKeyDown(keyCode, event);
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

    //전원버튼
    private void unregisterBroadcast() {
        unregisterReceiver(screenOff);
    }

    BroadcastReceiver screenOff = new BroadcastReceiver() {
        public static final String screenOff = "android.intent.action.SCREEN_OFF";

        @Override
        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(Screenoff)) {
//                if (trainingThread.isAlive()) {
//                    startChange = false;
//                    change = false;
//                    imageViewStart.setImageResource(R.drawable.firstSet);
//                    imageViewRewind.setImageResource(R.drawable.Rewind);
//                    imageViewForward.setImageResource(R.drawable.Forward);
//                    imageViewRewind.setClickable(true);
//                    imageViewForward.setClickable(true);
//                }
//            }
            if (!intent.getAction().equals(screenOff))
                return;
//            Log.e(TAG, "Screen off!!!!!!!");
        }
    };

    public void setControlBoxStart() {
        imageViewStart.setImageResource(R.drawable.pause);
        imageViewRewind.setImageResource(0);
        imageViewForward.setImageResource(0);
        imageViewRewind.setClickable(false);
        imageViewForward.setClickable(false);
    }

    public void setControlBoxPause() {
        imageViewStart.setImageResource(R.drawable.start);
        imageViewRewind.setImageResource(R.drawable.past);
        imageViewForward.setImageResource(R.drawable.future);
        imageViewRewind.setClickable(true);
        imageViewForward.setClickable(true);
    }

    public void LineCalculate() {
        currentLineNum++;
        if ((substitute - currentPosition) / wordLine < (LineNum / 2)*3) {
            if ((replace.size() - substitute) / (twoScreenPage / 2) >= 1) {
                substitute += twoScreenPage / 2;
            } else {
                substitute = replace.size();
            }
        }
        Log.d(TAG, "currentLineNum : " + currentLineNum);
        Log.d(TAG, "currentPosition : " + currentPosition);
        Log.d(TAG, "substitute : " + substitute);
        Log.d(TAG, "twoScreenPage : " + twoScreenPage);
        Log.d(TAG, "------------------------------------");
    }
}
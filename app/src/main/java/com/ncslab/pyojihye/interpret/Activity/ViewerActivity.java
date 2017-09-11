package com.ncslab.pyojihye.interpret.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.ncslab.pyojihye.interpret.Database.ButtonMenuDatabase;
import com.ncslab.pyojihye.interpret.Database.ViewerDatabase;
import com.ncslab.pyojihye.interpret.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.ncslab.pyojihye.interpret.ECT.Const.ANONYMOUS;
import static com.ncslab.pyojihye.interpret.ECT.Const.FilePath;
import static com.ncslab.pyojihye.interpret.ECT.Const.MESSAGES_CHILD_VIEWER;
import static com.ncslab.pyojihye.interpret.ECT.Const.MESSAGE_MENU;
import static com.ncslab.pyojihye.interpret.ECT.Const.checkLoginAccount;
import static com.ncslab.pyojihye.interpret.ECT.Const.currentTime;
import static com.ncslab.pyojihye.interpret.ECT.Const.delete;
import static com.ncslab.pyojihye.interpret.ECT.Const.gap;
import static com.ncslab.pyojihye.interpret.ECT.Const.mFirebaseAuth;
import static com.ncslab.pyojihye.interpret.ECT.Const.mFirebaseDatabaseReference;
import static com.ncslab.pyojihye.interpret.ECT.Const.mUsername;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class ViewerActivity extends AppCompatActivity {
    private final String TAG = "ViewerActivity";

    private List<String> word = new ArrayList<>();
    public TextView textViewViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(TAG, "onCreate()");
        setTitle("Viewer Mode");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);
        textViewViewer = (TextView) findViewById(R.id.textViewViewer);
        Typeface face = Typeface.createFromAsset(getAssets(), "D2Coding.ttc");
        textViewViewer.setTypeface(face);
        textViewViewer.setMovementMethod(new ScrollingMovementMethod());

        if (checkLoginAccount()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }


    @Override
    protected void onResume() {
//        Log.d(TAG, "onResume()");

        super.onResume();
        BufferedReader bufferedReader = null;
        FileInputStream fileInputStream;
        String strPath = null;

        try {
            File path = new File(FilePath);
            fileInputStream = new FileInputStream(path);

            if (fileInputStream != null) { //파일 존재시
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                StringBuffer buf = new StringBuffer();

                while ((strPath = bufferedReader.readLine()) != null) { //파일에 더이상 읽을 값이 없을때까지
                    buf.append(strPath + "\n");
                }

                String deleteWord = "";
                for (int i = 0; i < delete.size(); i++) {
                    if (i != delete.size() - 1) {
                        deleteWord += delete.get(i) + ", ";
                    } else {
                        deleteWord += delete.get(i);
                    }

                }
                ViewerDatabase dataBase = new ViewerDatabase(mUsername, currentTime(), deleteWord, gap+"", buf.toString());
                mFirebaseDatabaseReference.child(MESSAGES_CHILD_VIEWER).push().setValue(dataBase);

                //word 배열에 값 할당
                int point = 1;
                for (int i = 1; i < buf.length(); i++) {
                    if (buf.charAt(i) == ' ' || buf.charAt(i) == '\n') {
                        word.add(buf.substring(point, i + 1));
                        point = i + 1;
                    }
                }

                //삭제할 단어를 입력한 경우 먼저 시작
                Delete();
                Gap();

                fileInputStream.close();

                String str = "";
                for (String s : word) {
                    str += s;
                }
                textViewViewer.setText(str);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Gap() {
        //Gap이 0이 아닐때만 반복되도록 설정
        if (gap != 0) {
            String text;
            for (int i = 0; i < word.size(); i++) {
                if (i % (gap + 1) != 0) {
                    String underBar = "";
                    text = word.get(i);
                    for (int j = 0; j < text.length() - 1; j++) {
                        underBar += "_";
                    }
                    if (text.charAt(text.length() - 1) == '\n') {
                        underBar += "\n";
                    } else {
                        underBar += " ";
                    }
                    word.set(i, underBar);
                }
            }
        }
    }

    public void Delete() {
        //삭제할 단어를 입력한 경우에만
        if (delete.size() > 0) {
            String text;
            for (int i = 0; i < word.size(); i++) {
                for (int j = 0; j < delete.size(); j++) {

                    if (delete.get(j).contains(" ")) { //숙어부분
                        text = delete.get(j);
                        int spaceBarCount = 0;
                        for (int k = 0; k < text.length(); k++) {
                            if (text.charAt(k) == ' ') {
                                spaceBarCount++;
                            }
                        }
                        String compareText = "";
                        if(i!=word.size()-1){
                            for (int k = 0; k <= spaceBarCount; k++) {
                                compareText += word.get(i + k);
                            }
                        }

                        if (compareText.equalsIgnoreCase(delete.get(j)+" ") || compareText.equalsIgnoreCase(delete.get(j)+"\n")) {
                            String underBar = "";

                            for (int k = i; k <= i + spaceBarCount; k++) {
                                if (text.charAt(text.length() - 1) == '\n') {
                                    underBar = "_____\n";
                                } else {
                                    underBar = "_____ ";
                                }

                                word.set(k, underBar);
                            }
                        }
                    } else if (word.get(i).equalsIgnoreCase(delete.get(j) + " ") || word.get(i).equalsIgnoreCase(delete.get(j) + "\n")) {
                        text = word.get(i);

                        String underBar = "____";
                        if (text.charAt(text.length() - 1) == '\n') {
                            underBar += "\n";
                        } else {
                            underBar += " ";
                        }
                        word.set(i, underBar);
                    }
                }
            }
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
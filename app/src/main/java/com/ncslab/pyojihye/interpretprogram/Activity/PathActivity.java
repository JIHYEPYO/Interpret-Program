package com.ncslab.pyojihye.interpretprogram.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncslab.pyojihye.interpretprogram.Database.ButtonMenuDatabase;
import com.ncslab.pyojihye.interpretprogram.Database.FileDatabase;
import com.ncslab.pyojihye.interpretprogram.R;

import java.io.File;
import java.util.ArrayList;

import static com.ncslab.pyojihye.interpretprogram.ECT.Const.ANONYMOUS;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.FileName;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.FilePath;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.MESSAGES_CHILD_FILE;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.MESSAGE_MENU;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.checkLoginAccount;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.currentTime;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mFirebaseAuth;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mFirebaseDatabaseReference;
import static com.ncslab.pyojihye.interpretprogram.ECT.Const.mUsername;

/**
 * Created by PYOJIHYE on 2017-07-11.
 */

public class PathActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private final String TAG = "PathActivity";

    final private int APP_PERMISSION_STORAGE = 1;
    String root = "";
    String path = "";
    TextView textMsg;
    ListView listFile;
    ArrayList<String> arrayFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Select Path");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path);

        textMsg = (TextView) findViewById(R.id.textViewPath);
        checkPermission();
//        Log.d(TAG, "onCreate()");

        if (checkLoginAccount()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d(TAG, "onKeyDown()");

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder d = new AlertDialog.Builder(PathActivity.this);
            d.setTitle(getString(R.string.dialog_title));
            d.setMessage(getString(R.string.dialog_contents));
            d.setIcon(R.mipmap.ic_launcher);

            d.setPositiveButton(getString(R.string.dialog_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PathActivity.this.finish();
                }
            });

            d.setNegativeButton(getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            d.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void checkPermission() {
//        Log.d(TAG, "checkPermission()");

        if (android.os.Build.VERSION.SDK_INT < 23) {
            root = Environment.getExternalStorageDirectory().getAbsolutePath();
            String[] fileList = getFileList(root);

            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i] != null) {
//                Log.d("tag", fileList[i]);
                }
            }

            initListView();
            fileListArray(fileList);
        } else {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, R.string.permission_check, Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, APP_PERMISSION_STORAGE);

            } else {
                root = Environment.getExternalStorageDirectory().getAbsolutePath();
                String[] fileList = getFileList(root);

                for (int i = 0; i < fileList.length; i++) {
                    if (fileList[i] != null) {
//                Log.d("tag", fileList[i]);
                    }
                }
                initListView();
                fileListArray(fileList);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionResult()");

        root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String[] fileList = getFileList(root);

        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i] != null) {
//                Log.d("tag", fileList[i]);
            }
        }

        initListView();
        fileListArray(fileList);
    }

    public void initListView() {
//        Log.d(TAG, "initListView()");

        arrayFile = new ArrayList<String>();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayFile);

        listFile = (ListView) findViewById(R.id.listViewPath);
        listFile.setAdapter(adapter);
        listFile.setOnItemClickListener(this);
    }

    public void fileListArray(String[] fileList) {
//        Log.d(TAG, "fileListArray()");

        if (fileList == null) {
            return;
        }

        arrayFile.clear();
        if (root.length() < path.length()) {
            arrayFile.add("..");
        }

        for (int i = 0; i < fileList.length; i++) {
//            Log.d("tag",fileList[i]);
            arrayFile.add(fileList[i]);
        }

        ArrayAdapter adapter = (ArrayAdapter) listFile.getAdapter();
        adapter.notifyDataSetChanged();
    }

    public String[] getFileList(String strPath) {
//        Log.d(TAG, "getFileList()");

        File fileRoot = new File(strPath);

        if (fileRoot.isDirectory() == false) {
            return null;
        }

        path = strPath;
        textMsg.setText(path);
        String[] fileList = fileRoot.list();
        return fileList;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.d(TAG, "onItemClick()");

        FileName = arrayFile.get(position);
        FilePath = getCurrentPath(FileName);
        String[] fileList = getFileList(FilePath);
        fileListArray(fileList);

        if (FileName.endsWith("txt")) {
            FileDatabase dataBase = new FileDatabase(mUsername, currentTime(), FileName, FilePath);
            mFirebaseDatabaseReference.child(MESSAGES_CHILD_FILE).push().setValue(dataBase);
            Intent selectIntent = new Intent(this, SelectActivity.class);
            startActivity(selectIntent);
        } else if (FileName.contains(".") && !FileName.startsWith(".")) {
            Snackbar snackbar = Snackbar.make(parent.findViewById(R.id.listViewPath), R.string.snack_bar_format, Snackbar.LENGTH_LONG);
            View v = snackbar.getView();
            TextView textView = (TextView) v.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    public String getCurrentPath(String strFolder) {
//        Log.d(TAG, "getCurrentPath()");

        String strPath;
        if (strFolder == "..") {
            int pos = path.lastIndexOf("/");
            strPath = path.substring(0, pos);
        } else {
            strPath = path + "/" + strFolder;
        }
        return strPath;
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